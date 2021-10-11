/**
 * 
 */
package verbal;

/**
 * @author Ringo
 * 
 */
public class VerbalTaskDisplayListEntry
{
	private final String	m_string;
	private final boolean	m_rhymesWithTarget;

	public VerbalTaskDisplayListEntry(final String p_string,
		final boolean p_rhymesWithTarget)
	{
		m_string = p_string;
		m_rhymesWithTarget = p_rhymesWithTarget;
	}

	public String getString()
	{
		return m_string;
	}

	public boolean rhymesWithTarget()
	{
		return m_rhymesWithTarget;
	}
}
