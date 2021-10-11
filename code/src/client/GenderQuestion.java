/**
 * 
 */
package client;

import games.GameModel;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Ringo
 * 
 */
public class GenderQuestion
{
	private final GameModel		m_model;
	private final JComboBox		m_genderSelectionBox;

	private static final String	PROMPT	= "What is your gender?";

	public GenderQuestion(final GameModel p_model)
	{
		m_model = p_model;

		final JFrame mainFrame = m_model.getMainFrame();
		mainFrame.setVisible(false);
		mainFrame.setTitle(Constants.TITLE);
		mainFrame.getContentPane().removeAll();
		mainFrame.getContentPane().setLayout(
			new BorderLayout());

		final JLabel promptLabel = new JLabel(PROMPT);
		final JPanel promptPanel = new JPanel();
		promptPanel.add(promptLabel);
		mainFrame.getContentPane().add(promptPanel, BorderLayout.NORTH);

		m_genderSelectionBox = new JComboBox();
		for(final String element: Constants.GENDERS)
		{
			m_genderSelectionBox.addItem(element);
		}
		final JPanel comboBoxPanel = new JPanel();
		comboBoxPanel.add(m_genderSelectionBox);
		mainFrame.getContentPane().add(comboBoxPanel, BorderLayout.CENTER);

		final InterruptThreadButton submitButton = new InterruptThreadButton(
			"Submit", Thread.currentThread());
		final JPanel buttonPanel = new JPanel();
		buttonPanel.add(submitButton);
		mainFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		mainFrame.getRootPane().setDefaultButton(submitButton);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	public int getGenderIndicator()
	{
		final int selection = m_genderSelectionBox.getSelectedIndex();
		return Constants.GENDER_INDICATORS[selection];
	}

	public void waitForSubmit()
	{
		MainController.waitForInterrupt();
	}
}
