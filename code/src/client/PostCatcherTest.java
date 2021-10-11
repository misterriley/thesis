/**
 * 
 */
package client;

import games.GameRecord;

/**
 * @author Ringo
 * 
 */
public class PostCatcherTest
{
	public static void main(final String[] p_args)
	{
		final GameRecord record = new GameRecord();
		record.addComments("||, -, *, /, <>, <, >, ,(comma), =, <=, >=, ~=, !=, ^=, (, )\'\"");
		record.setAge(25);
		record.setGameUseType(GameUseType.MID_GAME_USAGE);
		record.setNeuroticismAvg(2.5);
		record.setRaceIndicators(5);
		record.setOtherRaceText("Hurr durr");
		record.setIncomeIndicator(2);
		record.setGenderIndicator(3);
		for(final GameType g: GameType.values())
		{
			record.addResult(12, true, 3, g, true);
			record.addResult(10, false, 2, g, false);
			record.setMidTaskDiffSelection(4, g);
			record.setAfterTaskDiffSelection(5, g);
		}

		MainController.submitResults(record, null);
	}
}
