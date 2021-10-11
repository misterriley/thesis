/**
 * 
 */
package client;

import java.util.Comparator;
import java.util.Random;

/**
 * @author Ringo
 * 
 */
public class Randomizer
	implements Comparator<Object>
{
	private final Random	m_random	= new Random();

	public int compare(final Object p_arg0, final Object p_arg1)
	{
		return m_random.nextBoolean() ? 1 : -1;
	}
}
