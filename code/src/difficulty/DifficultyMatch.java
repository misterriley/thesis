package difficulty;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import org.apache.commons.math3.special.Erf;

import client.Utilities;

public class DifficultyMatch
{
	private static final double	MEAN_PRECISION			= .01;
	private static final double	SD_PRECISION			= .01;
	private static final double	DEFAULT_MEAN_SD_RATIO	= 5;
	private static final int	SECTIONS_PER_SEARCH		= 6;
	private static double[]		SD_VALUES				= new double[SECTIONS_PER_SEARCH + 1];

	private static double[] MEAN_VALUES = new double[SECTIONS_PER_SEARCH + 1];

	private static boolean hasWinsAndLosses(final LinkedList<WinRecord> p_winRecord)
	{
		int numWins = 0;
		int numLosses = 0;

		for (final WinRecord p : p_winRecord)
		{
			if (!p.win())
			{
				numLosses++;
			}
			else
			{
				numWins++;
			}
		}

		return numWins > 0 && numLosses > 0;
	}

	private final double	m_initialDifficulty;
	private final double	m_targetWinPercentage;

	private final double	m_inverseNormTWP;
	private final double	m_diffMultiplier;
	private final boolean	m_expectLowWins;
	private DoublePoint		m_lastReturnValue;

	private final GradientSearch	m_gs;
	private final Random			m_random;

	private double m_currentDifficulty;

	/**
	 * @param p_initialDifficulty
	 * @param p_targetWinPercentage
	 * @param p_expectLowWins
	 * @param maxDifficulty
	 */
	public DifficultyMatch(
		final double p_initialDifficulty,
		final double p_targetWinPercentage,
		final boolean p_expectLowWins,
		final int maxDifficulty)
	{
		m_initialDifficulty = p_initialDifficulty;
		m_targetWinPercentage = p_targetWinPercentage;
		m_inverseNormTWP = Math.sqrt(2) * Erf.erfInv(2 * m_targetWinPercentage - 1);
		m_diffMultiplier = 1.2;
		m_expectLowWins = p_expectLowWins;
		m_gs = new GradientSearch();
		m_gs.setXBounds(1, maxDifficulty);
		m_gs.setYBounds(maxDifficulty / 10, maxDifficulty);
		m_random = new Random();
	}

	public double estimateNeutralDifficulty(final LinkedList<WinRecord> p_winRecord)
	{
		if (hasWinsAndLosses(p_winRecord))
		{
			final double separationPoint = separationPoint(p_winRecord);
			if (separationPoint != -1)
			{
				return separationPoint;
			}

			return getLogRegCoeffs(false, p_winRecord).x;
		}
		else
			if (p_winRecord.size() > 0)
			{
				double sum = 0;
				int count = 0;
				for (final WinRecord p : p_winRecord)
				{
					if (p.win())
					{
						sum += p.getX();
						count++;
					}
				}

				if (count != 0)
				{
					return sum / count;
				}
			}

		return 0;
	}

	public double getNewDifficulty(final LinkedList<WinRecord> p_winRecord)
	{
		return getNewDifficulty(p_winRecord, false);
	}

	public double getNewDifficulty(final LinkedList<WinRecord> p_winRecord, final boolean p_addRandomness)
	{
		if (p_addRandomness)
		{
			return getNewDifficulty(p_winRecord, m_random, m_gs.getMinY() / 2);
		}

		return getNewDifficulty(p_winRecord, null, 0);
	}

	public double getNewDifficulty(
		final LinkedList<WinRecord> p_winRecord,
		final Random p_random,
		final double p_sdRandomness)
	{
		if (p_winRecord.size() == 0)
		{
			m_currentDifficulty = m_initialDifficulty;
		}
		else
		{
			int numWins = 0;
			int numLosses = 0;

			for (final WinRecord p : p_winRecord)
			{
				if (!p.win())
				{
					numLosses++;
				}
				else
				{
					numWins++;
				}
			}

			if (numLosses == 0)
			{
				if (m_expectLowWins)
				{
					m_currentDifficulty *= m_diffMultiplier;
				}
				else
				{
					m_currentDifficulty /= m_diffMultiplier;
				}
			}
			else
				if (numWins == 0)
				{
					if (m_expectLowWins)
					{
						m_currentDifficulty /= m_diffMultiplier;
					}
					else
					{
						m_currentDifficulty *= m_diffMultiplier;
					}
				}
				else
				{
					final double separationPoint = separationPoint(p_winRecord);
					if (separationPoint != -1)
					{
						m_lastReturnValue = getLogRegCoeffs(true, p_winRecord);
					}
					else
					{
						m_lastReturnValue = getLogRegCoeffs(false, p_winRecord);
					}

					final double difficulty = calculateTargetDifficulty(m_lastReturnValue.x, m_lastReturnValue.y);

					m_currentDifficulty = Utilities
						.clamp(
							difficulty,
							m_currentDifficulty * m_diffMultiplier,
							m_currentDifficulty / m_diffMultiplier);
				}
		}

		if (p_sdRandomness != 0)
		{
			m_currentDifficulty += p_random.nextGaussian() * p_sdRandomness;
		}

		return m_currentDifficulty;
	}

	private double calculateK(final double p_mean, final double p_stdDev, final LinkedList<WinRecord> p_winRecord)
	{
		double ret = 0;

		final ListIterator<WinRecord> pointIterator = p_winRecord.listIterator();

		double i = 1;
		while (pointIterator.hasNext())
		{
			final WinRecord p = pointIterator.next();
			double zScore = (p.getX() - p_mean) / p_stdDev;

			if (m_expectLowWins)
			{
				zScore *= -1;
			}

			if (!p.win())
			{
				zScore *= -1;
			}

			final LnNormCDFLinearization lncdfl = LinearizationCache.getLinearization(p.chanceOfCorrectGuess());
			final double lnNormCDF = lncdfl.lnNormCDFValue(zScore);
			if (lnNormCDF == Double.NEGATIVE_INFINITY)
			{
				return Double.NEGATIVE_INFINITY;
			}
			ret += lnNormCDF;
			i++;
		}

		return ret;
	}

	private double calculateTargetDifficulty(final double p_mean, final double p_stdDev)
	{
		double offsetFromMean = m_inverseNormTWP * p_stdDev;
		if (m_expectLowWins)
		{
			offsetFromMean *= -1;
		}
		final double ret = offsetFromMean + p_mean;
		return ret;
	}

	private DoublePoint getLogRegCoeffs(final boolean p_fixScaleFactor, final LinkedList<WinRecord> p_winRecord)
	{
		double minMean = m_gs.getMinX();
		double maxMean = m_gs.getMaxX();

		double minSD = m_gs.getMinY();
		double maxSD = m_gs.getMaxY();

		double maxK = Double.NEGATIVE_INFINITY;
		int bestMeanIndex = -1;
		int bestSDIndex = -1;

		double meanSpread = maxMean - minMean;
		double sdSpread = maxSD - minSD;

		if (p_fixScaleFactor)
		{
			while (true)
			{
				meanSpread = maxMean - minMean;
				sdSpread = maxSD - minSD;

				if (meanSpread < MEAN_PRECISION && sdSpread < SD_PRECISION)
				{
					break;
				}

				for (int meanIndex = 0; meanIndex <= SECTIONS_PER_SEARCH; meanIndex++)
				{
					final double meanFactor = (double) meanIndex / SECTIONS_PER_SEARCH;
					final double meanValue = minMean + meanFactor * meanSpread;
					MEAN_VALUES[meanIndex] = meanValue;
					final double sdValue = meanValue / DEFAULT_MEAN_SD_RATIO;
					SD_VALUES[meanIndex] = sdValue;
					final double kForTheseValues = calculateK(meanValue, sdValue, p_winRecord);

					if (kForTheseValues > maxK)
					{
						bestMeanIndex = meanIndex;
						bestSDIndex = meanIndex;
						maxK = kForTheseValues;
					}
				}

				final int lowMeanIndex = Math.max(0, bestMeanIndex - 1);
				final int highMeanIndex = Math.min(bestMeanIndex + 1, SECTIONS_PER_SEARCH);

				final int lowSFIndex = Math.max(0, bestSDIndex - 1);
				final int highSFIndex = Math.min(bestSDIndex + 1, SECTIONS_PER_SEARCH);

				minMean = MEAN_VALUES[lowMeanIndex];
				maxMean = MEAN_VALUES[highMeanIndex];

				minSD = SD_VALUES[lowSFIndex];
				maxSD = SD_VALUES[highSFIndex];
			}

			return new DoublePoint((minMean + maxMean) / 2, (minSD + maxSD) / 2);
		}

		return m_gs.findMaximumPoint(p_winRecord, m_lastReturnValue, m_expectLowWins);
	}

	private double separationPoint(final LinkedList<WinRecord> p_winRecord)
	{
		double extremeWinX = -1;
		double extremeLossX = -1;

		for (final WinRecord p : p_winRecord)
		{
			if (m_expectLowWins)
			{
				if (p.win())
				{
					if (extremeWinX == -1 || p.getX() > extremeWinX)
					{
						extremeWinX = p.getX();
					}
				}
				else
				{
					if (extremeLossX == -1 || p.getX() < extremeLossX)
					{
						extremeLossX = p.getX();
					}
				}
			}
			else
			{
				if (p.win())
				{
					if (extremeWinX == -1 || p.getX() < extremeWinX)
					{
						extremeWinX = p.getX();
					}
				}
				else
				{
					if (extremeLossX == -1 || p.getX() > extremeLossX)
					{
						extremeLossX = p.getX();
					}
				}
			}
		}

		if (m_expectLowWins)
		{
			if (extremeWinX <= extremeLossX)
			{
				return (extremeWinX + extremeLossX) / 2;
			}
		}
		else
		{
			if (extremeLossX <= extremeWinX)
			{
				return (extremeWinX + extremeLossX) / 2;
			}
		}

		return -1;
	}
}
