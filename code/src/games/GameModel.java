/**
 * 
 */
package games;

import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

import kinesthetic.KinestheticTaskControlPanel;
import kinesthetic.KinestheticTaskController;
import kinesthetic.KinestheticTaskViewPanel;
import verbal.VerbalTaskControlPanel;
import verbal.VerbalTaskController;
import verbal.VerbalTaskViewPanel;
import visual.VisualTaskControlPanel;
import visual.VisualTaskController;
import visual.VisualTaskViewPanel;
import client.GameType;
import client.GameUseType;
import client.Randomizer;

/**
 * @author Ringo
 * 
 */
public class GameModel
{
	private final GameType[]	m_taskOrder;

	private int					m_taskIndex	= 0;
	private JSlider				m_slider;
	private GameViewPanel		m_taskViewPanel;
	private GameControlPanel	m_taskControlPanel;
	private GameController		m_controller;
	private final JFrame		m_mainFrame;
	private GameKeyListener		m_keyListener;
	private ButtonFocusManager	m_bfm;

	private final GameRecord	m_results;
	private GameUseType			m_gameUseType;

	public GameModel(final JFrame p_mainFrame)
	{
		m_mainFrame = p_mainFrame;
		m_taskOrder = randomizeTaskOrder();
		m_results = new GameRecord();
	}

	public void addSliderListener(final ChangeListener p_listener)
	{
		m_slider.addChangeListener(p_listener);
	}

	public int currentTaskIndex()
	{
		return m_taskIndex;
	}

	public GameType currentTaskType()
	{
		return m_taskOrder[m_taskIndex];
	}

	public ButtonFocusManager getButtonFocusManager()
	{
		return m_bfm;
	}

	public GameController getController()
	{
		return m_controller;
	}

	public int getCurrentDifficulty()
	{
		return m_slider.getValue();
	}

	public GameUseType getGameUseType()
	{
		return m_gameUseType;
	}

	public GameKeyListener getKeyListener()
	{
		return m_keyListener;
	}

	public KinestheticTaskController getKinestheticController()
	{
		return (KinestheticTaskController)m_controller;
	}

	public KinestheticTaskControlPanel getKinestheticTCP()
	{
		return (KinestheticTaskControlPanel)m_taskControlPanel;
	}

	public KinestheticTaskViewPanel getKinestheticTVP()
	{

		return (KinestheticTaskViewPanel)m_taskViewPanel;
	}

	public JFrame getMainFrame()
	{
		return m_mainFrame;
	}

	/**
	 * @return
	 */
	public int getMaximumDifficulty()
	{
		return m_slider.getMaximum();
	}

	public GameRecord getTaskRecord()
	{
		return m_results;
	}

	public GameControlPanel getTCP()
	{
		return m_taskControlPanel;
	}

	public GameViewPanel getTVP()
	{
		return m_taskViewPanel;
	}

	public VerbalTaskController getVerbalTaskController()
	{
		return (VerbalTaskController)m_controller;
	}

	public VerbalTaskControlPanel getVerbalTCP()
	{
		return (VerbalTaskControlPanel)m_taskControlPanel;
	}

	public VerbalTaskViewPanel getVerbalTVP()
	{
		return (VerbalTaskViewPanel)m_taskViewPanel;
	}

	public VisualTaskController getVisualController()
	{
		return (VisualTaskController)m_controller;
	}

	public VisualTaskControlPanel getVisualTCP()
	{
		return (VisualTaskControlPanel)m_taskControlPanel;
	}

	public VisualTaskViewPanel getVisualTVP()
	{
		return (VisualTaskViewPanel)m_taskViewPanel;
	}

	public boolean incrementTaskIndex()
	{
		m_taskIndex++;
		return (m_taskIndex < m_taskOrder.length);
	}

	public boolean isSliderEnabled()
	{
		return m_slider.isEnabled();
	}

	private GameType[] randomizeTaskOrder()
	{
		final GameType[] ret = Arrays.copyOf(GameType.values(),
			GameType.values().length);
		Arrays.sort(ret, new Randomizer());
		return ret;
	}

	public void setButtonFocusManager(final ButtonFocusManager bfm)
	{
		m_bfm = bfm;
	}

	public void setController(final GameController p_controller)
	{
		m_controller = p_controller;
	}

	public void setDifficulty(final int p_difficulty)
	{
		System.out.println(p_difficulty);
		m_slider.setValue(p_difficulty);
	}

	public void setGameUseType(final GameUseType p_gameUseType)
	{
		m_gameUseType = p_gameUseType;
	}

	/**
	 * @param p_keyListener
	 */
	public void setKeyListener(final GameKeyListener p_keyListener)
	{
		m_keyListener = p_keyListener;
	}

	/**
	 * @param p_newMax
	 */
	public void setMaximumDifficulty(final int p_newMax)
	{
		m_slider.setMaximum(p_newMax);
		m_slider.repaint();
	}

	public void setSlider(final JSlider p_slider)
	{
		m_slider = p_slider;
		m_slider.setEnabled(false);
	}

	public void setSliderEnabled(final boolean p_enabled)
	{
		m_slider.setEnabled(p_enabled);
	}

	public void setTaskControlPanel(final GameControlPanel p_taskControlPanel)
	{
		m_taskControlPanel = p_taskControlPanel;
	}

	public void setTaskViewPanel(final GameViewPanel p_taskViewPanel)
	{
		m_taskViewPanel = p_taskViewPanel;
		for(final ChangeListener l: m_slider.getChangeListeners())
		{
			m_slider.removeChangeListener(l);
		}

		m_slider.addChangeListener(m_taskViewPanel);
	}

	public void setUserType(final GameUseType p_gameUseType)
	{
		m_gameUseType = p_gameUseType;
	}
}