/**
 * 
 */
package kinesthetic;


import java.awt.Point;
import java.util.ArrayList;

import client.Direction;

/**
 * @author Ringo
 * 
 */
public class Ball
{
	private int								m_radius;
	private Point							m_position;
	private ArrayList<BallMovementListener>	m_listeners;

	public void addListener(final BallMovementListener p_listener)
	{
		if(m_listeners == null)
		{
			m_listeners = new ArrayList<BallMovementListener>();
		}

		m_listeners.add(p_listener);
	}

	private void fireChangeNotification()
	{
		if(m_listeners == null)
		{
			return;
		}

		for(final BallMovementListener listener: m_listeners)
		{
			listener.ballMoved(this);
		}
	}

	public Point getEdgePoint(final Direction p_dir)
	{
		int x = (int)m_position.getX();
		int y = (int)m_position.getY();

		switch(p_dir)
		{
			case DOWN:
				y += m_radius;
				break;
			case LEFT:
				x -= m_radius;
				break;
			case RIGHT:
				x += m_radius;
				break;
			case UP:
				y -= m_radius;
				break;
		}

		return new Point(x, y);
	}

	public Point getPosition()
	{
		return m_position;
	}

	public int getRadius()
	{
		return m_radius;
	}

	public void setPosition(final Point position)
	{
		m_position = position;
		fireChangeNotification();
	}

	public void setRadius(final int p_radius)
	{
		m_radius = p_radius;
		fireChangeNotification();
	}
}
