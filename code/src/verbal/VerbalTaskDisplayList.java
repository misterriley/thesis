/**
 * 
 */
package verbal;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Ringo
 * 
 */
public class VerbalTaskDisplayList
{
	private final String								m_targetWord;
	private final ArrayList<VerbalTaskDisplayListEntry>	m_list;
	private int											m_numRhymes;

	public VerbalTaskDisplayList(final String p_targetWord)
	{
		m_targetWord = p_targetWord;
		m_list = new ArrayList<VerbalTaskDisplayListEntry>();
		m_numRhymes = 0;
	}

	public void addToList(final String p_string,
		final boolean p_rhymesWithTarget)
	{
		m_list.add(new VerbalTaskDisplayListEntry(p_string, p_rhymesWithTarget));
		if(p_rhymesWithTarget)
		{
			m_numRhymes++;
		}
	}

	public ArrayList<VerbalTaskDisplayListEntry> getEntries()
	{
		return m_list;
	}

	public String getTargetWord()
	{
		return m_targetWord;
	}

	public int numRhymes()
	{
		return m_numRhymes;
	}

	public void shuffle()
	{
		Collections.shuffle(m_list);
	}

	public int size()
	{
		return m_list.size();
	}
}