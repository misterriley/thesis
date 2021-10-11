/**
 * 
 */
package visual;

import games.DoublePoint2D;


/**
 * @author Ringo
 * 
 */
public class VisualTaskTriangle
{
	private static DoublePoint2D generatePoint(final int p_pixelRadius,
		final double p_angle)
	{
		final DoublePoint2D ret = new DoublePoint2D(p_pixelRadius
			* Math.cos(p_angle), p_pixelRadius * Math.sin(p_angle));
		return ret;
	}

	private final DoublePoint2D		m_vertex1;
	private final DoublePoint2D		m_vertex2;
	private final DoublePoint2D		m_vertex3;
	private final int				m_radius;

	private final DoublePoint2D[]	m_vertices;

	private final DoublePoint2D		m_centroid;

	public VisualTaskTriangle(final int p_pixelRadius, final double p_angle1,
		final double p_angle2, final double p_angle3)
	{
		m_radius = p_pixelRadius;
		m_vertex1 = generatePoint(p_pixelRadius, p_angle1);
		m_vertex2 = generatePoint(p_pixelRadius, p_angle2);
		m_vertex3 = generatePoint(p_pixelRadius, p_angle3);

		m_vertices = new DoublePoint2D[3];
		m_vertices[0] = m_vertex1;
		m_vertices[1] = m_vertex2;
		m_vertices[2] = m_vertex3;

		double centX = 0;
		double centY = 0;
		for(final DoublePoint2D p: m_vertices)
		{
			centX += p.getX() / 3;
			centY += p.getY() / 3;
		}

		m_centroid = new DoublePoint2D(centX, centY);
	}

	public DoublePoint2D getCentroid()
	{
		return m_centroid;
	}

	public int getRadius()
	{
		return m_radius;
	}

	public DoublePoint2D[] getVertices()
	{
		return m_vertices;
	}
}
