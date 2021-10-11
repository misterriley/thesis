/**
 * 
 */
package visual;

import games.GameController;
import games.GameModel;
import games.KeyListenerMode;
import client.MainController;

/**
 * @author Ringo
 * 
 */
public class VisualTaskController extends GameController
{
	private TriangleComparisonChoice	m_lastChoice;

	public VisualTaskController(final GameModel p_model)
	{
		super(p_model);
		m_model.getKeyListener().setMode(KeyListenerMode.BUTTONS_AND_SLIDER);
	}

	private boolean calculateSuccess()
	{
		final TriangleComparisonChoice userChoice = m_model.getVisualTCP().getLastChoice();

		m_model.getVisualTVP().setTaskMode(VisualTaskMode.DRAW_FULL_SOLUTION);
		m_model.getVisualTCP().setIsActive(false);

		final boolean success = (m_lastChoice == userChoice);
		m_model.getTVP().showFeedback(success);
		return success;
	}

	private void letUserSeeFeedback()
	{
		m_model.getVisualTCP().setIsActive(false);
		m_model.getVisualTVP().setTaskMode(VisualTaskMode.DRAW_FULL_SOLUTION);
	}

	private void putUserToTest()
	{
		MainController.waitForInterrupt();
	}

	@Override
	protected boolean runOneTask(final boolean p_lastTask)
	{
		waitForUserToAcceptTriangles();
		final boolean ret = calculateSuccess();
		letUserSeeFeedback();

		return ret;
	}

	@Override
	protected void runTutorial()
	{
		display("This is the visual task.  You will be guided through a short tutorial before the task begins.");
		display("The goal of this task is to compare sizes of circles which fit around triangles.");

		setPredeterminedTriangles(50, 100,
			VisualTaskMode.DRAW_TRIANGLES_AND_CIRCLES);
		display("Here is an easy example.  One of these circles is bigger than the other. Select the correct answer using the panel on the right.");
		putUserToTest();
		calculateSuccess();
		letUserSeeFeedback();
		MainController.waitForInterrupt();

		setPredeterminedTriangles(105, 115,
			VisualTaskMode.DRAW_TRIANGLES_AND_CIRCLES);
		display("Here is a less obvious example.  These circles have a smaller radius gap.  Select an answer on the right panel.");
		putUserToTest();
		calculateSuccess();
		letUserSeeFeedback();
		MainController.waitForInterrupt();

		setPredeterminedTriangles(110, 60,
			VisualTaskMode.DRAW_TRIANGLES);
		display("Now let's try it without the circles drawn for you.  Try to picture the circles which fit around the triangles, then select the correct answer.");
		putUserToTest();
		calculateSuccess();
		letUserSeeFeedback();
		MainController.waitForInterrupt();

		setPredeterminedTriangles(80, 80,
			VisualTaskMode.DRAW_TRIANGLES);
		display("Sometimes the circles will be the same size.");
		putUserToTest();
		calculateSuccess();
	}

	private void setPredeterminedTriangles(final int p_radius1,
		final int p_radius2, final VisualTaskMode p_vtm)
	{
		final VisualTaskTriangle vtt1 = TriangleFactory.createTriangle(p_radius1);
		final VisualTaskTriangle vtt2 = TriangleFactory.createTriangle(p_radius2);
		m_model.getVisualTVP().setTriangle1(vtt1);
		m_model.getVisualTVP().setTriangle2(vtt2);
		m_model.getVisualTVP().setTaskMode(p_vtm);

		if(p_radius1 > p_radius2)
		{
			m_lastChoice = TriangleComparisonChoice.LEFT_BIGGER;
		}
		else if(p_radius1 == p_radius2)
		{
			m_lastChoice = TriangleComparisonChoice.SAME_SIZE;
		}
		else
		{
			m_lastChoice = TriangleComparisonChoice.RIGHT_BIGGER;
		}

		if(p_radius1 != p_radius2)
		{
			m_model.setDifficulty(Math.abs(p_radius1 - p_radius2));
		}

		m_model.getVisualTVP().clearFeedback();
		m_model.getVisualTCP().setIsActive(true);
	}

	void setRandomTriangles()
	{
		// make sure there's at least a one pixel difference
		int radiusDifference = m_model.getCurrentDifficulty();
		if(radiusDifference == 0)
		{
			radiusDifference = 1;
		}

		m_lastChoice = TriangleComparisonChoice.values()[getRandom().nextInt(3)];
		if(m_lastChoice == TriangleComparisonChoice.SAME_SIZE)
		{
			radiusDifference = 0;
		}

		final int maxRadiusVariance = VisualTaskViewPanel.MAX_TRIANGLE_RADIUS
			- VisualTaskViewPanel.MIN_TRIANGLE_RADIUS - radiusDifference;
		final int radiusVariance = getRandom().nextInt(maxRadiusVariance);
		final int smallRadius = VisualTaskViewPanel.MIN_TRIANGLE_RADIUS
			+ radiusVariance;
		final int largeRadius = smallRadius + radiusDifference;

		final VisualTaskTriangle smallTriangle = TriangleFactory.createTriangle(smallRadius);
		final VisualTaskTriangle largeTriangle = TriangleFactory.createTriangle(largeRadius);

		if(m_lastChoice == TriangleComparisonChoice.LEFT_BIGGER)
		{
			m_model.getVisualTVP().setTriangle1(largeTriangle);
			m_model.getVisualTVP().setTriangle2(smallTriangle);
		}
		else
		{
			m_model.getVisualTVP().setTriangle1(smallTriangle);
			m_model.getVisualTVP().setTriangle2(largeTriangle);
		}

		m_model.getVisualTVP().clearFeedback();
		m_model.getVisualTVP().setTaskMode(VisualTaskMode.DRAW_TRIANGLES);
		m_model.getVisualTCP().setIsActive(true);
	}

	private void waitForUserToAcceptTriangles()
	{
		setRandomTriangles();
		putUserToTest();
	}
}