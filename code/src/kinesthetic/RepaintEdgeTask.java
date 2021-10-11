/**
 * 
 */
package kinesthetic;

import games.GameModel;

/**
 * @author Ringo
 * 
 */
public class RepaintEdgeTask
	implements Runnable
{
	private final GameModel	m_model;
	private final int		m_delayMillis;
	private final int		m_totalTimeMillis;

	public RepaintEdgeTask(final GameModel p_model, final int p_delayMillis,
		final int p_totalTimeMillis)
	{
		m_model = p_model;
		m_delayMillis = p_delayMillis;
		m_totalTimeMillis = p_totalTimeMillis;
	}

	public void run()
	{
		final long now = System.currentTimeMillis();
		while(System.currentTimeMillis() - now <= m_totalTimeMillis)
		{
			m_model.getKinestheticTVP().repaintBlinkingEdges();
			try
			{
				Thread.sleep(m_delayMillis);
			}
			catch(final InterruptedException ex)
			{}
		}
	}
}
