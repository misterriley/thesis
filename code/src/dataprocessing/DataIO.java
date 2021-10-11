/**
 * 
 */
package dataprocessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;

import client.GameType;
import client.GameUseType;
import client.Utilities;

/**
 * @author Ringo
 * 
 */
public class DataIO
{
	private static final int		V2_PARTICIPANT_ID_SHIFT	= 100;
	private static final int		V2_RESULT_ID_SHIFT		= 10000;

	private static final String		DATA_PATH				= "C:/Users/Ringo/Dropbox/Thesis/data/";
	private static final String		PARTICIPANTS_V1			= "Participants_v1.csv";
	private static final String		PARTICIPANTS_V2			= "Participants_v2.csv";
	private static final String		RESULTS_V1				= "Results_v1.csv";
	private static final String		RESULTS_V2				= "Results_v2.csv";

	private static final String[]	PARTICIPANTS_HEADER		= {"ID", "AGE",
															"SUBMIT_TIME",
															"GAME_USAGE_TYPE",
															"K_DIFF",
															"K_END_DIFF",
															"VIS_DIFF",
															"VIS_END_DIFF",
															"VERB_DIFF",
															"VERB_END_DIFF",
															"COMMENTS",
															"GENDER",
															"INCOME",
															"NEUROTICISM",
															"RACE",
															"OTHER_RACE_TEXT"};
	private static final String[]	RESULTS_HEADER			= {"RESULT_ID",
															"PARTICIPANT_ID",
															"TASK_TYPE",
															"TRIAL_INDEX",
															"DIFFICULTY",
															"WAS_SUCCESS",
															"WAS_PRACTICE"};

	public static ArrayList<ParticipantData> getParticipants()
	{
		final ArrayList<ParticipantData> ret = new ArrayList<ParticipantData>();

		loadParticipants(ret, true);
		loadParticipants(ret, false);

		return ret;
	}

	private static BufferedReader getReader(final String p_file)
	{
		BufferedReader ret = null;

		try
		{
			ret = new BufferedReader(new FileReader(p_file));
		}
		catch(final FileNotFoundException ex)
		{
			ex.printStackTrace();
		}

		return ret;
	}

	public static ArrayList<ResultData> getResults()
	{
		final ArrayList<ResultData> ret = new ArrayList<ResultData>();

		loadResults(ret, true);
		loadResults(ret, false);

		return ret;
	}

	@SuppressWarnings("deprecation")
	private static void loadParticipants(
		final ArrayList<ParticipantData> p_list, final boolean p_isV1)
	{
		final String fileName = p_isV1 ? PARTICIPANTS_V1 : PARTICIPANTS_V2;
		final String filename = DATA_PATH + fileName;
		final BufferedReader reader = getReader(filename);

		String line = null;
		while(true)
		{
			try
			{
				line = reader.readLine();
			}
			catch(final IOException ex)
			{
				ex.printStackTrace();
				break;
			}

			if(line == null)
			{
				break;
			}

			final String[] bits = line.split(",");

			final ParticipantData data = new ParticipantData();
			p_list.add(data);

			for(int i = 0; i < bits.length; i++)
			{
				final String columnName = PARTICIPANTS_HEADER[i];
				String columnEntry = bits[i];
				if(columnEntry.startsWith("\""))
				{
					columnEntry = columnEntry.substring(1);
				}

				if(columnEntry.endsWith("\""))
				{
					columnEntry = columnEntry.substring(0,
						columnEntry.length() - 1);
				}

				if(columnName.equals("ID"))
				{
					int id = Integer.parseInt(columnEntry);
					if(!p_isV1)
					{
						id += V2_PARTICIPANT_ID_SHIFT;
					}
					data.setID(id);
				}
				else if(columnName.equals("AGE"))
				{
					data.setAge(Integer.parseInt(columnEntry));
				}
				else if(columnName.equals("SUBMIT_TIME"))
				{
					data.setTimeString(columnEntry);
				}
				else if(columnName.equals("GAME_USAGE_TYPE"))
				{
					data.setGameUseType(GameUseType.parseGameUseType(columnEntry));
				}
				else if(columnName.equals("K_DIFF"))
				{
					data.setKDiff(Integer.parseInt(columnEntry));
				}
				else if(columnName.equals("K_END_DIFF"))
				{
					data.setKEndDiff(Integer.parseInt(columnEntry));
				}
				else if(columnName.equals("VIS_DIFF"))
				{
					data.setVisDiff(Integer.parseInt(columnEntry));
				}
				else if(columnName.equals("VIS_END_DIFF"))
				{
					data.setVisEndDiff(Integer.parseInt(columnEntry));
				}
				else if(columnName.equals("VERB_DIFF"))
				{
					data.setVerbDiff(Integer.parseInt(columnEntry));
				}
				else if(columnName.equals("VERB_END_DIFF"))
				{
					data.setVerbEndDiff(Integer.parseInt(columnEntry));
				}
				else if(columnName.equals("COMMENTS"))
				{
					final String comments = URLDecoder.decode(columnEntry);
					data.setComments(comments);
				}
				else if(columnName.equals("GENDER"))
				{
					data.setGender(Integer.parseInt(columnEntry));
				}
				else if(columnName.equals("INCOME"))
				{
					data.setIncome(Integer.parseInt(columnEntry));
				}
				else if(columnName.equals("NEUROTICISM"))
				{
					data.setNeuroticism(Double.parseDouble(columnEntry));
				}
				else if(columnName.equals("RACE"))
				{
					data.setRace(Integer.parseInt(columnEntry));
				}
				else if(columnName.equals("OTHER_RACE_TEXT"))
				{
					data.setOtherRaceText(columnEntry);
				}
			}
		}

		Utilities.close(reader);
	}

	private static void loadResults(final ArrayList<ResultData> p_list,
		final boolean p_isV1)
	{
		final String fileName = p_isV1 ? RESULTS_V1 : RESULTS_V2;
		final String filename = DATA_PATH + fileName;
		final BufferedReader reader = getReader(filename);

		String line = null;
		while(true)
		{
			try
			{
				line = reader.readLine();
			}
			catch(final IOException ex)
			{
				ex.printStackTrace();
				break;
			}

			if(line == null)
			{
				break;
			}

			final String[] bits = line.split(",");

			final ResultData data = new ResultData();
			p_list.add(data);

			for(int i = 0; i < bits.length; i++)
			{
				final String columnName = RESULTS_HEADER[i];
				String columnEntry = bits[i];
				if(columnEntry.startsWith("\""))
				{
					columnEntry = columnEntry.substring(1);
				}

				if(columnEntry.endsWith("\""))
				{
					columnEntry = columnEntry.substring(0,
						columnEntry.length() - 1);
				}

				if(columnName.equals("RESULT_ID"))
				{
					int resultID = Integer.parseInt(columnEntry);
					if(!p_isV1)
					{
						resultID += V2_RESULT_ID_SHIFT;
					}
					data.setResultID(resultID);
				}
				else if(columnName.equals("PARTICIPANT_ID"))
				{
					int participantID = Integer.parseInt(columnEntry);
					if(!p_isV1)
					{
						participantID += V2_PARTICIPANT_ID_SHIFT;
					}
					data.setParticipantID(participantID);
				}
				else if(columnName.equals("TASK_TYPE"))
				{
					data.setGameType(GameType.parseGameType(columnEntry));
				}
				else if(columnName.equals("TRIAL_INDEX"))
				{
					data.setTrialIndex(Integer.parseInt(columnEntry));
				}
				else if(columnName.equals("DIFFICULTY"))
				{
					data.setDifficulty(Integer.parseInt(columnEntry));
				}
				else if(columnName.equals("WAS_SUCCESS"))
				{
					data.setWasSuccess(columnEntry.equals("1"));
				}
				else if(columnName.equals("WAS_PRACTICE"))
				{
					data.setWasPractice(columnEntry.equals("1"));
				}
			}
		}

		Utilities.close(reader);
	}

	public static void main(final String[] p_args)
	{
		getParticipants();
		getResults();
	}
}
