/**
 * 
 */
package visual;

import games.GameControlPanel;
import games.GameModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * @author Ringo
 * 
 */
public class VisualTaskControlPanel extends GameControlPanel
	implements ActionListener
{
	static final long					serialVersionUID	= 4872964399055952460L;

	private final JButton				m_leftBigger;
	private final JButton				m_sameSize;
	private final JButton				m_rightBigger;

	private TriangleComparisonChoice	m_lastChoice		= null;

	public VisualTaskControlPanel(final GameModel p_model)
	{
		super(p_model);

		addButton(m_leftBigger = new JButton("Left Is Bigger"));
		addButton(m_sameSize = new JButton("Same Size"));
		addButton(m_rightBigger = new JButton("Right Is Bigger"));
		addButton(getContinueButton());
	}

	public TriangleComparisonChoice getLastChoice()
	{
		return m_lastChoice;
	}

	public void setIsActive(final boolean p_isEnabled)
	{
		if(!p_isEnabled)
		{
			enableOnlyContinueButtons();
			m_model.getButtonFocusManager().setFocus(3, 0);
		}
		else
		{
			setContinueButtonEnabled(!p_isEnabled);
			setButtonEnabled(m_leftBigger, p_isEnabled);
			setButtonEnabled(m_rightBigger, p_isEnabled);
			setButtonEnabled(m_sameSize, p_isEnabled);
			m_model.getButtonFocusManager().setFocus(0, 0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.ControlPanel#thisActionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void thisActionPerformed(final ActionEvent p_arg0)
	{
		if(p_arg0.getSource() == m_leftBigger)
		{
			m_lastChoice = TriangleComparisonChoice.LEFT_BIGGER;
		}
		else if(p_arg0.getSource() == m_sameSize)
		{
			m_lastChoice = TriangleComparisonChoice.SAME_SIZE;
		}
		else if(p_arg0.getSource() == m_rightBigger)
		{
			m_lastChoice = TriangleComparisonChoice.RIGHT_BIGGER;
		}

		interruptControlThread();
	}
}
