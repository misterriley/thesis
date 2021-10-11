/**
 * 
 */
package games;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import kinesthetic.KinestheticTaskControlPanel;
import kinesthetic.KinestheticTaskViewPanel;
import verbal.VerbalTaskControlPanel;
import verbal.VerbalTaskViewPanel;
import visual.VisualTaskControlPanel;
import visual.VisualTaskViewPanel;
import client.Constants;
import client.GameType;

/**
 * @author Ringo
 * 
 */
public class GameComponentFactory
{
	private static final int	V_SLIDER_MAX	= 50;

	public static JSlider getDifficultySlider(final GameType p_type)
	{
		JSlider ret = null;
		switch(p_type)
		{
			case VISUAL:
				ret = new JSlider(SwingConstants.HORIZONTAL, 0, V_SLIDER_MAX,
					(int)(V_SLIDER_MAX * .8));
				ret.setMajorTickSpacing(10);
				ret.setMinorTickSpacing(1);
				break;
			case KINESTHETIC:
				final int maxRadius = Constants.AREA_WIDTH
					/ (2 * Constants.K_TASK_CELLS_WIDE);
				ret = new JSlider(SwingConstants.HORIZONTAL, 0, maxRadius, 5);
				ret.setMajorTickSpacing(10);
				ret.setMinorTickSpacing(1);
				break;
			case VERBAL:
				ret = new JSlider(SwingConstants.HORIZONTAL, 0, 10, 3);
				ret.setMajorTickSpacing(1);
				break;
		}

		ret.setPaintTicks(true);
		ret.setPaintLabels(true);
		ret.setSnapToTicks(true);
		return ret;
	}

	public static JLabel getLabel(final GameType p_type)
	{
		final String display = getMeasurementString(p_type) + ":";
		return new JLabel(display, SwingConstants.CENTER);
	}

	public static String getMeasurementString(final GameType p_type)
	{
		String display = null;
		switch(p_type)
		{
			case VISUAL:
				display = "radius gap (in pixels)";
				break;
			case VERBAL:
				display = "number of words";
				break;
			case KINESTHETIC:
				display = "ball radius (in pixels)";
				break;
		}
		return display;
	}

	public static GameControlPanel getTaskControlPanel(final GameModel p_model)
	{
		switch(p_model.currentTaskType())
		{
			case VISUAL:
				return new VisualTaskControlPanel(p_model);
			case KINESTHETIC:
				return new KinestheticTaskControlPanel(p_model);
			case VERBAL:
				return new VerbalTaskControlPanel(p_model);
			default:
				throw new RuntimeException();
		}
	}

	public static GameViewPanel getTaskViewPanel(final GameModel p_model)
	{
		switch(p_model.currentTaskType())
		{
			case KINESTHETIC:
				return new KinestheticTaskViewPanel(p_model);
			case VERBAL:
				return new VerbalTaskViewPanel(p_model);
			case VISUAL:
				return new VisualTaskViewPanel(p_model);
			default:
				throw new RuntimeException();
		}
	}
}
