/**
 * 
 */
package games;

/**
 * @author Ringo
 * 
 */
public class DoublePoint2D
{
	private final double	m_x;
	private final double	m_y;

	public DoublePoint2D(final double p_x, final double p_y)
	{
		m_x = p_x;
		m_y = p_y;
	}

	public double getX()
	{
		return m_x;
	}

	public int getXRounded()
	{
		return (int)Math.round(m_x);
	}

	public double getY()
	{
		return m_y;
	}

	public int getYRounded()
	{
		return (int)Math.round(m_y);
	}

	public DoublePoint2D minus(final DoublePoint2D p_other)
	{
		final DoublePoint2D ret = new DoublePoint2D(this.m_x - p_other.m_x,
			this.m_y - p_other.m_y);
		return ret;
	}

	public DoublePoint2D normalized()
	{
		final double length = Math.pow(m_y * m_y + m_x * m_x, .5);
		return this.times(1 / length);
	}

	public DoublePoint2D perpLeft()
	{
		return new DoublePoint2D(-1 * this.m_y, this.m_x);
	}

	public DoublePoint2D perpRight()
	{
		return new DoublePoint2D(this.m_y, -1 * this.m_x);
	}

	public DoublePoint2D plus(final DoublePoint2D p_other)
	{
		final DoublePoint2D ret = new DoublePoint2D(this.m_x + p_other.m_x,
			this.m_y + p_other.m_y);
		return ret;
	}

	public DoublePoint2D times(final double p_factor)
	{
		return new DoublePoint2D(this.m_x * p_factor, this.m_y * p_factor);
	}
}