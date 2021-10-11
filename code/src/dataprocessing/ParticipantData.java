/**
 * 
 */
package dataprocessing;

import client.Constants;
import client.GameType;
import client.GameUseType;

/**
 * @author Ringo
 * 
 */
public class ParticipantData
{
	private int			m_id;
	private int			m_age;
	private int			m_kDiff;
	private int			m_kEndDiff;
	private int			m_visDiff;
	private int			m_visEndDiff;
	private int			m_verbDiff;
	private int			m_verbEndDiff;
	private int			m_gender		= Constants.NO_GENDER_DATA;
	private int			m_income		= Constants.NO_INCOME_DATA;
	private int			m_race			= Constants.NO_RACE_DATA;

	private double		m_neuroticism	= Constants.NO_NEUROTICISM_DATA;

	private GameUseType	m_gameUseType;

	private String		m_timeString;
	private String		m_comments;
	private String		m_otherRaceText;

	public int getAge()
	{
		return m_age;
	}

	public String getComments()
	{
		return m_comments;
	}

	public int getDeltaDiff(final GameType p_gameType)
	{
		return getEndDiff(p_gameType) - getTestDiff(p_gameType);
	}

	public int getEndDiff(final GameType p_gameType)
	{
		switch(p_gameType)
		{
			case KINESTHETIC:
				return getKEndDiff();
			case VERBAL:
				return getVerbEndDiff();
			case VISUAL:
				return getVisEndDiff();
			default:
				throw new RuntimeException();
		}
	}

	public GameUseType getGameUseType()
	{
		return m_gameUseType;
	}

	public int getGender()
	{
		return m_gender;
	}

	public int getID()
	{
		return m_id;
	}

	public int getIncome()
	{
		return m_income;
	}

	public int getKDiff()
	{
		return m_kDiff;
	}

	public int getKEndDiff()
	{
		return m_kEndDiff;
	}

	public double getNeuroticism()
	{
		return m_neuroticism;
	}

	public String getOtherRaceText()
	{
		return m_otherRaceText;
	}

	public int getRace()
	{
		return m_race;
	}

	public int getTestDiff(final GameType p_gameType)
	{
		switch(p_gameType)
		{
			case KINESTHETIC:
				return getKDiff();
			case VERBAL:
				return getVerbDiff();
			case VISUAL:
				return getVisDiff();
			default:
				throw new RuntimeException();
		}
	}

	public String getTimeString()
	{
		return m_timeString;
	}

	public int getVerbDiff()
	{
		return m_verbDiff;
	}

	public int getVerbEndDiff()
	{
		return m_verbEndDiff;
	}

	public int getVisDiff()
	{
		return m_visDiff;
	}

	public int getVisEndDiff()
	{
		return m_visEndDiff;
	}

	public void setAge(final int age)
	{
		m_age = age;
	}

	public void setComments(final String comments)
	{
		m_comments = comments;
	}

	public void setGameUseType(final GameUseType gameUseType)
	{
		m_gameUseType = gameUseType;
	}

	public void setGender(final int gender)
	{
		m_gender = gender;
	}

	public void setID(final int p_id)
	{
		m_id = p_id;
	}

	public void setIncome(final int income)
	{
		m_income = income;
	}

	public void setKDiff(final int kDiff)
	{
		m_kDiff = kDiff;
	}

	public void setKEndDiff(final int kEndDiff)
	{
		m_kEndDiff = kEndDiff;
	}

	public void setNeuroticism(final double neuroticism)
	{
		m_neuroticism = neuroticism;
	}

	public void setOtherRaceText(final String otherRaceText)
	{
		m_otherRaceText = otherRaceText;
	}

	public void setRace(final int race)
	{
		m_race = race;
	}

	public void setTimeString(final String timeString)
	{
		m_timeString = timeString;
	}

	public void setVerbDiff(final int verbDiff)
	{
		m_verbDiff = verbDiff;
	}

	public void setVerbEndDiff(final int verbEndDiff)
	{
		m_verbEndDiff = verbEndDiff;
	}

	public void setVisDiff(final int visDiff)
	{
		m_visDiff = visDiff;
	}

	public void setVisEndDiff(final int visEndDiff)
	{
		m_visEndDiff = visEndDiff;
	}
}
