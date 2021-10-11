/**
 * 
 */
package graph;

import java.util.Collection;
import java.util.HashSet;

import client.Direction;

/**
 * @author Ringo
 * 
 */
public class Graph
{
	private final Collection<Node>	m_nodes;
	private final Collection<Edge>	m_edges;

	private Node					m_beginning;
	private Node					m_end;

	public Graph()
	{
		m_nodes = new HashSet<Node>();
		m_edges = new HashSet<Edge>();
	}

	public void add(final Edge p_edge)
	{
		m_edges.add(p_edge);
	}

	public void add(final Graph p_graph)
	{
		for(final Node node: p_graph.m_nodes)
		{
			add(node);
		}

		for(final Edge edge: p_graph.m_edges)
		{
			add(edge);
		}
	}

	public void add(final Node p_node)
	{
		m_nodes.add(p_node);
	}

	public void connect(final Node p_node1, final Node p_node2,
		final Direction p_node1Direction, final Direction p_node2Direction)
	{
		final Edge edge = new Edge(p_node1, p_node2, p_node1Direction,
			p_node2Direction);
		add(edge);
	}

	public boolean contains(final Edge p_edge)
	{
		return m_edges.contains(p_edge);
	}

	public boolean contains(final Node p_node)
	{
		return m_nodes.contains(p_node);
	}

	public Node getBeginning()
	{
		return m_beginning;
	}

	public Edge[] getEdges()
	{
		return m_edges.toArray(new Edge[0]);
	}

	public Node getEnd()
	{
		return m_end;
	}

	public Node[] getNodes()
	{
		return m_nodes.toArray(new Node[0]);
	}

	public void remove(final Edge p_edge)
	{
		m_edges.remove(p_edge);
	}

	public void setBeginning(final Node p_beginning)
	{
		m_beginning = p_beginning;
	}

	public void setEnd(final Node p_end)
	{
		m_end = p_end;
	}
}