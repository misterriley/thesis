/**
 * 
 */
package dataprocessing;

import java.util.ArrayList;
import java.util.HashMap;

import client.GameType;

/**
 * @author Ringo
 * 
 */
public class IPPCCalculator
{
	private static final double	minMean		= 0;
	private static final double	maxMean		= 70;
	private static final int	meanSteps	= 400;
	private static final double	minShape	= -20;
	private static final double	maxShape	= 20;
	private static final int	shapeSteps	= 800;

	private static final double	meanDelta	= (maxMean - minMean) / meanSteps;
	private static final double	shapeDelta	= (maxShape - minShape)
												/ shapeSteps;

	public static void calculateIPPCs(
		final ArrayList<ParticipantData> p_participants,
		final ArrayList<ResultData> p_results)
	{
		final HashMap<Integer, HashMap<GameType, ArrayList<ResultData>>> map = sortResults(p_results);

		for(final ParticipantData participant: p_participants)
		{
			final Integer participantID = new Integer(participant.getID());
			final HashMap<GameType, ArrayList<ResultData>> participantDataMap = map.get(participantID);

			for(final GameType gameType: GameType.values())
			{
				final ArrayList<ResultData> resultsList = participantDataMap.get(gameType);
				final double[] parameters = findBestFitParameters(resultsList,
					gameType);
				final double IPPC = successProbability(
					participant.getEndDiff(gameType), parameters[0],
					parameters[1], gameType);
				final double expectedTestScore = successProbability(
					participant.getTestDiff(gameType), parameters[0],
					parameters[1], gameType);
				System.out.println(participantID + " "
					+ getTestPercentage(resultsList) + " "
					+ expectedTestScore + " "
					+ participant.getDeltaDiff(gameType) + " " + gameType + " "
					+ IPPC);
			}
		}
	}

	private static double correctGuessProbability(final GameType p_gameType,
		final int p_difficulty)
	{
		switch(p_gameType)
		{
			case KINESTHETIC:
				return 0;
			case VERBAL:
				return 1.0 / (p_difficulty + 1);
			case VISUAL:
				return 1.0 / 3;
			default:
				throw new RuntimeException();
		}
	}

	private static double[] findBestFitParameters(
		final ArrayList<ResultData> p_results, final GameType p_gameType)
	{
		final HashMap<Integer, int[]> dataMap = new HashMap<Integer, int[]>();
		for(final ResultData result: p_results)
		{
			final Integer difficulty = new Integer(result.getDifficulty());
			int[] counter = dataMap.get(difficulty);
			if(counter == null)
			{
				counter = new int[2];
				dataMap.put(difficulty, counter);
			}

			counter[0]++;
			if(result.wasSuccess())
			{
				counter[1]++;
			}
		}

		double currentMean = minMean;
		double currentBestFit = Double.NEGATIVE_INFINITY;
		final double[] currentBestParameters = new double[2];

		for(int i = 0; i < meanSteps; i++)
		{
			double currentShape = minShape;
			for(int j = 0; j < shapeSteps; j++)
			{
				final double logLikelihood = logLikelihood(dataMap,
					currentMean, currentShape, p_gameType);
				if(logLikelihood > currentBestFit)
				{
					currentBestFit = logLikelihood;
					currentBestParameters[0] = currentMean;
					currentBestParameters[1] = currentShape;
				}
				currentShape += shapeDelta;
			}

			currentMean += meanDelta;
		}

		return currentBestParameters;
	}

	private static double getTestPercentage(
		final ArrayList<ResultData> p_results)
	{
		double testQs = 0;
		double testSuccesses = 0;
		for(final ResultData result: p_results)
		{
			if(!result.wasPractice())
			{
				testQs++;
				if(result.wasSuccess())
				{
					testSuccesses++;
				}
			}
		}

		return testSuccesses / testQs;
	}

	private static double logCombinations(final int p_n, final int p_r)
	{
		double ret = 0;
		for(int i = 1; i <= p_n; i++)
		{
			ret += Math.log(i);
		}

		for(int i = 1; i <= p_r; i++)
		{
			ret -= Math.log(i);
		}

		for(int i = 1; i <= (p_n - p_r); i++)
		{
			ret -= Math.log(i);
		}

		return ret;
	}

	private static double logistic(final double p_x, final double p_mean,
		final double p_shape)
	{
		final double exponential = Math.exp(p_shape * (p_mean - p_x));
		final double denominator = 1 + exponential;
		return 1 / denominator;
	}

	private static double logLikelihood(
		final HashMap<Integer, int[]> p_dataMap,
		final double p_mean, final double p_shape, final GameType p_gameType)
	{
		double ret = 0;
		for(final Integer difficulty: p_dataMap.keySet())
		{
			final int[] counter = p_dataMap.get(difficulty);
			final double logLikelihood = logLikelihood(difficulty.intValue(),
				p_mean, p_shape, counter[0], counter[1], p_gameType);
			ret += logLikelihood;
		}
		return ret;
	}

	private static double logLikelihood(final int p_difficulty,
		final double p_mean,
		final double p_shape, final int p_numTries, final int p_numSuccesses,
		final GameType p_gameType)
	{
		double ret = logCombinations(p_numTries, p_numSuccesses);

		final double succProb = successProbability(p_difficulty, p_mean,
			p_shape,
			p_gameType);// logistic(p_x, p_mean, p_shape);
		final double failProb = 1 - succProb;

		final double logSucc = Math.log(succProb);
		final double logFail = Math.log(failProb);

		ret += p_numSuccesses * logSucc;
		ret += (p_numTries - p_numSuccesses) * logFail;

		return ret;
	}

	public static void main(final String[] p_args)
	{
		final ArrayList<ParticipantData> participants = DataIO.getParticipants();
		final ArrayList<ResultData> results = DataIO.getResults();

		calculateIPPCs(participants, results);
	}

	private static HashMap<Integer, HashMap<GameType, ArrayList<ResultData>>> sortResults(
		final ArrayList<ResultData> p_results)
	{
		final HashMap<Integer, HashMap<GameType, ArrayList<ResultData>>> map = new HashMap<Integer, HashMap<GameType, ArrayList<ResultData>>>();

		for(final ResultData result: p_results)
		{
			final Integer participantID = new Integer(result.getParticipantID());
			HashMap<GameType, ArrayList<ResultData>> participantDataMap = map.get(participantID);
			if(participantDataMap == null)
			{
				participantDataMap = new HashMap<GameType, ArrayList<ResultData>>();
				map.put(participantID, participantDataMap);
			}

			final GameType gameType = result.getGameType();
			ArrayList<ResultData> taskDataList = participantDataMap.get(gameType);
			if(taskDataList == null)
			{
				taskDataList = new ArrayList<ResultData>();
				participantDataMap.put(gameType, taskDataList);
			}

			taskDataList.add(result);
		}

		return map;
	}

	private static double successProbability(final int p_difficulty,
		final double p_mean, final double p_shape,
		final GameType p_gameType)
	{
		final double guessProbability = correctGuessProbability(p_gameType,
			p_difficulty);
		final double guessProbabilityComplement = 1 - guessProbability;

		final double logistic = logistic(p_difficulty, p_mean, p_shape);

		return guessProbability + guessProbabilityComplement * logistic;
	}
}