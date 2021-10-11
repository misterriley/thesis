/**
 * 
 */
package verbal;

import games.GameModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.Constants;

/**
 * @author Ringo
 * 
 */
public class Countdown
	implements ActionListener
{
	private final GameModel	m_model;
	private int				m_timerCount;

	public Countdown(final GameModel p_model, final int p_timerCount)
	{
		m_model = p_model;
		m_timerCount = p_timerCount;
	}

	public void actionPerformed(final ActionEvent p_e)
	{
		m_timerCount--;
		m_model.getVerbalTVP().setSecondsLeft(m_timerCount);
		if(m_timerCount == 0)
		{
			// m_model.getVerbalTaskController().stopTimer();
			m_model.getVerbalTVP().setDisplayString(
				Constants.VERBAL_TASK_TIME_UP_PROMPT);
		}
	}
}
