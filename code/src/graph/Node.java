/**
 * 
 */
package graph;

import java.util.Collection;

import client.Direction;

/**
 * @author Ringo
 * 
 */
public class Node
{
	private final Edge[]	m_edges;

	private final int		m_xIndex;
	private final int		m_yIndex;

	public Node(final int p_xIndex, final int p_yIndex)
	{
		m_edges = new Edge[Direction.values().length];
		m_xIndex = p_xIndex;
		m_yIndex = p_yIndex;
	}

	public void addAllEdgesToBag(final Collection<Edge> p_bag)
	{
		for(final Edge edge: m_edges)
		{
			if(edge != null)
			{
				p_bag.add(edge);
			}
		}
	}

	public void addEdge(final Edge p_edge, final Direction p_direction)
	{
		m_edges[p_direction.ordinal()] = p_edge;
	}

	/**
	 * @param p_upNode
	 * @return
	 */
	public boolean connectsTo(final Node p_other)
	{
		return getEdge(p_other) != null;
	}

	public int edgeCount()
	{
		return edgeCount(null);
	}

	public int edgeCount(final Graph p_graph)
	{
		int ret = 0;
		for(final Edge edge: m_edges)
		{
			if(edge != null && (p_graph == null || p_graph.contains(edge)))
			{
				ret++;
			}
		}
		return ret;
	}

	public Edge getEdge(final Direction p_direction)
	{
		return m_edges[p_direction.ordinal()];
	}

	public Edge getEdge(final Node p_other)
	{
		if(p_other == null)
		{
			return null;
		}

		for(final Edge edge: m_edges)
		{
			if(edge != null && edge.contains(p_other))
			{
				return edge;
			}
		}

		return null;
	}

	public int getXIndex()
	{
		return m_xIndex;
	}

	public int getYIndex()
	{
		return m_yIndex;
	}

	public void remove(final Edge p_edge)
	{
		for(int i = 0; i < m_edges.length; i++)
		{
			if(m_edges[i] == p_edge)
			{
				m_edges[i] = null;
			}
		}
	}
}