/**
 * 
 */
package difficulty;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author Ringo
 * 
 */
public class ViabilityCalculator
{
	public static double calculateViability(final double p_mean,
		final double p_stdDev, final LinkedList<WinRecord> p_winRecord,
		final boolean p_expectLowWins)
	{
		double ret = 0;

		final ListIterator<WinRecord> pointIterator = p_winRecord.listIterator();

		double i = 1;
		while(pointIterator.hasNext())
		{
			final WinRecord p = pointIterator.next();
			double zScore = (p.getX() - p_mean) / p_stdDev;

			if(p_expectLowWins)
			{
				zScore *= -1;
			}

			if(!p.win())
			{
				zScore *= -1;
			}

			final LnNormCDFLinearization lncdfl = LinearizationCache.getLinearization(p.chanceOfCorrectGuess());
			final double lnNormCDF = lncdfl.lnNormCDFValue(zScore);
			if(lnNormCDF == Double.NEGATIVE_INFINITY)
			{
				return Double.NEGATIVE_INFINITY;
			}
			ret += lnNormCDF;// * i / p_winRecord.size();
			i++;
		}

		return ret;
	}
}
