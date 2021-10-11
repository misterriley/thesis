/**
 * 
 */
package client;

import games.GameModel;

import java.awt.BorderLayout;
import java.text.DateFormatSymbols;
import java.util.Calendar;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Ringo
 * 
 */
public class DatePicker
{
	private final GameModel	m_model;
	private final String	m_prompt;
	private final JComboBox	m_monthBox;
	private final JComboBox	m_dayBox;
	private final JComboBox	m_yearBox;

	public DatePicker(final GameModel p_model, final String p_prompt)
	{
		m_model = p_model;
		m_prompt = p_prompt;

		final JFrame mainFrame = m_model.getMainFrame();
		mainFrame.setTitle(Constants.TITLE);
		mainFrame.getContentPane().removeAll();
		mainFrame.getContentPane().setLayout(new BorderLayout());

		final JPanel comboPanel = new JPanel();
		m_monthBox = new JComboBox(
			new DateFormatSymbols().getMonths());
		m_dayBox = getIntComboBox(1, 31);
		m_yearBox = getIntComboBox(1900, 2014);
		m_yearBox.setSelectedItem("1980");
		final InterruptThreadButton submitButton = new InterruptThreadButton(
			"Submit", Thread.currentThread());

		comboPanel.add(m_monthBox);
		comboPanel.add(m_dayBox);
		comboPanel.add(m_yearBox);
		comboPanel.add(submitButton);

		final JLabel promptLabel = new JLabel(m_prompt);
		final JPanel promptPanel = new JPanel();
		promptPanel.add(promptLabel);
		mainFrame.getContentPane().add(promptPanel, BorderLayout.NORTH);
		mainFrame.getContentPane().add(comboPanel, BorderLayout.SOUTH);

		mainFrame.getRootPane().setDefaultButton(submitButton);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	private JComboBox getIntComboBox(final int p_lowValue,
		final int p_highValue)
	{
		final JComboBox ret = new JComboBox();
		for(int i = p_lowValue; i <= p_highValue; i++)
		{
			ret.addItem(Integer.toString(i));
		}
		return ret;
	}

	public Calendar pickDate()
	{
		MainController.waitForInterrupt();

		final Calendar date = Calendar.getInstance();
		date.set(Calendar.MONTH, m_monthBox.getSelectedIndex() + 1);
		date.set(Calendar.DATE,
			Integer.parseInt((String)m_dayBox.getSelectedItem()));
		date.set(Calendar.YEAR,
			Integer.parseInt((String)m_yearBox.getSelectedItem()));

		return date;
	}
}
