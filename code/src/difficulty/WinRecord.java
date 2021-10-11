/**
 * 
 */
package difficulty;

/**
 * @author Ringo
 * 
 */
public class WinRecord
{
	private final double	m_x;
	private final boolean	m_win;
	private final double	m_chanceOfCorrectGuess;

	public WinRecord(final double p_x, final boolean p_win,
		final double p_chanceOfCorrectGuess)
	{
		m_x = p_x;
		m_win = p_win;
		m_chanceOfCorrectGuess = p_chanceOfCorrectGuess;
	}

	public double chanceOfCorrectGuess()
	{
		return m_chanceOfCorrectGuess;
	}

	public double getX()
	{
		return m_x;
	}

	public boolean win()
	{
		return m_win;
	}
}
