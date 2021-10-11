/**
 * 
 */
package client;

/**
 * @author Ringo
 * 
 */
public enum GameUseType
{
	HIGH_GAME_USAGE, MID_GAME_USAGE, LOW_GAME_USAGE;

	public static GameUseType getType(final boolean p_playsFiveHours,
		final boolean p_playsAtAll)
	{
		if(p_playsFiveHours)
		{
			return HIGH_GAME_USAGE;
		}

		if(p_playsAtAll)
		{
			return MID_GAME_USAGE;
		}

		return LOW_GAME_USAGE;
	}

	public static GameUseType parseGameUseType(final String p_gutString)
	{
		if(p_gutString.equalsIgnoreCase("high"))
		{
			return HIGH_GAME_USAGE;
		}
		else if(p_gutString.equalsIgnoreCase("mid"))
		{
			return MID_GAME_USAGE;
		}
		else if(p_gutString.equalsIgnoreCase("low"))
		{
			return LOW_GAME_USAGE;
		}

		throw new RuntimeException("p_gutString = " + p_gutString);
	}

	@Override
	public String toString()
	{
		switch(this)
		{
			case HIGH_GAME_USAGE:
				return "High";
			case MID_GAME_USAGE:
				return "Mid";
			case LOW_GAME_USAGE:
				return "Low";
		}

		return null;
	}
}
