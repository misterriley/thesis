/**
 *
 */
package games;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import client.GameType;
import client.GameUseType;
import client.HTTPKeyManager;

/**
 * @author Ringo
 *
 */
public class GameRecord
{
	private final List<NameValuePair> m_data;

	public GameRecord()
	{
		m_data = new ArrayList<>();
	}

	public void addComments(final String p_comments)
	{
		try
		{
			final String key = HTTPKeyManager.COMMENTS_KEY;
			final String value = URLEncoder.encode(p_comments, "UTF-8");
			add(key, value);
		}
		catch (final UnsupportedEncodingException ex)
		{
			ex.printStackTrace();
		}
	}

	public void addResult(
		final int p_difficulty,
		final boolean p_succeeded,
		final int p_index,
		final GameType p_taskType,
		final boolean p_isPractice)
	{
		final String resultKey = HTTPKeyManager.getTrialResultKey(p_taskType, p_isPractice, p_index);
		add(resultKey, Integer.toString(p_difficulty) + HTTPKeyManager.KEY_DELIMITER + Boolean.toString(p_succeeded));
	}

	public void dumpToConsole()
	{
		for (final NameValuePair nvp : m_data)
		{
			System.out.println(nvp.getName() + "=" + nvp.getValue());
		}
	}

	/**
	 * @return
	 */
	public List<NameValuePair> getData()
	{
		return m_data;
	}

	public void setAfterTaskDiffSelection(final int p_afterTaskDiffSelection, final GameType p_type)
	{
		final String key = HTTPKeyManager.getEndTaskDiffKey(p_type);
		add(key, Integer.toString(p_afterTaskDiffSelection));
	}

	public void setAge(final int p_age)
	{
		add(HTTPKeyManager.AGE_KEY, Integer.toString(p_age));
	}

	public void setGameUseType(final GameUseType p_type)
	{
		add(HTTPKeyManager.GAME_USE_TYPE_KEY, p_type.toString());
	}

	public void setGenderIndicator(final int genderIndicator)
	{
		add(HTTPKeyManager.GENDER_KEY, Integer.toString(genderIndicator));
	}

	public void setIncomeIndicator(final int incomeIndicator)
	{
		add(HTTPKeyManager.INCOME_KEY, Integer.toString(incomeIndicator));
	}

	public void setMidTaskDiffSelection(final int p_midTaskDiffSelection, final GameType p_type)
	{
		final String key = HTTPKeyManager.getMidTaskDiffKey(p_type);
		add(key, Integer.toString(p_midTaskDiffSelection));
	}

	public void setNeuroticismAvg(final double p_neuroticismAvg)
	{
		add(HTTPKeyManager.NEUROTICISM_SCALE_KEY, Double.toString(p_neuroticismAvg));
	}

	public void setOtherRaceText(final String otherRaceText)
	{
		add(HTTPKeyManager.OTHER_RACE_TEXT_KEY, otherRaceText);
	}

	public void setRaceIndicators(final int p_raceIndicators)
	{
		add(HTTPKeyManager.RACE_KEY, Integer.toString(p_raceIndicators));
	}

	private void add(final String p_key, final String p_value)
	{
		m_data.add(new BasicNameValuePair(p_key, p_value));
	}
}
