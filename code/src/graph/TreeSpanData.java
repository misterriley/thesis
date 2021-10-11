/**
 * 
 */
package graph;

/**
 * @author Ringo
 * 
 */
public class TreeSpanData
{
	private Path	m_deepestPath;
	private Path	m_longestPath;

	public Path getDeepestPath()
	{
		return m_deepestPath;
	}

	public Path getLongestPath()
	{
		return m_longestPath;
	}

	public void setDeepestPath(final Path p_deepestPath)
	{
		m_deepestPath = p_deepestPath;
	}

	public void setLongestPath(final Path p_longestPath)
	{
		m_longestPath = p_longestPath;
	}
}
