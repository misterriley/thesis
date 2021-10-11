/**
 * 
 */
package client;

/**
 * @author Ringo
 * 
 */
public enum GameType
{
	VISUAL, VERBAL, KINESTHETIC;

	public static GameType parseGameType(final String p_gtString)
	{
		if(p_gtString.equalsIgnoreCase("visual"))
		{
			return VISUAL;
		}
		else if(p_gtString.equalsIgnoreCase("verbal"))
		{
			return VERBAL;
		}
		else if(p_gtString.equalsIgnoreCase("kinesthetic"))
		{
			return KINESTHETIC;
		}

		throw new RuntimeException();
	}
}
