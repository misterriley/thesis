/**
 * 
 */
package difficulty;

import org.apache.commons.math3.special.Erf;

/**
 * @author Ringo
 * 
 */
public class LnNormCDFLinearization
{
	private static final double	SQRT_2			= Math.sqrt(2);
	private final double[]		m_cachedValues;
	private final double		m_leftXAsymptote;

	private static final int	NUM_POINTS		= 10001;
	private static final int	NUM_SECTIONS	= NUM_POINTS - 1;
	private static final double	SUPPORT_MIN		= -40;
	private static final double	SUPPORT_MAX		= 40;
	private static final double	SUPPORT_SPAN	= SUPPORT_MAX - SUPPORT_MIN;
	private static final double	DELTA			= SUPPORT_SPAN / (NUM_SECTIONS);

	public LnNormCDFLinearization(final double p_leftXAsymptote)
	{
		m_leftXAsymptote = p_leftXAsymptote;
		m_cachedValues = new double[NUM_POINTS];

		for(int i = 0; i < NUM_POINTS; i++)
		{
			final double zScore = SUPPORT_MIN + (DELTA * i);
			final double lnProbitVal = Math.log(exactNormCDF(zScore));
			m_cachedValues[i] = lnProbitVal;
		}
	}

	private double exactNormCDF(final double p_zScore)
	{
		final double probit = .5 * (1 + Erf.erf(p_zScore / SQRT_2));
		final double adjusted = probit * (1 - m_leftXAsymptote)
			+ m_leftXAsymptote;
		return adjusted;
	}

	public double lnNormCDFValue(final double p_zScore)
	{
		if(p_zScore < SUPPORT_MIN)
		{
			if(m_leftXAsymptote == 0)
			{
				return Double.NEGATIVE_INFINITY;
			}

			return Math.log(m_leftXAsymptote);
		}

		if(p_zScore > SUPPORT_MAX)
		{
			return 0;
		}

		final double pointApprox = (p_zScore - SUPPORT_MIN) / SUPPORT_SPAN
			* NUM_SECTIONS;
		final int lowerEst = (int)pointApprox;
		final int higherEst = lowerEst + 1;
		final double fraction = pointApprox - lowerEst;

		if(higherEst >= m_cachedValues.length)
		{
			return m_cachedValues[lowerEst];
		}

		if(lowerEst < 0)
		{
			return m_cachedValues[higherEst];
		}

		if(m_cachedValues[lowerEst] == Double.NEGATIVE_INFINITY ||
			m_cachedValues[higherEst] == Double.NEGATIVE_INFINITY)
		{
			return Double.NEGATIVE_INFINITY;
		}

		final double ret = m_cachedValues[lowerEst] * (1 - fraction)
			+ m_cachedValues[higherEst] * fraction;

		return ret;
	}
}