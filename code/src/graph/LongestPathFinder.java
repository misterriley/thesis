/**
 * 
 */
package graph;

import java.util.ArrayList;

import client.Direction;

/**
 * @author Ringo
 * 
 */
public class LongestPathFinder
{
	private static TreeSpanData constructTreeSpanData(final Node p_parent,
		final Node p_current, final RectangleGraph p_tree,
		final boolean p_onlyEdgeNodes)
	{
		final int arrayLen = p_parent == null ? p_current.edgeCount(p_tree) : p_current.edgeCount(p_tree) - 1;

		if(arrayLen == 0)
		{
			if(!p_onlyEdgeNodes || p_tree.isEdgeNode(p_current))
			{
				// base case
				final TreeSpanData ret = new TreeSpanData();
				final Path path = new Path(p_tree, p_current);
				ret.setLongestPath(path);
				ret.setDeepestPath(path);
				return ret;
			}

			return null;
		}

		final ArrayList<TreeSpanData> treeList = new ArrayList<TreeSpanData>();
		for(final Direction d: Direction.values())
		{
			final Edge edge = p_current.getEdge(d);
			if(edge == null || !p_tree.contains(edge))
			{
				continue;
			}

			final Node other = edge.getOtherNode(p_current);
			if(other == p_parent)
			{
				continue;
			}

			final TreeSpanData datum = constructTreeSpanData(p_current, other,
				p_tree, p_onlyEdgeNodes);
			if(datum != null)
			{
				treeList.add(datum);
			}
		}

		if(treeList.size() == 0)
		{
			return null;
		}

		Path deepestPath = null;
		Path secondDeepestPath = null;
		Path longestPath = null;
		for(final TreeSpanData datum: treeList)
		{
			if(deepestPath == null
				|| (datum.getDeepestPath() != null && datum.getDeepestPath().length() > deepestPath.length()))
			{
				secondDeepestPath = deepestPath;
				deepestPath = datum.getDeepestPath();
			}
			else if(secondDeepestPath == null
				|| (datum.getDeepestPath() != null && datum.getDeepestPath().length() > secondDeepestPath.length()))
			{
				secondDeepestPath = datum.getDeepestPath();
			}

			if(longestPath == null
				|| (datum.getLongestPath() != null && datum.getLongestPath().length() > longestPath.length()))
			{
				longestPath = datum.getLongestPath();
			}
		}

		final TreeSpanData ret = new TreeSpanData();
		final int oldLongestPathLen = longestPath.length();

		if(secondDeepestPath == null)
		{
			final int newLongestPathLen = deepestPath.length() + 1;
			if(oldLongestPathLen > newLongestPathLen)
			{
				ret.setLongestPath(longestPath);
			}
			else
			{
				final Path newLongestPath = new Path(p_tree, p_current);
				newLongestPath.addToEnd(deepestPath);
				ret.setLongestPath(newLongestPath);
			}
		}
		else
		{
			final int newLongestPathLen = deepestPath.length()
				+ secondDeepestPath.length() + 2;
			if(oldLongestPathLen > newLongestPathLen)
			{
				ret.setLongestPath(longestPath);
			}
			else
			{
				final Path newLongestPath = new Path(p_tree, p_current);
				newLongestPath.addToEnd(deepestPath);
				secondDeepestPath.reverseDirection();
				newLongestPath.addToBeginning(secondDeepestPath);
				ret.setLongestPath(newLongestPath);
			}
		}

		deepestPath.addToBeginning(p_current);
		ret.setDeepestPath(deepestPath);

		return ret;
	}

	public static Path findLongestPath(final RectangleGraph p_tree,
		final boolean p_onlyEdgeNodes)
	{
		final Node root = p_tree.getNodes()[0];
		final TreeSpanData data = constructTreeSpanData(null, root, p_tree,
			p_onlyEdgeNodes);

		return data.getLongestPath();
	}
}
