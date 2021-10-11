/**
 * 
 */
package difficulty;

import java.util.HashMap;

/**
 * @author Ringo
 * 
 */
public class LinearizationCache
{
	private static HashMap<Double, LnNormCDFLinearization>	m_map;

	static
	{
		m_map = new HashMap<Double, LnNormCDFLinearization>();
	}

	public static LnNormCDFLinearization getLinearization(
		final double p_leftXAsymptote)
	{
		final Double key = new Double(p_leftXAsymptote);
		LnNormCDFLinearization ret = m_map.get(key);
		if(ret == null)
		{
			ret = new LnNormCDFLinearization(p_leftXAsymptote);
			m_map.put(key, ret);
		}
		return ret;
	}
}
