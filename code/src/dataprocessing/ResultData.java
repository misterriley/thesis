/**
 * 
 */
package dataprocessing;

import client.GameType;

/**
 * @author Ringo
 * 
 */
public class ResultData
{
	private int			m_resultID;
	private int			m_participantID;
	private int			m_trialIndex;
	private int			m_difficulty;

	private boolean		m_wasSuccess;
	private boolean		m_wasPractice;

	private GameType	m_gameType;

	public int getDifficulty()
	{
		return m_difficulty;
	}

	public GameType getGameType()
	{
		return m_gameType;
	}

	public int getParticipantID()
	{
		return m_participantID;
	}

	public int getResultID()
	{
		return m_resultID;
	}

	public int getTrialIndex()
	{
		return m_trialIndex;
	}

	public void setDifficulty(final int difficulty)
	{
		m_difficulty = difficulty;
	}

	public void setGameType(final GameType gameType)
	{
		m_gameType = gameType;
	}

	public void setParticipantID(final int participantID)
	{
		m_participantID = participantID;
	}

	public void setResultID(final int resultID)
	{
		m_resultID = resultID;
	}

	public void setTrialIndex(final int trialIndex)
	{
		m_trialIndex = trialIndex;
	}

	public void setWasPractice(final boolean wasPractice)
	{
		m_wasPractice = wasPractice;
	}

	public void setWasSuccess(final boolean wasSuccess)
	{
		m_wasSuccess = wasSuccess;
	}

	public boolean wasPractice()
	{
		return m_wasPractice;
	}

	public boolean wasSuccess()
	{
		return m_wasSuccess;
	}
}
