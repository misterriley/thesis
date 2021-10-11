/**
 * 
 */
package graph;

import java.util.Arrays;
import java.util.HashSet;

import client.Randomizer;

/**
 * @author Ringo
 * 
 */
public class TreeGenerator
{
	public static RectangleGraph findSpanningTree(
		final RectangleGraph p_graph)
	{
		final HashSet<Graph> graphs = new HashSet<Graph>();
		final RectangleGraph ret = new RectangleGraph(p_graph.getNodeArray(),
			p_graph.getMaxX(), p_graph.getMaxY(), false);

		for(final Node node: p_graph.getNodes())
		{
			final Graph tempGraph = new Graph();
			tempGraph.add(node);
			graphs.add(tempGraph);
		}

		final Edge[] edges = p_graph.getEdges();
		Arrays.sort(edges, new Randomizer());
		for(final Edge edge: edges)
		{
			final Node nodeA = edge.getNodeA();
			final Node nodeB = edge.getNodeB();

			Graph graphA = null;
			Graph graphB = null;
			for(final Graph graph: graphs)
			{
				if(graph.contains(nodeA))
				{
					graphA = graph;
				}

				if(graph.contains(nodeB))
				{
					graphB = graph;
				}
			}

			if(graphA != graphB)
			{
				final Graph superGraph = new Graph();
				superGraph.add(graphA);
				superGraph.add(graphB);
				superGraph.add(edge);
				graphs.remove(graphA);
				graphs.remove(graphB);
				graphs.add(superGraph);

				ret.add(edge);
			}
		}

		if(graphs.size() != 1)
		{
			throw new RuntimeException("Unable to create tree");
		}

		return ret;
	}
}