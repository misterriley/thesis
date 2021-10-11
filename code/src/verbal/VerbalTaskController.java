/**
 * 
 */
package verbal;

import games.GameController;
import games.GameModel;
import games.KeyListenerMode;

import javax.swing.Timer;

import client.MainController;
import client.StringEnumeration;

/**
 * @author Ringo
 * 
 */
public class VerbalTaskController extends GameController
{
	private static final int			NUM_SECONDS_PER_TRIAL	= 4;

	private Timer						m_timer;
	private int							m_timerCount;
	private final VerbalTaskWordList	m_wordList;
	private boolean						m_sliderChanged;

	/**
	 * @param p_model
	 */
	public VerbalTaskController(final GameModel p_model)
	{
		super(p_model);

		m_wordList = new VerbalTaskWordList().load();
		m_wordList.shuffle();
		m_waitBetweenTrials = false;
	}

	private boolean calculateSuccess()
	{
		m_model.getVerbalTVP().setTaskMode(VerbalTaskMode.DISPLAY_ANSWERS);
		m_model.getVerbalTVP().clearDisplayString();

		final int response = m_model.getVerbalTCP().getLastResponse();
		final boolean success = (response == m_model.getVerbalTVP().getDisplayList().numRhymes());
		m_model.getTVP().showFeedback(success);
		MainController.waitForInterrupt();

		return success;
	}

	VerbalTaskDisplayList constructDisplayList(
		final VerbalTaskWordListEntry p_entry, final int p_listSize)
	{
		final VerbalTaskDisplayList ret = new VerbalTaskDisplayList(
			p_entry.getTargetWord());
		final int numRhymes = p_entry.numRhymes();
		final int numNonRhymes = p_entry.numNonRhymes();

		final int minRhymes = Math.max(p_listSize - numNonRhymes, 0);
		final int maxRhymes = Math.min(numRhymes, p_listSize);

		final int randomNumRhymes = minRhymes
			+ getRandom().nextInt(maxRhymes - minRhymes + 1);

		final StringEnumeration[] enums = new StringEnumeration[4];

		enums[0] = new StringEnumeration(p_entry.getRhymingLookalikes());
		enums[1] = new StringEnumeration(
			p_entry.getRhymingNonLookalikes());
		enums[2] = new StringEnumeration(p_entry.getEyeRhymes());
		enums[3] = new StringEnumeration(p_entry.getNoneOfTheAbove());

		while(ret.size() < randomNumRhymes)
		{
			final StringEnumeration nextEnum = getRandom().nextBoolean() ? enums[0] : enums[1];
			if(nextEnum.hasMoreElements())
			{
				ret.addToList(nextEnum.nextElement(), true);
			}
		}

		while(ret.size() < p_listSize)
		{
			final StringEnumeration nextEnum = getRandom().nextBoolean() ? enums[2] : enums[3];
			if(nextEnum.hasMoreElements())
			{
				ret.addToList(nextEnum.nextElement(), false);
			}
		}

		ret.shuffle();
		return ret;
	}

	private void putUserToTest(final boolean p_runTimer)
	{
		if(p_runTimer)
		{
			m_model.getVerbalTVP().setDisplayTimer(true);
			m_timerCount = NUM_SECONDS_PER_TRIAL;
			m_model.getVerbalTVP().setSecondsLeft(m_timerCount);
			startNewTimer();
		}

		MainController.waitForInterrupt();
		stopTimer();
		m_model.getKeyListener().setMode(KeyListenerMode.BUTTONS_AND_SLIDER);
		m_model.getTCP().enableOnlyContinueButtons();
	}

	@Override
	protected boolean runOneTask(final boolean p_lastTask)
	{
		do
		{
			m_sliderChanged = false;
			m_model.getVerbalTCP().setContinueButtonEnabled(true);
			m_model.getVerbalTVP().setDisplayTimer(false);

			setupUIForTask();

			putUserToTest(true);

			if(m_sliderChanged)
			{
				m_model.getVerbalTVP().setDisplayList(null);
				m_model.getVerbalTVP().setDisplayTimer(false);
			}
		}
		while(m_sliderChanged);

		return calculateSuccess();
	}

	@Override
	protected void runTutorial()
	{
		display("This is the verbal task.  You will be guided through a short tutorial before the task begins.");
		display("The goal of this task is to count the number of words in a list which rhyme with a target word.");

		setupUIForTask();
		final String targetWord = m_model.getVerbalTVP().getDisplayList().getTargetWord();
		display("Here is an example.  Your target word is \""
			+ targetWord
			+ "\".  Count the words in the list which rhyme with \""
			+ targetWord
			+ "\", then press the correct answer on the right.");
		putUserToTest(false);
		calculateSuccess();
		display("Let's try it with a different target word and a larger list.");

		m_model.setDifficulty(8);
		setupUIForTask();
		putUserToTest(false);
		calculateSuccess();

		display("In order to increase the difficulty, you'll only have "
			+ NUM_SECONDS_PER_TRIAL
			+ " seconds per trial.  When the timer reaches 0, the word list will disappear.");
		m_model.setDifficulty(3);
		setupUIForTask();
		putUserToTest(true);
		calculateSuccess();
	}

	private void setupUIForTask()
	{
		prepareForTask();
		m_model.getKeyListener().setMode(KeyListenerMode.BUTTON_CYCLING);
		final int numEntries = m_model.getCurrentDifficulty();

		m_model.getVerbalTVP().setDisplayList(
			constructDisplayList(m_wordList.nextElement(), numEntries));
		m_model.getVerbalTVP().setTaskMode(VerbalTaskMode.DISPLAY_WORDS);
		m_model.getVerbalTCP().enableButtonsUpTo(
			m_model.getVerbalTVP().getDisplayList().size());
	}

	private void startNewTimer()
	{
		m_timer = new Timer(1000, new Countdown(m_model, NUM_SECONDS_PER_TRIAL));
		m_timer.start();
	}

	public void stopTimer()
	{
		if(m_timer != null)
		{
			m_timer.stop();
			m_timer = null;
		}
	}
}