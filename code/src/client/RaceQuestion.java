/**
 * 
 */
package client;

import games.GameModel;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Ringo
 * 
 */
public class RaceQuestion
{
	private final GameModel		m_model;
	private final JCheckBox[]	m_checkBoxes;
	private final JTextField	m_otherRaceTextField;

	private static final String	PROMPT	= "What is your race/ethnicity? (Check all that apply.)";

	public RaceQuestion(final GameModel p_model)
	{
		m_model = p_model;

		final JFrame mainFrame = m_model.getMainFrame();
		mainFrame.setVisible(false);
		mainFrame.setTitle(Constants.TITLE);
		mainFrame.getContentPane().removeAll();
		mainFrame.getContentPane().setLayout(
			new GridLayout(Constants.RACES.length + 3, 1));

		final JLabel promptLabel = new JLabel(PROMPT);
		final JPanel promptPanel = new JPanel();
		promptPanel.add(promptLabel);
		mainFrame.getContentPane().add(promptPanel, 0, 0);

		m_checkBoxes = new JCheckBox[Constants.RACES.length];
		for(int i = 0; i < Constants.RACES.length; i++)
		{
			final JPanel racePanel = new JPanel();
			final JCheckBox raceCheckBox = new JCheckBox();
			m_checkBoxes[i] = raceCheckBox;
			racePanel.add(raceCheckBox);
			racePanel.add(new JLabel(Constants.RACES[i]));
			mainFrame.getContentPane().add(racePanel, 0, i + 1);
		}

		m_otherRaceTextField = new JTextField();
		mainFrame.getContentPane().add(m_otherRaceTextField, 0,
			Constants.RACES.length + 1);

		final InterruptThreadButton submitButton = new InterruptThreadButton(
			"Submit", Thread.currentThread());
		final JPanel buttonPanel = new JPanel();
		buttonPanel.add(submitButton);
		mainFrame.getContentPane().add(buttonPanel, 0,
			Constants.RACES.length + 2);

		mainFrame.getRootPane().setDefaultButton(submitButton);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	public String getOtherRaceText()
	{
		return m_otherRaceTextField.getText();
	}

	public int getRaceIndicators()
	{
		int ret = 1;

		for(int i = 0; i < m_checkBoxes.length; i++)
		{
			if(m_checkBoxes[i].isSelected())
			{
				ret *= Constants.RACE_INDICATORS[i];
			}
		}

		return ret;
	}

	public void waitForSubmit()
	{
		MainController.waitForInterrupt();
	}
}
