/**
 * 
 */
package verbal;

import games.GameControlPanel;
import games.GameModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

/**
 * @author Ringo
 * 
 */
public class VerbalTaskControlPanel extends GameControlPanel
	implements ActionListener
{
	private static final long	serialVersionUID	= 1L;

	private static final int	BUTTON_COUNT		= 10;
	private static final int	BUTTON_COLUMNS		= 2;
	private static final int	BUTTON_ROWS			= BUTTON_COUNT
														/ BUTTON_COLUMNS;

	private int					m_lastResponse		= -1;
	private final JButton[]		m_numButtons;

	public VerbalTaskControlPanel(final GameModel p_model)
	{
		super(p_model);

		m_numButtons = new JButton[BUTTON_COUNT];
		for(int rowIndex = 0; rowIndex < BUTTON_ROWS; rowIndex++)
		{
			final ArrayList<JButton> row = new ArrayList<JButton>();
			for(int columnIndex = 0; columnIndex < BUTTON_COLUMNS; columnIndex++)
			{
				final int index = rowIndex * BUTTON_COLUMNS + columnIndex;
				final String title = Integer.toString(index);
				final JButton numButton = new JButton(title);
				numButton.setName(title);
				row.add(numButton);
				m_numButtons[index] = numButton;
			}
			addButtonRow(row);
		}

		addButton(getContinueButton());
	}

	public void enableButtonsUpTo(final int p_maxEnabled)
	{
		for(int i = 0; i < m_numButtons.length; i++)
		{
			final boolean enabled = i <= p_maxEnabled;
			setButtonEnabled(m_numButtons[i], enabled);
		}

		setContinueButtonEnabled(false);
		m_model.getButtonFocusManager().setFocus(0, 0);
	}

	public int getLastResponse()
	{
		return m_lastResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.ControlPanel#thisActionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void thisActionPerformed(final ActionEvent p_arg0)
	{
		final JButton source = (JButton)p_arg0.getSource();
		final String name = source.getName();

		if(name != null)
		{
			m_lastResponse = Integer.parseInt(name);
			interruptControlThread();
		}
	}
}