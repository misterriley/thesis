/**
 * 
 */
package graph;

/**
 * @author Ringo
 * 
 */
public class Path extends Graph
{
	private final Graph	m_containingGraph;

	public Path(final RectangleGraph p_graph, final Node p_singleton)
	{
		m_containingGraph = p_graph;
		setBeginning(p_singleton);
		setEnd(p_singleton);
		add(p_singleton);
	}

	public void addToBeginning(final Node p_node)
	{
		add(p_node);
		add(p_node.getEdge(getBeginning()));
		setBeginning(p_node);
	}

	public void addToBeginning(final Path p_path)
	{
		add(p_path);
		add(getBeginning().getEdge(p_path.getEnd()));
		setBeginning(p_path.getBeginning());
	}

	public void addToEnd(final Path p_path)
	{
		add(p_path);
		add(getEnd().getEdge(p_path.getBeginning()));
		setEnd(p_path.getEnd());
	}

	public Graph getContainingGraph()
	{
		return m_containingGraph;
	}

	public int length()
	{
		return getEdges().length;
	}

	public void reverseDirection()
	{
		final Node temp = getEnd();
		setEnd(getBeginning());
		setBeginning(temp);
	}
}
