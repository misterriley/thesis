/**
 * 
 */
package client;


/**
 * @author Ringo
 * 
 */
public class HTTPKeyManager
{
	public static String	GAME_USE_TYPE_KEY		= "GameUseType";
	public static String	AGE_KEY					= "Age";
	public static String	MID_TASK_DIFF_KEY		= "MidTaskDiff";
	public static String	END_TASK_DIFF_KEY		= "EndTaskDiff";
	public static String	COMMENTS_KEY			= "Comments";
	public static String	PRACTICE_KEY			= "Practice";
	public static String	TEST_KEY				= "Test";
	public static String	DIFFICULTY_KEY			= "Diff";
	public static String	RESULT_KEY				= "Result";
	public static String	GENDER_KEY				= "Gender";
	public static String	INCOME_KEY				= "Income";
	public static String	NEUROTICISM_SCALE_KEY	= "NeuroticismScale";
	public static String	RACE_KEY				= "Race";
	public static String	OTHER_RACE_TEXT_KEY		= "OtherRaceText";

	public static String	KEY_DELIMITER			= "_";

	public static String getEndTaskDiffKey(final GameType p_type)
	{
		return getTaskTypeKey(p_type) + KEY_DELIMITER + END_TASK_DIFF_KEY;
	}

	public static String getMidTaskDiffKey(final GameType p_type)
	{
		return getTaskTypeKey(p_type) + KEY_DELIMITER + MID_TASK_DIFF_KEY;
	}

	public static String getModeKey(final boolean p_isPractice)
	{
		if(p_isPractice)
		{
			return PRACTICE_KEY;
		}

		return TEST_KEY;
	}

	public static String getTaskTypeKey(final GameType p_type)
	{
		return p_type.toString();
	}

	public static String getTrialDiffKey(final GameType p_type,
		final boolean p_isPractice, final int p_trialIndex)
	{
		return getTrialKeyPrefix(p_type, p_isPractice, p_trialIndex)
			+ KEY_DELIMITER +
			DIFFICULTY_KEY;
	}

	public static String getTrialKeyPrefix(final GameType p_type,
		final boolean p_isPractice, final int p_trialIndex)
	{
		return getTaskTypeKey(p_type) + KEY_DELIMITER +
			getModeKey(p_isPractice) + KEY_DELIMITER +
			p_trialIndex;
	}

	public static String getTrialResultKey(final GameType p_type,
		final boolean p_isPractice, final int p_trialIndex)
	{
		return getTrialKeyPrefix(p_type, p_isPractice, p_trialIndex)
			+ KEY_DELIMITER +
			RESULT_KEY;
	}
}
