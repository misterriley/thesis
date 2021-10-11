/**
 * 
 */
package visual;

import games.DoublePoint2D;
import games.GameModel;
import games.GameViewPanel;

import java.awt.Color;
import java.awt.Graphics;

import client.Constants;

/**
 * @author Ringo
 * 
 */
public class VisualTaskViewPanel extends GameViewPanel
{
	private static final long			serialVersionUID	= 1L;

	public static final int				MAX_TRIANGLE_RADIUS	= Constants.AREA_HEIGHT / 2 - 20;
	public static final int				MIN_TRIANGLE_RADIUS	= 30;

	private static final int			LEFT_X				= Constants.AREA_HEIGHT / 2;
	private static final int			RIGHT_X				= Constants.AREA_WIDTH
																- Constants.AREA_HEIGHT
																/ 2;
	private static final int			Y					= Constants.AREA_HEIGHT / 2;

	private static final DoublePoint2D	LEFT_POINT			= new DoublePoint2D(
																LEFT_X, Y);
	private static final DoublePoint2D	RIGHT_POINT			= new DoublePoint2D(
																RIGHT_X, Y);

	private VisualTaskTriangle			m_triangle1;
	private VisualTaskTriangle			m_triangle2;

	private VisualTaskMode				m_currentMode;

	public VisualTaskViewPanel(final GameModel p_model)
	{
		super(p_model);
	}

	@Override
	protected boolean displayFeedback()
	{
		return true;
	}

	private void drawCircle(final DoublePoint2D p_center, final int p_radius,
		final boolean p_filled, final Graphics g, final Color p_color)
	{
		g.setColor(p_color);
		if(p_filled)
		{
			g.fillOval(p_center.getXRounded() - p_radius,
				p_center.getYRounded()
				- p_radius, 2 * p_radius, 2 * p_radius);
		}
		else
		{
			g.drawOval(p_center.getXRounded() - p_radius,
				p_center.getYRounded()
				- p_radius, 2 * p_radius, 2 * p_radius);
		}
	}

	private void drawCircles(final Graphics g, final Color p_leftColor,
		final Color p_rightColor, final DoublePoint2D circumCenter1,
		final DoublePoint2D circumCenter2, final int p_radius1,
		final int p_radius2)
	{
		final DoublePoint2D median = circumCenter1.plus(circumCenter2).times(
			.5);
		drawCircle(median, p_radius1, false, g,
			p_leftColor);
		drawCircle(median, p_radius2, false, g,
			p_rightColor);
		drawCommonExteriorTangents(median, circumCenter1,
			p_radius1, g, p_leftColor);
		drawCommonExteriorTangents(median, circumCenter2,
			p_radius2, g, p_rightColor);
	}

	private void drawCommonExteriorTangents(final DoublePoint2D circumCenter1,
		final DoublePoint2D circumCenter2,
		final int radius, final Graphics g, final Color p_color)
	{
		g.setColor(p_color);
		final DoublePoint2D vector = circumCenter2.minus(circumCenter1);

		final DoublePoint2D perpRadiusVectorL = vector.perpLeft().normalized().times(
			radius);
		final DoublePoint2D perpRadiusVectorR = vector.perpRight().normalized().times(
			radius);

		final DoublePoint2D leftEndpoint1 = circumCenter1.plus(perpRadiusVectorL);
		final DoublePoint2D leftEndpoint2 = circumCenter1.plus(perpRadiusVectorR);

		final DoublePoint2D rightEndpoint1 = circumCenter2.plus(perpRadiusVectorL);
		final DoublePoint2D rightEndpoint2 = circumCenter2.plus(perpRadiusVectorR);

		drawLine(leftEndpoint1, rightEndpoint1, g);
		drawLine(leftEndpoint2, rightEndpoint2, g);
	}

	private void drawLine(final DoublePoint2D p_start,
		final DoublePoint2D p_end, final Graphics g)
	{
		g.drawLine(p_start.getXRounded(), p_start.getYRounded(),
			p_end.getXRounded(), p_end.getYRounded());
	}

	private void drawSolution(final Graphics g, final Color p_leftColor,
		final Color p_rightColor)
	{
		g.setColor(Color.BLUE);

		final DoublePoint2D circumCenter1 = LEFT_POINT.minus(m_triangle1.getCentroid());
		final DoublePoint2D circumCenter2 = RIGHT_POINT.minus(m_triangle2.getCentroid());

		if(m_triangle1.getRadius() == m_triangle2.getRadius())
		{
			final int radius = m_triangle1.getRadius();
			drawCommonExteriorTangents(circumCenter1, circumCenter2,
				radius, g, Color.GREEN);
		}
		else
		{
			drawCircles(g, p_leftColor, p_rightColor, circumCenter1,
				circumCenter2, m_triangle1.getRadius(), m_triangle2.getRadius());
		}
	}

	private void drawTriangle(final VisualTaskTriangle p_vtt,
		final DoublePoint2D p_absoluteCentroid, final Graphics g,
		final Color p_circleColor)
	{
		if(p_vtt == null)
		{
			return;
		}

		g.setColor(Color.BLACK);

		final DoublePoint2D[] vertices = p_vtt.getVertices();
		final DoublePoint2D relativeCentroid = p_vtt.getCentroid();

		for(int i = 0; i < vertices.length; i++)
		{
			final DoublePoint2D start = vertices[i].minus(relativeCentroid).plus(
				p_absoluteCentroid);
			final int nextIndex = (i + 1) % vertices.length;
			final DoublePoint2D end = vertices[nextIndex].minus(
				relativeCentroid).plus(p_absoluteCentroid);

			drawLine(start, end, g);
		}

		if(m_currentMode == VisualTaskMode.DRAW_TRIANGLES_AND_CIRCLES ||
			m_currentMode == VisualTaskMode.DRAW_FULL_SOLUTION)
		{
			final DoublePoint2D circumCenter = p_absoluteCentroid.minus(relativeCentroid);
			drawCircle(circumCenter, p_vtt.getRadius(), false, g, p_circleColor);
		}
	}

	@Override
	public void enableNeutralDisplay()
	{
		m_triangle1 = null;
		m_triangle2 = null;
	}

	@Override
	public void paintThisComponent(final Graphics g)
	{
		if(m_currentMode != null && m_currentMode.isTriangleDrawingMode())
		{
			if(m_triangle1 != null && m_triangle2 != null)
			{
				Color color1 = Color.GREEN;
				Color color2 = Color.GREEN;

				if(m_triangle1.getRadius() > m_triangle2.getRadius())
				{
					color1 = Color.BLUE;
					color2 = Color.PINK;
				}
				else if(m_triangle1.getRadius() < m_triangle2.getRadius())
				{
					color1 = Color.PINK;
					color2 = Color.BLUE;
				}

				drawTriangle(m_triangle1, LEFT_POINT, g, color1);
				drawTriangle(m_triangle2, RIGHT_POINT, g, color2);

				if(m_currentMode == VisualTaskMode.DRAW_FULL_SOLUTION)
				{
					drawSolution(g, color1, color2);
				}
			}
		}

		if(m_currentMode == VisualTaskMode.DRAW_CIRCLES)
		{
			final int radius1 = (MAX_TRIANGLE_RADIUS
				+ m_model.getCurrentDifficulty())
				/ 2;
			final int radius2 = radius1 - m_model.getCurrentDifficulty();

			drawCircle(LEFT_POINT, radius1, false, g, Color.BLACK);
			drawCircle(RIGHT_POINT, radius2, false, g, Color.BLACK);
			drawCircles(g, Color.BLACK, Color.BLACK, LEFT_POINT, RIGHT_POINT,
				radius1, radius2);
		}
	}

	public void setTaskMode(final VisualTaskMode p_mode)
	{
		m_currentMode = p_mode;
		repaint();
	}

	public void setTriangle1(final VisualTaskTriangle p_triangle)
	{
		m_triangle1 = p_triangle;
		repaint();
	}

	public void setTriangle2(final VisualTaskTriangle p_triangle)
	{
		m_triangle2 = p_triangle;
		repaint();
	}
}