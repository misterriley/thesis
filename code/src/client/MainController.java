/**
 * 
 */
package client;

import games.GameController;
import games.GameModel;
import games.GameRecord;
import games.GameView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import kinesthetic.KinestheticTaskController;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import verbal.VerbalTaskController;
import visual.VisualTaskController;

/**
 * @author Ringo
 * 
 */
public class MainController
{
	private static GameModel		m_model;

	private static final String		QUESTION_1					= "In a given week, how likely are you to play one or more video games?";
	private static final String		QUESTION_2					= "In a given week, how likely are you to play AT LEAST FIVE HOURS of video games?";
	private static final int		LIKERT_THRESHOLD			= 3;

	private static final String[]	NEUROTICISM_QUESTIONS		= {
			"I am someone who is depressed and/or blue",
			"I am someone who is relaxed and/or handles stress well",
			"I am someone who can be tense",
			"I am someone who worries a lot",
			"I am someone who is emotionally stable and/or not easily upset",
			"I am someone who can be moody",
			"I am someone who remains calm in tense situations",
			"I am someone who gets nervous easily"
																	};

	private static final boolean[]	NEUROTICISM_Q_IS_REVERSED	= {false,
																true,
																false,
																false,
																true,
																false,
																true,
																false};

	private static void doGamingHabitsQuiz()
	{
		final LikertScaleQuestion lsq = new LikertScaleQuestion(m_model,
			QUESTION_1, LikertScaleQuestion.ScaleType.LIKELY);
		final int response1 = lsq.getResponse();
		final boolean playsAtAll = (response1 >= LIKERT_THRESHOLD);

		lsq.setPrompt(QUESTION_2);
		final int response2 = lsq.getResponse();
		final boolean playsFiveHours = (response2 >= LIKERT_THRESHOLD);
		m_model.getTaskRecord().setGameUseType(
			GameUseType.getType(playsFiveHours, playsAtAll));
	}

	private static void doGenderQuestion()
	{
		final GenderQuestion gq = new GenderQuestion(m_model);
		gq.waitForSubmit();

		m_model.getTaskRecord().setGenderIndicator(gq.getGenderIndicator());
	}

	private static void doIncomeQuestion()
	{
		final IncomeQuestion iq = new IncomeQuestion(m_model);
		iq.waitForSubmit();

		m_model.getTaskRecord().setIncomeIndicator(iq.getIncomeIndicator());
	}

	private static void doNeuroticismInventory()
	{
		JOptionPane.showMessageDialog(
			m_model.getMainFrame(),
			"For the following "
				+ NEUROTICISM_QUESTIONS.length
				+ " questions, please state how much you agree or disagree with the given statement.");

		final LikertScaleQuestion lsq = new LikertScaleQuestion(m_model, "",
			LikertScaleQuestion.ScaleType.AGREE);

		int runningScore = 0;
		for(int i = 0; i < NEUROTICISM_QUESTIONS.length; i++)
		{
			lsq.setPrompt(NEUROTICISM_QUESTIONS[i] + ".");
			final int response = lsq.getResponse();
			int score = response;
			if(NEUROTICISM_Q_IS_REVERSED[i])
			{
				score = 6 - response;
			}
			runningScore += score;
		}

		final double average = runningScore
			/ ((double)NEUROTICISM_QUESTIONS.length);
		m_model.getTaskRecord().setNeuroticismAvg(average);
	}

	private static void doRaceQuestion()
	{
		final RaceQuestion rq = new RaceQuestion(m_model);
		rq.waitForSubmit();

		m_model.getTaskRecord().setRaceIndicators(rq.getRaceIndicators());
		m_model.getTaskRecord().setOtherRaceText(rq.getOtherRaceText());
	}

	private static void doSetup()
	{
		final JFrame mainFrame = new JFrame(Constants.TITLE);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_model = new GameModel(mainFrame);
	}

	private static void getComments()
	{
		final CommentPanel panel = new CommentPanel(m_model);
		panel.showText(false, true);
		final String comments = panel.getText();
		m_model.getTaskRecord().addComments(comments);
		m_model.getTaskRecord().dumpToConsole();
	}

	private static void getConsent()
	{
		new AgreeSaveDisagreeTextView(m_model, Constants.INFORMED_CONSENT_FILE).showText(
			true, false);
	}

	private static void initializeTask()
	{
		new GameView(m_model).showTask();
	}

	public static void main(final String[] p_args)
	{
		try
		{
			doSetup();
			if(Constants.TEST_TYPE == null)
			{
				runAgeCheck();
				showDescription();
				getConsent();
				doGamingHabitsQuiz();
				doGenderQuestion();
				doRaceQuestion();
				doIncomeQuestion();
				doNeuroticismInventory();
			}
			runTasks();
			showDebriefingStatement();
			getComments();
			submitResults(m_model.getTaskRecord(), m_model.getMainFrame());
		}
		catch(final Exception ex)
		{
			Utilities.showException(ex);
		}

		System.exit(0);
	}

	private static int makePost(final CloseableHttpClient httpclient,
		final HttpPost httppost, final GameRecord p_record)
		throws UnsupportedEncodingException,
		IOException, ClientProtocolException
	{
		int statusCode;
		httppost.setEntity(new UrlEncodedFormEntity(
			p_record.getData(), "UTF-8"));

		final HttpResponse response = httpclient.execute(httppost);
		statusCode = response.getStatusLine().getStatusCode();
		final InputStream is = response.getEntity().getContent();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
			is));
		while(true)
		{
			final String line = reader.readLine();
			if(line == null)
			{
				break;
			}
			System.out.println(line);
		}
		return statusCode;
	}

	private static void runAgeCheck()
	{
		final int age = new AgeCheckController(m_model).doAgeCheck();
		m_model.getTaskRecord().setAge(age);
	}

	private static void runTask()
	{
		GameController controller = null;
		switch(m_model.currentTaskType())
		{
			case VISUAL:
				controller = new VisualTaskController(m_model);
				break;
			case VERBAL:
				controller = new VerbalTaskController(m_model);
				break;
			case KINESTHETIC:
				controller = new KinestheticTaskController(m_model);
				break;
			default:
				throw new RuntimeException();
		}

		m_model.setController(controller);
		controller.runTask();
	}

	public static void runTasks()
	{
		do
		{
			if(Constants.TEST_TYPE == null
				|| Constants.TEST_TYPE == m_model.currentTaskType())
			{
				initializeTask();
				runTask();
			}
		}
		while(m_model.incrementTaskIndex());
	}

	private static void showDebriefingStatement()
	{
		new SaveContinueTextView(m_model, Constants.DEBRIEFING_FILE).showText(
			true, false);
	}

	private static void showDescription()
	{
		new DefaultTextView(m_model, Constants.DESCRIPTION_FILE, "Continue").showText(
			true,
			false);
	}

	private static void showResult(final int statusCode, final JFrame p_frame)
	{
		if(statusCode == Constants.GOOD_STATUS_CODE)
		{
			JOptionPane.showMessageDialog(p_frame,
				"Data successfully transmitted.");
		}
		else
		{
			new DefaultTextView(m_model, Constants.TRANSMISSION_FAIL_FILE,
				"OK").showText(true, false);
		}
	}

	public static void submitResults(final GameRecord p_record,
		final JFrame p_frame)
	{
		final CloseableHttpClient httpclient = HttpClients.createDefault();
		final HttpPost httppost = new HttpPost(Constants.POST_URL
			+ Constants.POST_FILE);

		int statusCode = 0;
		try
		{
			statusCode = makePost(httpclient, httppost, p_record);
		}
		catch(final Exception ex)
		{}
		finally
		{
			showResult(statusCode, p_frame);
		}
	}

	public static void waitForInterrupt()
	{
		try
		{
			Thread.sleep(10000000);
		}
		catch(final InterruptedException ex)
		{}
	}
}