/**
 * 
 */
package difficulty;

import java.util.LinkedList;

import client.Utilities;

/**
 * @author Ringo
 * 
 */
public class GradientSearch
{
	private static final double	INITIAL_GS_MULTIPLIER	= 1;
	private static final double	GRADIENT_PRECISION		= .01;
	private static final double	DELTA					= 1;

	private double				m_minX;
	private double				m_maxX;
	private double				m_minY;
	private double				m_maxY;

	public DoublePoint findMaximumPoint(
		final LinkedList<WinRecord> p_winRecord,
		final DoublePoint p_initialValue, final boolean p_expectLowWins)
	{
		double x = m_minX;
		double y = m_minY;
		if(p_initialValue != null)
		{
			x = p_initialValue.x;
			y = p_initialValue.y;
		}

		double multiplier = INITIAL_GS_MULTIPLIER;

		double k = ViabilityCalculator.calculateViability(x, y, p_winRecord,
			p_expectLowWins);

		double lastK = k;

		while(true)
		{
			final double dVdx = (ViabilityCalculator.calculateViability(x
				+ DELTA, y, p_winRecord, p_expectLowWins) - k)
				/ DELTA;
			final double dVdy = (ViabilityCalculator.calculateViability(x, y
				+ DELTA, p_winRecord, p_expectLowWins) - k)
				/ DELTA;

			final double length = Math.sqrt(dVdx * dVdx + dVdy * dVdy);

			while(true)
			{
				final double deltaX = dVdx / length * multiplier;
				final double deltaY = dVdy / length * multiplier;

				final double tempX = Utilities.clamp(x + deltaX, m_maxX,
					m_minX);
				final double tempY = Utilities.clamp(y + deltaY, m_maxY, m_minY);

				if((tempX <= m_minX || tempX >= m_maxX)
					&& (tempY <= m_minY || tempY >= m_maxY))
				{
					return new DoublePoint(tempX, tempY);
				}

				k = ViabilityCalculator.calculateViability(tempX, tempY,
					p_winRecord, p_expectLowWins);
				if(lastK >= k)
				{
					if(multiplier < GRADIENT_PRECISION)
					{
						final double retX = Utilities.clamp((x + tempX) / 2,
							m_maxX, m_minX);
						final double retY = Utilities.clamp((y + tempY) / 2,
							m_maxY, m_minY);
						return new DoublePoint(retX, retY);
					}

					multiplier /= 2;
				}
				else
				{
					x += deltaX;
					y += deltaY;
					lastK = k;
					break;
				}
			}
		}
	}

	public double getMaxX()
	{
		return m_maxX;
	}

	public double getMaxY()
	{
		return m_maxY;
	}

	public double getMinX()
	{
		return m_minX;
	}

	public double getMinY()
	{
		return m_minY;
	}

	public void setXBounds(final double p_minX, final double p_maxX)
	{
		m_minX = p_minX;
		m_maxX = p_maxX;
	}

	public void setYBounds(final double p_minY, final double p_maxY)
	{
		m_minY = p_minY;
		m_maxY = p_maxY;
	}
}