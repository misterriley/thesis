/**
 * 
 */
package graph;

import client.Direction;

/**
 * @author Ringo
 * 
 */
public class Edge
{
	private final Node		m_a;

	private final Node		m_b;

	// in which direction does a lie?
	private final Direction	m_aDirection;

	// in which direction does b lie?
	private final Direction	m_bDirection;

	public Edge(final Node p_a, final Node p_b, final Direction p_aDirection,
		final Direction p_bDirection)
	{
		m_a = p_a;
		m_b = p_b;

		m_aDirection = p_aDirection;
		m_bDirection = p_bDirection;

		// if a is in the a direction, this edge is the opposite from that
		m_a.addEdge(this, p_bDirection);
		m_b.addEdge(this, p_aDirection);
	}

	public boolean contains(final Node p_node)
	{
		return p_node == m_a || p_node == m_b;
	}

	public Direction getADirection()
	{
		return m_aDirection;
	}

	public Direction getBDirection()
	{
		return m_bDirection;
	}

	public Node getNodeA()
	{
		return m_a;
	}

	public Node getNodeB()
	{
		return m_b;
	}

	public Node getOtherNode(final Node p_node)
	{
		if(p_node == m_a)
		{
			return m_b;
		}

		if(p_node == m_b)
		{
			return m_a;
		}

		throw new RuntimeException();
	}
}