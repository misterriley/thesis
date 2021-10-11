/**
 * 
 */
package client;

import games.GameModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author Ringo
 * 
 */
public class AgreeSaveDisagreeTextView extends TextView
	implements ActionListener
{
	private JButton	m_declineButton;

	public AgreeSaveDisagreeTextView(final GameModel p_model,
		final String p_toDisplay)
	{
		super(p_model, p_toDisplay);
	}

	public void actionPerformed(final ActionEvent p_arg0)
	{
		final JButton source = (JButton)p_arg0.getSource();
		if(source == m_declineButton)
		{
			JOptionPane.showMessageDialog(m_model.getMainFrame(),
				"Sorry, but you must give consent to participate.");
			System.exit(0);
		}
	}

	@Override
	public JPanel getButtonPanel()
	{
		final JPanel ret = new JPanel();
		ret.add(new InterruptThreadButton("I Agree",
			Thread.currentThread()));
		ret.add(new SaveFileButton(m_model, m_toDisplay));
		ret.add(m_declineButton = new JButton("I Do Not Agree"));

		m_declineButton.addActionListener(this);

		return ret;
	}

	@Override
	public JButton getDefaultButton()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.TextView#getNorthPanel()
	 */
	@Override
	public JPanel getNorthPanel()
	{
		return null;
	}
}
