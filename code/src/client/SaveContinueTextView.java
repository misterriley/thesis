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
public class SaveContinueTextView extends TextView
{
	private final JButton	m_continueButton;

	public SaveContinueTextView(final GameModel p_model,
		final String p_toDisplay)
	{
		super(p_model, p_toDisplay);
		m_continueButton = new InterruptThreadButton("Continue",
			Thread.currentThread());
	}

	@Override
	public JPanel getButtonPanel()
	{
		final JPanel ret = new JPanel();
		ret.add(new SaveFileButton(m_model, m_toDisplay));
		ret.add(m_continueButton);
		return ret;
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
