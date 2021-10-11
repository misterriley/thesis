/**
 * 
 */
package games;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import client.Constants;
import client.Direction;

/**
 * @author Ringo
 * 
 */
public abstract class GameControlPanel extends JPanel
	implements ActionListener
{
	private static final int			BUTTON_PAD			= 10;
	private static final long			serialVersionUID	= -277104722210237004L;

	private final Thread				m_interruptThread;
	protected final GameModel			m_model;
	private final JButton				m_continueButton;
	private int							m_numButtonRows;
	private final ArrayList<JButton>	m_buttonList;

	public GameControlPanel(final GameModel p_model)
	{
		setLayout(null);
		p_model.setButtonFocusManager(new ButtonFocusManager(p_model));
		m_interruptThread = Thread.currentThread();
		m_model = p_model;
		m_continueButton = new JButton("Continue");
		m_numButtonRows = 0;
		m_buttonList = new ArrayList<JButton>();
	}

	public void actionPerformed(final ActionEvent p_arg0)
	{
		if(p_arg0.getSource() == getContinueButton())
		{
			interruptControlThread();
		}
		else
		{
			thisActionPerformed(p_arg0);
		}
	}

	public void addButton(final JButton p_button)
	{
		setupOneButton(p_button, Constants.BORDER_IN_PIXELS,
			Constants.BUTTON_ROW_WIDTH, 0);
		m_numButtonRows++;
		resetPreferredSize();
	}

	public void addButtonRow(final ArrayList<JButton> p_row)
	{
		final int buttonWidth = getButtonWidth(p_row.size());
		for(int i = 0; i < p_row.size(); i++)
		{
			final int x = getColumnX(i, p_row.size());
			setupOneButton(p_row.get(i), x, buttonWidth, i);
		}

		m_numButtonRows++;
		resetPreferredSize();
	}

	public void cycleDefaultButton(final Direction p_dir)
	{
		m_model.getButtonFocusManager().cycle(p_dir);
	}

	public void enableOnlyContinueButtons()
	{
		setButtonEnabled(m_continueButton, true);
		for(final JButton button: m_buttonList)
		{
			if(button != m_continueButton)
			{
				setButtonEnabled(button, false);
			}
		}
	}

	private int getButtonWidth(final int p_numColumns)
	{
		return (Constants.BUTTON_ROW_WIDTH - (p_numColumns - 1) * BUTTON_PAD)
			/ p_numColumns;
	}

	private int getButtonY(final int index)
	{
		return Constants.BORDER_IN_PIXELS * (index + 1)
			+ Constants.BUTTON_HEIGHT * (index);
	}

	private int getColumnX(final int p_columnIndex, final int p_numColumns)
	{
		final int buttonWidth = getButtonWidth(p_numColumns);
		return Constants.BORDER_IN_PIXELS + p_columnIndex
			* (buttonWidth + BUTTON_PAD);
	}

	public JButton getContinueButton()
	{
		return m_continueButton;
	}

	public GameModel getModel()
	{
		return m_model;
	}

	private int getPreferredHeight(final int p_numButtons)
	{
		return getButtonY(p_numButtons + 1);
	}

	protected void interruptControlThread()
	{
		m_interruptThread.interrupt();
	}

	private void resetPreferredSize()
	{
		final int height = getPreferredHeight(m_numButtonRows);
		setPreferredSize(new Dimension(Constants.BORDER_IN_PIXELS * 2
			+ Constants.BUTTON_ROW_WIDTH,
			height));
	}

	public void setButtonEnabled(final JButton p_button,
		final boolean p_isEnabled)
	{
		p_button.setEnabled(p_isEnabled);
		m_model.getButtonFocusManager().snapToNextEnabledButton();
	}

	public void setContinueButtonEnabled(final boolean p_enabled)
	{
		setButtonEnabled(m_continueButton, p_enabled);
	}

	private void setupOneButton(final JButton p_button, final int x,
		final int width, final int column)
	{
		add(p_button);
		p_button.setBounds(x, getButtonY(m_numButtonRows), width,
			Constants.BUTTON_HEIGHT);
		p_button.addActionListener(this);
		m_model.getButtonFocusManager().putButton(p_button, m_numButtonRows,
			column);
		m_buttonList.add(p_button);
	}

	public abstract void thisActionPerformed(ActionEvent p_arg0);
}
