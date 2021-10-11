/**
 * 
 */
package kinesthetic;

import games.GameController;
import games.GameKeyListener;
import games.GameModel;
import games.KeyListenerMode;

import java.awt.Point;

import client.Constants;
import client.Direction;
import client.MainController;

/**
 * @author Ringo
 * 
 */
public class KinestheticTaskController extends GameController
{
	private static final int	REPAINT_INTERVAL_MILLIS	= 50;
	private static final int	TICK_INTERVAL			= 5;
	private static final int	AFTER_WIN_WAIT_MILLIS	= 500;

	private long				m_lastTick;
	private boolean				m_restartShown;
	private boolean				m_remindAboutSlider;

	public KinestheticTaskController(final GameModel p_model)
	{
		super(p_model);
		m_model.getTCP().setContinueButtonEnabled(false);
		m_waitBetweenTrials = false;
	}

	private void buildStandardGraph()
	{
		m_model.getKinestheticTVP().generateNewGraph(
			Constants.K_TASK_CELLS_WIDE,
			Constants.K_TASK_CELLS_WIDE / 2);
	}

	private void goUntilUserGetsItRight()
	{
		runAndBuildNextMaze(false);

		for(int i = 0; !m_model.getKinestheticTVP().lastTrialSuccessful(); i++)
		{
			if(m_remindAboutSlider)
			{
				display("If the ball is too big, use the slider to shrink it.");
				m_remindAboutSlider = false;
				m_restartShown = true;
			}
			else if(!m_restartShown)
			{
				display("If you hit a wall, you will restart.");
				m_restartShown = true;
			}
			else
			{
				waitForUserStartTaskClick();
			}
			runAndBuildNextMaze(false);
		}
	}

	private boolean runAndBuildNextMaze(final boolean p_newMazeOnSuccess)
	{
		prepareForTask();

		m_lastTick = System.currentTimeMillis();
		final GameKeyListener keyListener = m_model.getKeyListener();
		keyListener.setMode(KeyListenerMode.TASK);
		m_model.getKinestheticTVP().startTask();

		while(m_model.getKinestheticTVP().isTaskActive())
		{
			try
			{
				Thread.sleep(TICK_INTERVAL);
			}
			catch(final InterruptedException ex)
			{}

			final long newTick = System.currentTimeMillis();
			final double tickSpacing = newTick - m_lastTick;
			m_lastTick = newTick;

			final int delta = (int)Math.round(Constants.K_TASK_BALL_SPEED
				* (tickSpacing / 1000));

			final Ball ball = m_model.getKinestheticTVP().getBall();
			int dx = 0;
			int dy = 0;

			if(keyListener.isDirectionPressed(Direction.UP))
			{
				dy -= delta;
			}
			if(keyListener.isDirectionPressed(Direction.DOWN))
			{
				dy += delta;
			}
			if(keyListener.isDirectionPressed(Direction.LEFT))
			{
				dx -= delta;
			}
			if(keyListener.isDirectionPressed(Direction.RIGHT))
			{
				dx += delta;
			}

			if(ball != null && (dx != 0 || dy != 0))
			{
				final Point p = ball.getPosition();
				ball.setPosition(new Point((int)p.getX() + dx, (int)p.getY()
					+ dy));
			}
		}

		final boolean success = m_model.getKinestheticTVP().lastTrialSuccessful();
		if(success && p_newMazeOnSuccess)
		{
			waitThenBuildGraph();
		}
		return success;
	}

	@Override
	protected boolean runOneTask(final boolean p_lastTask)
	{
		final boolean ret = runAndBuildNextMaze(true);
		if(!ret && !p_lastTask)
		{
			waitForUserStartTaskClick();
		}
		return ret;
	}

	@Override
	protected void runTutorial()
	{
		m_model.getKinestheticTCP().setStartTaskButtonEnabled(false);
		m_model.getKinestheticTVP().generateNewGraph(2, 1);
		m_model.setDifficulty(Constants.K_TASK_DEFAULT_BALL_RADIUS);
		display("This is the kinesthetic task. You will be guided through a short tutorial before the task begins.");
		display("The goal of this task is to guide a ball through a maze without touching the edges.");
		display("Using the up, down, left and right arrow keys, guide the ball into the blue square.");
		goUntilUserGetsItRight();

		display("Here's a slightly more complicated maze.");
		m_model.getKinestheticTVP().generateNewGraph(4, 2);
		goUntilUserGetsItRight();

		display("Well done. Let's make the ball bigger this time.");
		m_model.getKinestheticTVP().generateNewGraph(4, 2);
		m_model.setDifficulty(40);
		goUntilUserGetsItRight();

		m_model.getKinestheticTVP().generateNewGraph(6, 3);
	}

	public void startButtonPushed()
	{
		interruptThreadIfSleeping();
	}

	public void triggerRepaintEdgeCalls()
	{
		final RepaintEdgeTask ret = new RepaintEdgeTask(m_model,
			REPAINT_INTERVAL_MILLIS, Constants.NUM_COLLISION_BLINKS * 2
			* Constants.COLLISION_BLINK_PERIOD_MILLIS);
		new Thread(ret).start();
	}

	private void waitForUserStartTaskClick()
	{
		m_model.getKinestheticTCP().setStartTaskButtonEnabled(true);
		m_model.getKeyListener().setMode(KeyListenerMode.BUTTONS_AND_SLIDER);
		MainController.waitForInterrupt();
		m_model.getKeyListener().setMode(KeyListenerMode.TASK);
		m_model.getKinestheticTCP().setStartTaskButtonEnabled(false);
	}

	private void waitThenBuildGraph()
	{
		try
		{
			Thread.sleep(AFTER_WIN_WAIT_MILLIS);
		}
		catch(final InterruptedException ex)
		{}

		buildStandardGraph();
	}
}
