/**
 * 
 */
package graph;

import games.DoublePoint2D;

import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;

import client.Direction;

/**
 * @author Ringo
 * 
 */
public class RectangleMaze extends RectangleGraph
{
	private final RectangleGraph	m_template;

	private final Collection<Edge>	m_returnCollection	= new HashSet<Edge>();

	public RectangleMaze(final RectangleGraph p_template, final int p_maxX,
		final int p_maxY)
	{
		super(p_template.getNumNodesWide() + 1,
			p_template.getNumNodesTall() + 1, p_maxX, p_maxY, false);
		m_template = p_template;

		for(int i = 0; i < m_template.getNumNodesWide(); i++)
		{
			for(int j = 0; j < m_template.getNumNodesTall(); j++)
			{
				final Node thisTemplateNode = m_template.getNodeIfExists(i, j);

				final Node upTemplateNode = m_template.getNodeIfExists(i, j - 1);
				final Node rightTemplateNode = m_template.getNodeIfExists(
					i + 1, j);
				final Node leftTemplateNode = m_template.getNodeIfExists(i - 1,
					j);
				final Node downTemplateNode = m_template.getNodeIfExists(i,
					j + 1);

				final Node upLeftMazeNode = getNodeIfExists(i, j);
				final Node upRightMazeNode = getNodeIfExists(i + 1, j);
				final Node downLeftMazeNode = getNodeIfExists(i, j + 1);
				final Node downRightMazeNode = getNodeIfExists(i + 1, j + 1);

				if(upTemplateNode == null)
				{
					connect(upLeftMazeNode, upRightMazeNode, Direction.LEFT,
						Direction.RIGHT);
				}

				if(rightTemplateNode == null
					|| thisTemplateNode.getEdge(rightTemplateNode) == null)
				{
					connect(upRightMazeNode, downRightMazeNode, Direction.UP,
						Direction.DOWN);
				}

				if(downTemplateNode == null
					|| thisTemplateNode.getEdge(downTemplateNode) == null)
				{
					connect(downLeftMazeNode, downRightMazeNode,
						Direction.LEFT, Direction.RIGHT);
				}

				if(leftTemplateNode == null)
				{
					connect(downLeftMazeNode, upLeftMazeNode, Direction.DOWN,
						Direction.UP);
				}
			}
		}
	}

	private double distance(final double x1, final double y1, final double x2,
		final double y2)
	{
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	public DoublePoint2D getCell(final Point p_loc)
	{
		final int x = (int)p_loc.getX();
		final int y = (int)p_loc.getY();

		int xIndex = 0;
		int yIndex = 0;

		while(xIndex < getNumNodesWide() && getXCoordinate(xIndex) <= x)
		{
			xIndex++;
		}

		while(yIndex < getNumNodesTall() && getYCoordinate(yIndex) <= y)
		{
			yIndex++;
		}

		return new DoublePoint2D(xIndex - 1, yIndex - 1);
	}

	public int getCellHeight(final int p_yIndex)
	{
		return getYCoordinate(p_yIndex + 1) -
			getYCoordinate(p_yIndex);
	}

	public int getCellWidth(final int p_xIndex)
	{
		return getXCoordinate(p_xIndex + 1) - getXCoordinate(p_xIndex);
	}

	public Collection<Edge> getCollidingEdges(final Point p_location,
		final int p_distance)
	{
		if(m_returnCollection.size() > 0)
		{
			m_returnCollection.clear();
		}

		Point location = null;
		int x = (int)p_location.getX();
		int y = (int)p_location.getY();
		x = Math.max(0, x);
		y = Math.max(0, y);
		x = Math.min(x, getMaxX());
		y = Math.min(y, getMaxY());

		location = new Point(x, y);

		final DoublePoint2D cell = getCell(location);

		final Node upLeftCorner = getNodeIfExists(cell.getXRounded(),
			cell.getYRounded());
		final Node upRightCorner = getNodeIfExists(cell.getXRounded() + 1,
			cell.getYRounded());
		final Node downLeftCorner = getNodeIfExists(cell.getXRounded(),
			cell.getYRounded() + 1);
		final Node downRightCorner = getNodeIfExists(cell.getXRounded() + 1,
			cell.getYRounded() + 1);

		final Edge rightEdge = upRightCorner == null ? null : upRightCorner.getEdge(downRightCorner);
		final Edge leftEdge = upLeftCorner == null ? null : upLeftCorner.getEdge(downLeftCorner);
		final Edge upEdge = upRightCorner == null ? null : upRightCorner.getEdge(upLeftCorner);
		final Edge downEdge = downRightCorner == null ? null : downRightCorner.getEdge(downLeftCorner);

		final int lowerX = getXCoordinate(cell.getXRounded());
		final int higherX = getXCoordinate(Math.min(cell.getXRounded() + 1,
			getNumNodesWide() - 1));
		final int lowerY = getYCoordinate(cell.getYRounded());
		final int higherY = getYCoordinate(Math.min(cell.getYRounded() + 1,
			getNumNodesTall() - 1));

		if(higherX - location.getX() < p_distance && (rightEdge != null))
		{
			m_returnCollection.add(rightEdge);
		}
		if(location.getX() - lowerX < p_distance && leftEdge != null)
		{
			m_returnCollection.add(leftEdge);
		}
		if(location.getY() - lowerY < p_distance && upEdge != null)
		{
			m_returnCollection.add(upEdge);
		}
		if(higherY - location.getY() < p_distance && downEdge != null)
		{
			m_returnCollection.add(downEdge);
		}

		if(distance(location.getX(), location.getY(), higherX, lowerY) < p_distance)
		{
			upRightCorner.addAllEdgesToBag(m_returnCollection);
		}
		if(distance(location.getX(), location.getY(), higherX, higherY) < p_distance)
		{
			downRightCorner.addAllEdgesToBag(m_returnCollection);
		}
		if(distance(location.getX(), location.getY(), lowerX, lowerY) < p_distance)
		{
			upLeftCorner.addAllEdgesToBag(m_returnCollection);
		}
		if(distance(location.getX(), location.getY(), lowerX, higherY) < p_distance)
		{
			downLeftCorner.addAllEdgesToBag(m_returnCollection);
		}

		if(m_returnCollection.isEmpty())
		{
			return null;
		}

		return new HashSet<Edge>(m_returnCollection);
	}

	public Edge getEdge(final DoublePoint2D p_cell, final Direction p_direction)
	{
		final Node upLeftCorner = getNodeIfExists(p_cell.getXRounded(),
			p_cell.getYRounded());
		final Node upRightCorner = getNodeIfExists(p_cell.getXRounded() + 1,
			p_cell.getYRounded());
		final Node downLeftCorner = getNodeIfExists(p_cell.getXRounded(),
			p_cell.getYRounded() + 1);
		final Node downRightCorner = getNodeIfExists(p_cell.getXRounded() + 1,
			p_cell.getYRounded() + 1);

		switch(p_direction)
		{
			case DOWN:
				return downLeftCorner.getEdge(downRightCorner);
			case LEFT:
				return upLeftCorner.getEdge(downLeftCorner);
			case RIGHT:
				return upRightCorner.getEdge(downRightCorner);
			case UP:
				return upRightCorner.getEdge(upLeftCorner);
			default:
				return null;
		}
	}

	public Node getEndSquare()
	{
		return m_template.getEnd();
	}

	public Node getStartNode()
	{
		return m_template.getBeginning();
	}

	/**
	 * @return
	 */
	public RectangleGraph getTemplate()
	{
		return m_template;
	}

	public boolean isInEndBounds(final Point p_loc, final int p_distance)
	{
		final DoublePoint2D cell = getCell(p_loc);
		final Node node = m_template.getNodeIfExists(cell.getXRounded(),
			cell.getYRounded());
		if(node != m_template.getEnd())
		{
			return false;
		}

		return getCollidingEdges(p_loc, p_distance) == null;
	}

	public boolean isInStartBounds(final Point p_loc, final int p_distance)
	{
		final DoublePoint2D cell = getCell(p_loc);
		final Node node = m_template.getNodeIfExists(cell.getXRounded(),
			cell.getYRounded());
		if(node != m_template.getBeginning())
		{
			return false;
		}

		return getCollidingEdges(p_loc, p_distance) == null;
	}
}