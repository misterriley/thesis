/**
 * 
 */
package verbal;

/**
 * @author Ringo
 * 
 */
public class VerbalTaskWordListEntry
{
	public String	m_targetWord;
	public String[]	m_rhymingLookalikes;
	public String[]	m_rhymingNonLookalikes;
	public String[]	m_eyeRhymes;
	public String[]	m_noneOfTheAbove;

	public VerbalTaskWordListEntry(final String p_targetWord,
		final String[] p_rhymingLookalikes,
		final String[] p_rhymingNonLookalikes, final String[] p_eyeRhymes,
		final String[] p_noneOfTheAbove)
	{
		m_targetWord = p_targetWord;
		m_rhymingLookalikes = p_rhymingLookalikes;
		m_rhymingNonLookalikes = p_rhymingNonLookalikes;
		m_eyeRhymes = p_eyeRhymes;
		m_noneOfTheAbove = p_noneOfTheAbove;
	}

	public String[] getEyeRhymes()
	{
		return m_eyeRhymes;
	}

	public String[] getNoneOfTheAbove()
	{
		return m_noneOfTheAbove;
	}

	public String[] getRhymingLookalikes()
	{
		return m_rhymingLookalikes;
	}

	public String[] getRhymingNonLookalikes()
	{
		return m_rhymingNonLookalikes;
	}

	public String getTargetWord()
	{
		return m_targetWord;
	}

	public int numNonRhymes()
	{
		return m_eyeRhymes.length + m_noneOfTheAbove.length;
	}

	public int numRhymes()
	{
		return m_rhymingLookalikes.length + m_rhymingNonLookalikes.length;
	}

	public int totalWords()
	{
		return m_rhymingLookalikes.length + m_rhymingNonLookalikes.length
			+ m_eyeRhymes.length + m_noneOfTheAbove.length;
	}
}
