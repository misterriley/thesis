/**
 * 
 */
package graph;

import client.Direction;

/**
 * @author Ringo
 * 
 */
public class RectangleGraph extends Graph
{
	private Node[][]	m_nodesArray;
	private int[]		m_xBounds;
	private int[]		m_yBounds;

	public RectangleGraph(final int p_numNodesWide, final int p_numNodesTall,
		final int p_maxX, final int p_maxY, final boolean p_connectAdjacents)
	{
		buildNodeArray(p_numNodesWide, p_numNodesTall);
		constructBoundArrays(p_maxX, p_maxY);

		if(p_connectAdjacents)
		{
			connectAdjacents();
		}
	}

	public RectangleGraph(final Node[][] p_nodesArray,
		final int p_maxX,
		final int p_maxY, final boolean p_connectAdjacents)
	{
		m_nodesArray = p_nodesArray;
		for(final Node[] nodes: p_nodesArray)
		{
			for(final Node node: nodes)
			{
				add(node);
			}
		}

		constructBoundArrays(p_maxX, p_maxY);
		if(p_connectAdjacents)
		{
			connectAdjacents();
		}
	}

	private void buildNodeArray(final int p_numNodesWide,
		final int p_numNodesTall)
	{
		m_nodesArray = new Node[p_numNodesWide][p_numNodesTall];
		for(int i = 0; i < p_numNodesWide; i++)
		{
			for(int j = 0; j < p_numNodesTall; j++)
			{
				m_nodesArray[i][j] = new Node(i, j);
				add(m_nodesArray[i][j]);
			}
		}
	}

	private void connectAdjacents()
	{
		for(int i = 0; i < m_nodesArray.length; i++)
		{
			for(int j = 0; j < m_nodesArray[0].length; j++)
			{
				final Node node = m_nodesArray[i][j];

				final Node down = getNodeIfExists(i, j + 1);
				final Node right = getNodeIfExists(i + 1, j);

				connectIfPossible(node, down, Direction.UP, Direction.DOWN);
				connectIfPossible(node, right, Direction.LEFT, Direction.RIGHT);
			}
		}
	}

	private void connectIfPossible(final Node p_a, final Node p_b,
		final Direction p_aDirection, final Direction p_bDirection)
	{
		if(p_a != null && p_b != null)
		{
			connect(p_a, p_b, p_aDirection, p_bDirection);
		}
	}

	private void constructBoundArrays(final int p_maxX, final int p_maxY)
	{
		m_xBounds = new int[m_nodesArray.length];
		m_yBounds = new int[m_nodesArray[0].length];

		if(m_xBounds.length > 1)
		{
			for(int i = 0; i < m_xBounds.length; i++)
			{
				m_xBounds[i] = (i * p_maxX) / (m_xBounds.length - 1);
			}
		}
		else
		{
			m_xBounds[0] = 0;
		}

		if(m_yBounds.length > 1)
		{
			for(int j = 0; j < m_yBounds.length; j++)
			{
				m_yBounds[j] = (j * p_maxY) / (m_yBounds.length - 1);
			}
		}
		else
		{
			m_yBounds[0] = 0;
		}
	}

	public int getMaxX()
	{
		return m_xBounds[m_xBounds.length - 1];
	}

	public int getMaxY()
	{
		return m_yBounds[m_yBounds.length - 1];
	}

	public Node[][] getNodeArray()
	{
		return m_nodesArray;
	}

	public Node getNodeIfExists(final int p_x,
		final int p_y)
	{
		if(p_x >= 0 && p_x < m_nodesArray.length)
		{
			final Node[] nodes = m_nodesArray[p_x];
			if(p_y >= 0 && p_y < nodes.length)
			{
				return nodes[p_y];
			}
		}

		return null;
	}

	public int getNumNodesTall()
	{
		return m_nodesArray[0].length;
	}

	public int getNumNodesWide()
	{
		return m_nodesArray.length;
	}

	public int getXCoordinate(final int p_xIndex)
	{
		return m_xBounds[p_xIndex];
	}

	public int getYCoordinate(final int p_yIndex)
	{
		return m_yBounds[p_yIndex];
	}

	public boolean isEdgeNode(final Node p_node)
	{
		return p_node.getXIndex() == 0 || p_node.getYIndex() == 0
			|| p_node.getXIndex() == getNumNodesWide() - 1
			|| p_node.getYIndex() == getNumNodesTall() - 1;
	}
}
