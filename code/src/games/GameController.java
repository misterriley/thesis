/**
 * 
 */
package games;

import java.util.LinkedList;
import java.util.Random;

import javax.swing.JOptionPane;

import client.Constants;
import client.GameType;
import client.MainController;
import difficulty.DifficultyMatch;
import difficulty.WinRecord;

/**
 * @author Ringo
 * 
 */
public abstract class GameController
{
	protected final GameModel	m_model;
	protected final Thread		m_controlThread;
	protected boolean			m_waitBetweenTrials	= true;

	private final Random		m_random;

	public GameController(final GameModel p_model)
	{
		m_model = p_model;
		m_controlThread = Thread.currentThread();
		m_random = new Random();
	}

	private double chanceOfCorrectGuess()
	{
		switch(m_model.currentTaskType())
		{
			case VISUAL:
				return 1.0 / 3;
			case VERBAL:
				return 1.0 / (m_model.getCurrentDifficulty() + 1);
			case KINESTHETIC:
				return 0;
			default:
				throw new RuntimeException();
		}
	}

	private boolean confirmDifficultyChoice(final boolean p_taskIsOver)
	{
		String out = null;
		final String taskMeasurementString = GameComponentFactory.getMeasurementString(m_model.currentTaskType());

		if(p_taskIsOver)
		{
			out = "You would prefer the "
				+ taskMeasurementString
				+ " to be "
				+ m_model.getCurrentDifficulty()
				+ " if you did this task again?";
		}
		else
		{
			out = "Set " + taskMeasurementString + " to "
				+ m_model.getCurrentDifficulty()
				+ " for the next " + Constants.NUM_TASKS_PER_TEST_SESSION
				+ " trials?";
		}

		final int response = JOptionPane.showConfirmDialog(
			m_model.getMainFrame(), out, "Confirm difficulty",
			JOptionPane.OK_CANCEL_OPTION);
		return response == JOptionPane.YES_OPTION;
	}

	protected void display(final String p_string)
	{
		JOptionPane.showMessageDialog(m_model.getMainFrame(), p_string);
		m_model.getTVP().requestFocus();
	}

	/**
	 * Does the current task get easier at lower slider values? Yes for
	 * KINESTHETIC and VERBAL, but no for VISUAL.
	 * 
	 * @return
	 */
	private boolean expectLowWins()
	{
		return m_model.currentTaskType() == GameType.KINESTHETIC
			|| m_model.currentTaskType() == GameType.VERBAL;
	}

	private void getEndTaskDiff()
	{
		moveToQuestionMode();
		m_model.getTVP().setDisplayString(Constants.END_TASK_DIFF_QUESTION);
		display("You have finished the trials for this task.");
		display("One last question: if you had to do it again, what difficulty would you choose? Use the slider to enter your answer.");

		waitForUserDiffChoice(true);

		m_model.getTaskRecord().setAfterTaskDiffSelection(
			m_model.getCurrentDifficulty(), m_model.currentTaskType());

		display("Thanks.  You have finished this task.");
	}

	private void getMidTaskDiff()
	{
		moveToQuestionMode();
		m_model.getTVP().setDisplayString(Constants.MID_TASK_DIFF_QUESTION);
		display("Now that you've had some practice, please select the difficulty you think will be the most enjoyable. There will be "
			+ Constants.NUM_TASKS_PER_TEST_SESSION + " trials at this level.");

		waitForUserDiffChoice(false);

		m_model.getTaskRecord().setMidTaskDiffSelection(
			m_model.getCurrentDifficulty(), m_model.currentTaskType());

		m_model.getTVP().setDisplayString(null);
		m_model.getTCP().setContinueButtonEnabled(false);
		m_model.setSliderEnabled(false);
	}

	protected Random getRandom()
	{
		return m_random;
	}

	protected void interruptThreadIfSleeping()
	{
		if(m_controlThread.getState() == Thread.State.TIMED_WAITING)
		{
			m_controlThread.interrupt();
		}
	}

	private void moveToQuestionMode()
	{
		m_model.getTVP().clearFeedback();
		m_model.getTVP().getKeyListener().setMode(
			KeyListenerMode.BUTTONS_AND_SLIDER);
		m_model.getTCP().enableOnlyContinueButtons();
		m_model.setSliderEnabled(true);
	}

	protected void prepareForTask()
	{
		m_model.getTVP().clearFeedback();
		m_model.getTVP().requestFocus();
	}

	protected abstract boolean runOneTask(boolean p_isFinalTask);

	private final void runPracticeSet()
	{
		runSession(true);
	}

	private void runSession(final boolean isPractice)
	{
		m_model.getTVP().enableNeutralDisplay();

		DifficultyMatch dm = null;
		LinkedList<WinRecord> wrList = null;
		if(isPractice)
		{
			dm = new DifficultyMatch(m_model.getMaximumDifficulty() / 2,
				Constants.PRACTICE_TARGET_WIN_PCT,
				expectLowWins(),
				m_model.getMaximumDifficulty());
			wrList = new LinkedList<WinRecord>();
		}

		final int numTasks = isPractice ? Constants.NUM_TASKS_PER_AUTO_SESSION : Constants.NUM_TASKS_PER_TEST_SESSION;
		for(int i = 0; i < numTasks; i++)
		{
			if(i != 0 && m_waitBetweenTrials)
			{
				MainController.waitForInterrupt();
			}

			if(isPractice)
			{
				int diff = (int)Math.round(dm.getNewDifficulty(wrList, true));
				if(diff < 1)
				{
					// never auto-set difficulty to zero
					diff = 1;
				}
				m_model.setDifficulty(diff);
			}

			final boolean lastTask = (i + 1 == numTasks);
			final boolean success = runOneTask(lastTask);
			m_model.getTaskRecord().addResult(
				m_model.getCurrentDifficulty(), success, i,
				m_model.currentTaskType(), isPractice);

			if(isPractice)
			{
				final WinRecord wr = new WinRecord(
					m_model.getCurrentDifficulty(),
					success,
					chanceOfCorrectGuess());
				wrList.add(wr);
			}
		}

		if(!isPractice)
		{
			m_model.getTCP().setContinueButtonEnabled(true);
			MainController.waitForInterrupt();
			m_model.getTCP().setContinueButtonEnabled(false);
		}
	}

	public void runTask()
	{
		if(Constants.RUN_TUTORIAL)
		{
			runTutorial();
			showTutorialEnd();
		}

		runPracticeSet();
		getMidTaskDiff();
		runTestSet();
		getEndTaskDiff();
	}

	private final void runTestSet()
	{
		runSession(false);
	}

	protected abstract void runTutorial();

	private void showTutorialEnd()
	{
		display("We will attempt to hone in on an appropriate difficulty level for you.  After "
			+ Constants.NUM_TASKS_PER_AUTO_SESSION
			+ " trials, you'll be able to set your own difficulty level.");
		display("Look at the slider once in awhile to get an idea for your preferred difficulty level.");
	}

	private void waitForUserDiffChoice(final boolean taskOver)
	{
		do
		{
			MainController.waitForInterrupt();
		}
		while(!confirmDifficultyChoice(taskOver));
	}
}
