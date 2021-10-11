/**
 * 
 */
package games;

import java.util.ArrayList;

import javax.swing.JButton;

import client.Direction;

/**
 * @author Ringo
 * 
 */
public class ButtonFocusManager
{
	private final ArrayList<ArrayList<JButton>>	m_buttons;
	private final GameModel						m_model;
	private int									m_currentRow;
	private int									m_currentColumn;
	private JButton								m_lastSelected	= null;

	public ButtonFocusManager(final GameModel p_model)
	{
		m_model = p_model;
		m_buttons = new ArrayList<ArrayList<JButton>>();
		m_currentRow = 0;
		m_currentColumn = 0;
	}

	public void add(final JButton p_button)
	{
		putButton(p_button, m_buttons.size(), 0);
	}

	public JButton currentButton()
	{
		final ArrayList<JButton> array = m_buttons.get(m_currentRow);
		if(m_currentColumn < array.size())
		{
			return array.get(m_currentColumn);
		}

		return array.get(array.size() - 1);
	}

	public void cycle(final Direction p_dir)
	{
		final int startRow = m_currentRow;
		final int startColumn = m_currentColumn;
		do
		{
			switch(p_dir)
			{
				case DOWN:
					m_currentRow = (m_currentRow + 1) % m_buttons.size();
					break;
				case LEFT:
					m_currentColumn = (m_currentColumn - 1 + getWidth(m_currentRow))
						% getWidth(m_currentRow);
					break;
				case RIGHT:
					m_currentColumn = (m_currentColumn + 1)
						% getWidth(m_currentRow);
					break;
				case UP:
					m_currentRow = (m_currentRow + m_buttons.size() - 1)
						% m_buttons.size();
					break;
			}

			if(m_currentRow == startRow && m_currentColumn == startColumn)
			{
				break;
			}
		}
		while(!currentButton().isEnabled());

		setDefaultButton();
	}

	private void ensureCapacity(final ArrayList<JButton> p_array,
		final int p_minSize)
	{
		while(p_array.size() < p_minSize)
		{
			p_array.add(null);
		}
	}

	private void ensureListCapacity(
		final ArrayList<ArrayList<JButton>> p_array,
		final int p_minSize)
	{
		while(p_array.size() < p_minSize)
		{
			p_array.add(new ArrayList<JButton>());
		}
	}

	private int getWidth(final int p_row)
	{
		return m_buttons.get(p_row).size();
	}

	private void incrementIndices()
	{
		m_currentColumn++;
		if(m_currentColumn >= getWidth(m_currentRow))
		{
			m_currentColumn = 0;
			m_currentRow++;

			if(m_currentRow >= m_buttons.size())
			{
				m_currentRow = 0;
			}
		}
	}

	public void putButton(final JButton p_button, final int p_rowIndex,
		final int p_columnIndex)
	{
		ensureListCapacity(m_buttons, p_rowIndex + 1);

		ArrayList<JButton> row = m_buttons.get(p_rowIndex);

		if(row == null)
		{
			row = new ArrayList<JButton>();
			m_buttons.set(p_rowIndex, row);
		}

		ensureCapacity(row, p_columnIndex + 1);

		row.set(p_columnIndex, p_button);
	}

	private void setDefaultButton()
	{
		if(m_lastSelected != null)
		{
			m_lastSelected.setSelected(false);
		}
		m_model.getMainFrame().getRootPane().setDefaultButton(currentButton());
		currentButton().setSelected(true);
		m_lastSelected = currentButton();
	}

	public void setFocus(final int p_row, final int p_column)
	{
		m_currentRow = p_row;
		m_currentColumn = p_column;
		setDefaultButton();
	}

	public void snapToNextEnabledButton()
	{
		final int startRow = m_currentRow;
		final int startColumn = m_currentColumn;
		while(!currentButton().isEnabled())
		{
			incrementIndices();

			if(m_currentRow == startRow && m_currentColumn == startColumn)
			{
				break;
			}
		}

		setDefaultButton();
	}
}
