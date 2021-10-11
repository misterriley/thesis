/**
 * 
 */
package client;

import games.GameModel;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author Ringo
 * 
 */
public class DefaultTextView extends TextView
{
	private final JButton	m_continueButton;

	public DefaultTextView(final GameModel p_model,
		final String p_fileToDisplay,
		final String p_continueButtonTitle)
	{
		super(p_model, p_fileToDisplay);
		m_continueButton = new InterruptThreadButton(p_continueButtonTitle,
			Thread.currentThread());
	}

	@Override
	public JPanel getButtonPanel()
	{
		final JPanel continuePanel = new JPanel();

		continuePanel.add(m_continueButton);
		return continuePanel;
	}

	@Override
	public JButton getDefaultButton()
	{
		return m_continueButton;
	}

	@Override
	public JPanel getNorthPanel()
	{
		return null;
	}
}
