/**
 * 
 */
package client;

/**
 * @author Ringo
 * 
 */
public class StringEnumeration
{
	private String[]	m_strings	= null;
	private int			m_index		= 0;

	public StringEnumeration(final String[] p_strings)
	{
		m_strings = p_strings;
	}

	public boolean hasMoreElements()
	{
		return m_index < m_strings.length;
	}

	public String nextElement()
	{
		return m_strings[m_index++].trim();
	}
}