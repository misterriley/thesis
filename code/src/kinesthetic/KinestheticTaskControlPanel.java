/**
 * 
 */
package kinesthetic;

import games.GameControlPanel;
import games.GameModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * @author Ringo
 * 
 */
public class KinestheticTaskControlPanel extends GameControlPanel
	implements ActionListener
{
	private static final long	serialVersionUID	= -2737219373392021044L;
	private final JButton		m_startTaskButton;

	public KinestheticTaskControlPanel(final GameModel p_model)
	{
		super(p_model);

		addButton(m_startTaskButton = new JButton("Start Task"));
		addButton(getContinueButton());
	}

	public boolean isStartTaskButtonEnabled()
	{
		return m_startTaskButton.isEnabled();
	}

	public void setStartTaskButtonEnabled(final boolean p_enabled)
	{
		setButtonEnabled(m_startTaskButton, p_enabled);
		if(p_enabled)
		{
			m_model.getButtonFocusManager().setFocus(0, 0);
		}
	}

	@Override
	public void thisActionPerformed(final ActionEvent p_arg0)
	{
		if(p_arg0.getSource() == m_startTaskButton)
		{
			m_model.getKinestheticController().startButtonPushed();
		}
	}
}
