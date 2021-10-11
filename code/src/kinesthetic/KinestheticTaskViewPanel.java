/**
 * 
 */
package kinesthetic;

import games.GameModel;
import games.GameViewPanel;
import graph.Edge;
import graph.FullPathFinder;
import graph.Node;
import graph.RectangleGraph;
import graph.RectangleMaze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collection;

import javax.swing.event.ChangeEvent;

import client.Constants;
import client.Direction;

/**
 * @author Ringo
 * 
 */
public class KinestheticTaskViewPanel extends GameViewPanel
	implements BallMovementListener
{
	private static final class RepaintRunnable
		implements Runnable
	{
		private final KinestheticTaskViewPanel	m_panel;

		public RepaintRunnable(final KinestheticTaskViewPanel p_panel)
		{
			m_panel = p_panel;
		}

		public void run()
		{
			try
			{
				Thread.sleep(LOADING_PAUSE);
			}
			catch(final InterruptedException ex)
			{}
			m_panel.repaint();
		}
	}

	private static final Color	LIGHT_BLUE			= new Color(
														220,
														220,
														255);

	private static final int	LOADING_PAUSE		= 200;

	private static final long	serialVersionUID	= -6086060553208007941L;
	private RectangleMaze		m_maze;

	private boolean				m_taskIsActive;

	private Ball				m_ball;
	private long				m_lastCollisionMillis;
	private Collection<Edge>	m_lastCollisionEdges;
	private boolean				m_drawCollisionEdgesRed;
	private boolean				m_success;

	public KinestheticTaskViewPanel(final GameModel p_model)
	{
		super(p_model);
	}

	private boolean ballIsInEndSquare()
	{
		if(!m_maze.isInEndBounds(m_ball.getPosition(), m_ball.getRadius()))
		{
			return false;
		}

		for(final Direction d: Direction.values())
		{
			if(!m_maze.isInEndBounds(m_ball.getEdgePoint(d), 0))
			{
				return false;
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kinesthetic.BallMovementListener#ballMoved(kinesthetic.Ball)
	 */
	public void ballMoved(final Ball p_source)
	{
		doCheckAfterMovement();
		repaint();
	}

	private void checkForCollisionBlink()
	{
		if(m_lastCollisionMillis == 0)
		{
			return;
		}

		final long lastCollisionEpoch = System.currentTimeMillis()
			- m_lastCollisionMillis;
		final int blinkCount = (int)(lastCollisionEpoch / Constants.COLLISION_BLINK_PERIOD_MILLIS);
		m_drawCollisionEdgesRed = (blinkCount % 2 == 0 && blinkCount / 2 < Constants.NUM_COLLISION_BLINKS);
	}

	@Override
	protected boolean displayFeedback()
	{
		return false;
	}

	private void doCheckAfterMovement()
	{
		if(ballIsInEndSquare())
		{
			// success!
			m_success = true;
			endTask();
		}
		else
		{
			final Collection<Edge> collisions = m_maze.getCollidingEdges(
				m_ball.getPosition(), m_ball.getRadius());

			if(collisions != null)
			{
				registerCollisions(collisions);
			}
		}
	}

	private void drawBall(final Graphics g)
	{
		final int radius = m_ball.getRadius();
		if(m_ball.getPosition() != null)
		{
			final int xPos = (int)m_ball.getPosition().getX() - radius;
			final int yPos = (int)m_ball.getPosition().getY() - radius;

			g.setColor(Color.BLACK);
			g.fillOval(xPos, yPos, 2 * radius, 2 * radius);
		}
	}

	private void drawBoundarySquare(final Graphics g, final Color p_color,
		final Node node, final boolean p_drawCircle,
		final boolean p_writeSuccess)
	{
		final int xLoc = m_maze.getXCoordinate(node.getXIndex());
		final int yLoc = m_maze.getYCoordinate(node.getYIndex());
		final int width = m_maze.getCellWidth(node.getXIndex());
		final int height = m_maze.getCellHeight(node.getYIndex());
		g.setColor(p_color);

		if(p_drawCircle)
		{
			final int radius = m_model.getCurrentDifficulty();
			final Point position = getBallStartPosition();
			g.fillOval((int)position.getX() - radius, (int)position.getY()
				- radius,
				2 * radius, 2 * radius);
		}
		else
		{
			g.fillRect(xLoc, yLoc, width, height);
		}

		if(p_writeSuccess)
		{
			g.setColor(Color.BLACK);
			final String out = "YAY!";
			final int outWidth = g.getFontMetrics().stringWidth(out);
			final int x = xLoc + (width - outWidth) / 2;
			final int y = yLoc + (height + g.getFontMetrics().getAscent()) / 2;
			g.drawString(out, x, y);
		}
	}

	private void drawEndSquare(final Graphics g)
	{
		if(m_maze != null)
		{
			drawBoundarySquare(g, LIGHT_BLUE,
				m_maze.getEndSquare(),
				false, m_success);
		}
	}

	private void drawGraph(final Graphics g)
	{
		if(m_maze == null)
		{
			return;
		}

		g.setColor(Color.BLACK);
		final Edge[] edges = m_maze.getEdges();
		for(final Edge edge: edges)
		{
			if(m_drawCollisionEdgesRed && m_lastCollisionEdges.contains(edge))
			{
				g.setColor(Color.RED);

				final int[] bounds = getBlinkingEdgeBounds(edge);
				g.fillRect(bounds[0], bounds[1], bounds[2] - bounds[0] + 1,
					bounds[3] - bounds[1] + 1);

				g.setColor(Color.BLACK);
			}
			else
			{
				final Node nodeA = edge.getNodeA();
				final Node nodeB = edge.getNodeB();

				final int xA = m_maze.getXCoordinate(nodeA.getXIndex());
				final int xB = m_maze.getXCoordinate(nodeB.getXIndex());
				final int yA = m_maze.getYCoordinate(nodeA.getYIndex());
				final int yB = m_maze.getYCoordinate(nodeB.getYIndex());

				g.drawLine(xA, yA, xB, yB);
			}
		}
	}

	private void drawStartSquare(final Graphics g)
	{
		if(m_maze != null)
		{
			drawBoundarySquare(g, new Color(160, 255, 160),
				m_maze.getStartNode(), true, false);
		}
	}

	@Override
	public void enableNeutralDisplay()
	{

	}

	private void endTask()
	{
		setTaskIsActive(false);
		setBall(null);
		repaint();
	}

	public void generateNewGraph(final int p_cellsWide, final int p_cellsHigh)
	{
		boolean alterSlider = false;
		if(m_maze == null
			|| m_maze.getTemplate().getNumNodesWide() != p_cellsWide
			|| m_maze.getTemplate().getNumNodesTall() != p_cellsHigh)
		{
			alterSlider = true;
		}

		setDisplayString("Building maze ...");
		m_success = false;
		new Thread(new RepaintRunnable(this)).start();
		final Node lastStart = m_maze == null ? null : m_maze.getBeginning();
		final RectangleGraph template = FullPathFinder.constructFullPath(
			p_cellsWide, p_cellsHigh,
			Constants.AREA_WIDTH - 1, Constants.AREA_HEIGHT - 1, lastStart);
		m_maze = new RectangleMaze(template, Constants.AREA_WIDTH - 1,
			Constants.AREA_HEIGHT - 1);

		if(alterSlider)
		{
			final int diameter = m_maze.getMaxX()
				/ (m_maze.getTemplate().getNumNodesWide());
			final int newMax = diameter / 2;
			final int oldMax = m_model.getMaximumDifficulty();
			final int oldValue = m_model.getCurrentDifficulty();
			final int newValue = newMax * oldValue / oldMax;

			m_model.setMaximumDifficulty(newMax);
			m_model.setDifficulty(newValue);
		}

		setDisplayString(null);
		setBall(null);
		repaint();
	}

	public Ball getBall()
	{
		return m_ball;
	}

	private Point getBallStartPosition()
	{
		final int x = (m_maze.getXCoordinate(m_maze.getStartNode().getXIndex()) + m_maze.getXCoordinate(m_maze.getStartNode().getXIndex() + 1)) / 2;
		final int y = (m_maze.getYCoordinate(m_maze.getStartNode().getYIndex()) + m_maze.getYCoordinate(m_maze.getStartNode().getYIndex() + 1)) / 2;
		return new Point(x, y);
	}

	private int[] getBlinkingEdgeBounds(final Edge p_edge)
	{
		final Node nodeA = p_edge.getNodeA();
		final Node nodeB = p_edge.getNodeB();

		final int xA = m_maze.getXCoordinate(nodeA.getXIndex());
		final int xB = m_maze.getXCoordinate(nodeB.getXIndex());
		final int yA = m_maze.getYCoordinate(nodeA.getYIndex());
		final int yB = m_maze.getYCoordinate(nodeB.getYIndex());

		final int[] ret = new int[4];

		ret[0] = Math.min(xA, xB) - 1;
		ret[1] = Math.min(yA, yB) - 1;
		ret[2] = Math.max(xA, xB) + 1;
		ret[3] = Math.max(yA, yB) + 1;

		return ret;
	}

	public synchronized boolean isTaskActive()
	{
		return m_taskIsActive;
	}

	public boolean lastTrialSuccessful()
	{
		return m_success;
	}

	@Override
	public void paintThisComponent(final Graphics g)
	{
		checkForCollisionBlink();

		drawStartSquare(g);
		drawEndSquare(g);
		drawGraph(g);

		if(isTaskActive())
		{
			synchronized(this)
			{
				if(getBall() != null)
				{
					drawBall(g);
				}
			}
		}
	}

	private void registerCollisions(final Collection<Edge> collisions)
	{
		repaintBlinkingEdges();

		m_lastCollisionEdges = collisions;
		m_lastCollisionMillis = System.currentTimeMillis();
		endTask();
		m_model.getKinestheticController().triggerRepaintEdgeCalls();
	}

	public void repaintBlinkingEdges()
	{
		if(m_lastCollisionEdges != null)
		{
			for(final Edge edge: m_lastCollisionEdges)
			{
				final int[] bounds = getBlinkingEdgeBounds(edge);

				repaint(bounds[0], bounds[1], bounds[2] - bounds[0] + 1,
					bounds[3] - bounds[1] + 1);
			}
		}
	}

	private void setBall(final Ball p_ball)
	{
		synchronized(this)
		{
			m_ball = p_ball;
		}
		repaint();
	}

	private synchronized void setTaskIsActive(final boolean p_taskIsActive)
	{
		m_taskIsActive = p_taskIsActive;
	}

	public void startTask()
	{
		m_success = false;
		setTaskIsActive(true);
		setBall(new Ball());
		m_ball.addListener(this);

		m_ball.setPosition(getBallStartPosition());
		m_ball.setRadius(m_model.getCurrentDifficulty());

		requestFocus();
		repaint();
	}

	@Override
	public void stateChanged(final ChangeEvent p_e)
	{
		if(m_ball != null)
		{
			m_ball.setRadius(m_model.getCurrentDifficulty());
		}
		super.stateChanged(p_e);
		requestFocus();
	}
}
