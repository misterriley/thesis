/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import client.Direction;
import client.Randomizer;

/**
 * @author Ringo
 * 
 */
public class FullPathFinder
{
	private static boolean buildLogicalExtensions(final RectangleGraph p_graph,
		final HashSet<Edge> safeEdges,
		final HashSet<Node> lockedNodes, final Node p_leadingNode)
		throws Exception
	{
		boolean ret = false;
		boolean didSomething = true;
		while(didSomething)
		{
			didSomething = lockSafeEdges(p_graph, p_leadingNode,
				safeEdges, lockedNodes);

			didSomething = lockNodesAndPruneEdges(p_graph, safeEdges,
				lockedNodes)
				|| didSomething;

			if(didSomething)
			{
				ret = true;
			}
		}
		return ret;
	}

	public static RectangleGraph constructFullPath(final int p_graphColumns,
		final int p_graphRows, final int p_maxX, final int p_maxY,
		final Node p_lastStart)
	{
		RectangleGraph graph = null;
		while(true)
		{
			graph = new RectangleGraph(p_graphColumns,
				p_graphRows, p_maxX, p_maxY, true);
			if(tryToBuildFullPath(graph, p_lastStart))
			{
				break;
			}
		}
		return graph;
	}

	private static int countSafeEdges(final Node p_node,
		final HashSet<Edge> p_safeEdges)
	{
		int ret = 0;
		for(final Direction d: Direction.values())
		{
			if(p_safeEdges.contains(p_node.getEdge(d)))
			{
				ret++;
			}
		}
		return ret;
	}

	private static ArrayList<Node> getLeadingPath(final Node start,
		final HashSet<Edge> safeEdges)
	{
		if(start == null)
		{
			return null;
		}

		final ArrayList<Node> ret = new ArrayList<Node>();
		ret.add(start);

		Node leadingNode = start;

		final ArrayList<Edge> takenEdges = new ArrayList<Edge>();

		while(true)
		{
			boolean safeEdgeFound = false;
			for(final Direction d: Direction.values())
			{
				final Edge edge = leadingNode.getEdge(d);
				if(edge != null && safeEdges.contains(edge)
					&& !takenEdges.contains(edge))
				{
					takenEdges.add(edge);
					leadingNode = edge.getOtherNode(leadingNode);
					ret.add(leadingNode);
					safeEdgeFound = true;
					break;
				}
			}

			if(!safeEdgeFound)
			{
				break;
			}
		}

		return ret;
	}

	private static boolean graphIsConnected(final RectangleGraph p_graph,
		final Collection<Node> p_excludedNodes)
	{
		final Node start = selectAnyNode(p_graph, p_excludedNodes);
		final Collection<Node> nodeBag = new HashSet<Node>();
		nodeBag.add(start);

		boolean repeat = true;
		while(repeat)
		{
			repeat = false;
			final Node[] nodes = nodeBag.toArray(new Node[0]);
			for(final Node node: nodes)
			{
				for(final Direction d: Direction.values())
				{
					final Edge edge = node.getEdge(d);
					if(edge == null || !p_graph.contains(edge))
					{
						continue;
					}

					final Node other = edge.getOtherNode(node);
					if(!nodeBag.contains(other)
						&& !p_excludedNodes.contains(other))
					{
						repeat = true;
						nodeBag.add(other);
					}
				}
			}
		}

		return nodeBag.size() + p_excludedNodes.size() == p_graph.getNodes().length;
	}

	private static void lockNodeAndEdges(final RectangleGraph p_graph,
		final HashSet<Edge> safeEdges, final HashSet<Node> lockedNodes,
		final Node node)
	{
		lockedNodes.add(node);
		for(final Direction d: Direction.values())
		{
			final Edge edge = node.getEdge(d);
			if(p_graph.contains(edge))
			{
				safeEdges.add(edge);
			}
		}
	}

	private static boolean lockNodesAndPruneEdges(final RectangleGraph p_graph,
		final HashSet<Edge> safeEdges, final HashSet<Node> lockedNodes)
	{
		boolean prunedAnything = false;

		for(final Node node: p_graph.getNodes())
		{
			if(lockedNodes.contains(node))
			{
				continue;
			}

			if(node != p_graph.getBeginning()
				&& node != p_graph.getEnd()
				&& countSafeEdges(node, safeEdges) == 2)
			{
				prunedAnything = true;
				pruneDeadEdges(p_graph, safeEdges, lockedNodes, node);
			}

			if(node == p_graph.getEnd() || node == p_graph.getBeginning()
				&& countSafeEdges(node, safeEdges) == 1)
			{
				prunedAnything = true;
				pruneDeadEdges(p_graph, safeEdges, lockedNodes, node);
			}
		}

		return prunedAnything;
	}

	private static boolean lockSafeEdges(final RectangleGraph p_graph,
		final Node p_leadingNode,
		final HashSet<Edge> safeEdges, final HashSet<Node> lockedNodes)
		throws Exception
	{
		boolean lockedAnything = false;
		if(!lockedNodes.contains(p_leadingNode)
			&& p_leadingNode != p_graph.getBeginning()
			&& p_leadingNode.edgeCount(p_graph) == 2)
		{
			lockedAnything = true;
			lockNodeAndEdges(p_graph, safeEdges, lockedNodes, p_leadingNode);
		}

		for(final Node node: p_graph.getNodes())
		{
			if(!lockedNodes.contains(node) && node != p_graph.getBeginning()
				&& node != p_graph.getEnd())
			{
				final int edgeCount = node.edgeCount(p_graph);
				if(edgeCount == 2 && p_graph.getEnd() != null)
				{
					lockedAnything = true;
					lockNodeAndEdges(p_graph, safeEdges, lockedNodes, node);
				}
				else if(edgeCount == 1)
				{
					if(p_graph.getEnd() != null)
					{
						throw new Exception();
					}

					p_graph.setEnd(node);
					lockNodeAndEdges(p_graph, safeEdges, lockedNodes, node);
				}
			}
		}

		return lockedAnything;
	}

	private static void pruneDeadEdges(final RectangleGraph p_graph,
		final HashSet<Edge> safeEdges, final HashSet<Node> lockedNodes,
		final Node node)
	{
		lockedNodes.add(node);
		for(final Direction d: Direction.values())
		{
			final Edge edge = node.getEdge(d);
			if(!safeEdges.contains(edge) && edge != null)
			{
				edge.getNodeA().remove(edge);
				edge.getNodeB().remove(edge);
				p_graph.remove(edge);
			}
		}
	}

	private static Node selectAnyNode(final RectangleGraph p_graph,
		final Collection<Node> p_excludedNodes)
	{
		for(final Node node: p_graph.getNodes())
		{
			if(!p_excludedNodes.contains(node))
			{
				return node;
			}
		}

		return null;
	}

	private static Node selectRandomNode(final RectangleGraph p_graph,
		final Node p_lastNode)
	{
		final Node[] nodes = p_graph.getNodes();
		final Random rand = new Random();
		int index = 0;
		while(true)
		{
			index = rand.nextInt(nodes.length);
			if(p_lastNode == null
				|| nodes[index].getXIndex() != p_lastNode.getXIndex()
				|| nodes[index].getYIndex() != p_lastNode.getYIndex())
			{
				break;
			}
		}
		return nodes[index];
	}

	private static boolean tryToBuildFullPath(final RectangleGraph graph,
		final Node p_lastStart)
	{
		graph.setBeginning(selectRandomNode(graph, p_lastStart));

		final HashSet<Edge> safeEdges = new HashSet<Edge>();
		final HashSet<Node> lockedNodes = new HashSet<Node>();

		while(true)
		{
			final ArrayList<Node> startPath = getLeadingPath(
				graph.getBeginning(), safeEdges);

			if(startPath.size() == graph.getNodes().length)
			{
				return true;
			}

			final ArrayList<Node> endPath = getLeadingPath(graph.getEnd(),
				safeEdges);
			final Node leadingNode = startPath.get(startPath.size() - 1);

			boolean movement;
			try
			{
				movement = buildLogicalExtensions(graph, safeEdges,
					lockedNodes, leadingNode);
			}
			catch(final Exception ex)
			{
				return false;
			}

			final HashSet<Node> tempLockedNodes = new HashSet<Node>();
			tempLockedNodes.addAll(startPath);

			final Direction[] directions = Direction.values();
			Arrays.sort(directions, new Randomizer());
			for(final Direction d: directions)
			{
				final Edge edge = leadingNode.getEdge(d);
				if(edge == null || safeEdges.contains(edge))
				{
					continue;
				}

				final Node nextNode = edge.getOtherNode(leadingNode);
				if(endPath != null && endPath.contains(nextNode))
				{
					continue;
				}

				tempLockedNodes.add(nextNode);
				if(graphIsConnected(graph, tempLockedNodes))
				{
					movement = true;
					safeEdges.add(edge);
					break;
				}
				tempLockedNodes.remove(nextNode);
			}

			if(!movement)
			{
				return false;
			}
		}
	}
}