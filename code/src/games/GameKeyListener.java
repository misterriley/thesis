/**
 * 
 */
package games;


import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import client.Direction;

/**
 * @author Ringo
 * 
 */
public class GameKeyListener
	implements KeyListener, FocusListener
{
	private final boolean[]	m_pressedDirs;
	private final GameModel	m_model;
	private KeyListenerMode	m_mode;
	private boolean			m_waitingForRelease;

	public GameKeyListener(final GameModel p_model,
		final KeyListenerMode p_initialMode)
	{
		m_model = p_model;
		m_pressedDirs = new boolean[Direction.values().length];
		m_mode = p_initialMode;
	}

	private synchronized boolean allDirectionsReleased()
	{
		for(final boolean pressed: m_pressedDirs)
		{
			if(pressed)
			{
				return false;
			}
		}

		return true;
	}

	public synchronized void clearAll()
	{
		for(int i = 0; i < m_pressedDirs.length; i++)
		{
			m_pressedDirs[i] = false;
		}
	}

	private synchronized void doChangeArray(final KeyEvent p_e,
		final boolean p_value)
	{
		switch(p_e.getKeyCode())
		{
			case KeyEvent.VK_DOWN:
				m_pressedDirs[Direction.DOWN.ordinal()] = p_value;
				break;
			case KeyEvent.VK_UP:
				m_pressedDirs[Direction.UP.ordinal()] = p_value;
				break;
			case KeyEvent.VK_LEFT:
				m_pressedDirs[Direction.LEFT.ordinal()] = p_value;
				break;
			case KeyEvent.VK_RIGHT:
				m_pressedDirs[Direction.RIGHT.ordinal()] = p_value;
				break;
		}
	}

	public void focusGained(final FocusEvent p_arg0)
	{

	}

	public void focusLost(final FocusEvent p_arg0)
	{
		setWaitingForRelease(false);
		clearAll();
	}

	public synchronized boolean isDirectionPressed(final Direction p_dir)
	{
		return m_pressedDirs[p_dir.ordinal()];
	}

	public void keyPressed(final KeyEvent p_e)
	{
		if(m_mode == KeyListenerMode.OFF)
		{
			return;
		}

		final ButtonFocusManager bfm = m_model.getButtonFocusManager();

		if(m_mode == KeyListenerMode.TASK)
		{
			doChangeArray(p_e, true);
		}
		else if(m_mode.isUIMode() && !waitingForRelease())
		{
			final int value = m_model.getCurrentDifficulty();
			switch(p_e.getKeyCode())
			{
				case KeyEvent.VK_LEFT:
					if(m_mode.isSliderMode() && m_model.isSliderEnabled())
					{
						m_model.setDifficulty(value - 1);
					}
					else if(m_mode.isButtonMode())
					{
						bfm.cycle(Direction.LEFT);
					}
					break;
				case KeyEvent.VK_RIGHT:
					if(m_mode.isSliderMode() && m_model.isSliderEnabled())
					{
						m_model.setDifficulty(value + 1);
					}
					else if(m_mode.isButtonMode())
					{
						bfm.cycle(Direction.RIGHT);
					}
					break;
				case KeyEvent.VK_UP:
					if(m_mode.isButtonMode())
					{
						bfm.cycle(Direction.UP);
					}
					break;
				case KeyEvent.VK_DOWN:
					if(m_mode.isButtonMode())
					{
						bfm.cycle(Direction.DOWN);
					}
					break;
			}
		}
	}

	public void keyReleased(final KeyEvent p_e)
	{
		if(m_mode == KeyListenerMode.OFF)
		{
			return;
		}

		if(m_mode == KeyListenerMode.TASK
			|| waitingForRelease())
		{
			doChangeArray(p_e, false);

			if(waitingForRelease() && allDirectionsReleased())
			{
				setWaitingForRelease(false);
			}
		}
	}

	public void keyTyped(final KeyEvent p_e)
	{
		if(m_mode == KeyListenerMode.OFF)
		{
			return;
		}
	}

	public void setMode(final KeyListenerMode p_mode)
	{
		m_mode = p_mode;
		if(!allDirectionsReleased())
		{
			setWaitingForRelease(true);
		}
	}

	private synchronized void setWaitingForRelease(final boolean p_bool)
	{
		m_waitingForRelease = p_bool;
	}

	private synchronized boolean waitingForRelease()
	{
		return m_waitingForRelease;
	}
}
