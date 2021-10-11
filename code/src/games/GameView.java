/**
 * 
 */
package games;


import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

import client.Constants;
import client.GameType;
import client.Utilities;

/**
 * @author Ringo
 * 
 */
public class GameView
{
	private final GameModel		m_model;
	private static final int	SLIDER_HEIGHT	= 50;
	private static final int	LABEL_WIDTH		= 130;

	public GameView(final GameModel p_model)
	{
		m_model = p_model;
		final JFrame mainFrame = m_model.getMainFrame();
		mainFrame.setVisible(false);
		mainFrame.repaint();
		final String title = Constants.SHORT_TITLE + " - "
			+ Utilities.getTaskNameString(m_model.currentTaskType()) + " ("
			+ (m_model.currentTaskIndex() + 1) + "/" + GameType.values().length
			+ ")";
		mainFrame.setTitle(title);
		final Container c = mainFrame.getContentPane();
		c.setLayout(null);
		c.removeAll();

		final JSlider difficultySlider = GameComponentFactory.getDifficultySlider(m_model.currentTaskType());
		m_model.setSlider(difficultySlider);

		final GameViewPanel viewPanel = GameComponentFactory.getTaskViewPanel(m_model);
		m_model.setTaskViewPanel(viewPanel);

		final GameControlPanel taskControlPanel = GameComponentFactory.getTaskControlPanel(m_model);
		m_model.setTaskControlPanel(taskControlPanel);

		final JLabel sliderLabel = GameComponentFactory.getLabel(m_model.currentTaskType());

		c.add(viewPanel);
		c.add(difficultySlider);
		c.add(sliderLabel);
		c.add(taskControlPanel);

		final Dimension viewPanelSize = viewPanel.getPreferredSize();
		final Dimension controlPanelSize = taskControlPanel.getPreferredSize();
		final int height = (int)viewPanelSize.getHeight()
			+ SLIDER_HEIGHT + Constants.BORDER_IN_PIXELS + 2
			* Constants.BORDER_IN_PIXELS;

		taskControlPanel.setBounds(Constants.BORDER_IN_PIXELS * 2
			+ (int)viewPanelSize.getWidth(), 0,
			(int)controlPanelSize.getWidth(), (int)controlPanelSize.getHeight());
		viewPanel.setBounds(Constants.BORDER_IN_PIXELS,
			Constants.BORDER_IN_PIXELS,
			(int)viewPanelSize.getWidth(),
			(int)viewPanelSize.getHeight());
		difficultySlider.setBounds(
			2 * Constants.BORDER_IN_PIXELS + LABEL_WIDTH,
			height - SLIDER_HEIGHT - Constants.BORDER_IN_PIXELS,
			(int)viewPanelSize.getWidth()
			- (Constants.BORDER_IN_PIXELS + LABEL_WIDTH),
			SLIDER_HEIGHT);
		sliderLabel.setBounds(Constants.BORDER_IN_PIXELS,
			2 * Constants.BORDER_IN_PIXELS + (int)viewPanelSize.getHeight(),
			LABEL_WIDTH,
			SLIDER_HEIGHT);

		c.setPreferredSize(new Dimension(Constants.BORDER_IN_PIXELS * 3 +
			(int)viewPanelSize.getWidth() + (int)controlPanelSize.getWidth(),
			height));

		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
	}

	public void showTask()
	{
		m_model.getMainFrame().setVisible(true);
	}
}
