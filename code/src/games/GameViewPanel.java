/**
 * 
 */
package games;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import client.Constants;
import client.Utilities;

/**
 * @author Ringo
 * 
 */
public abstract class GameViewPanel extends JPanel
	implements ChangeListener
{
	private static final long		serialVersionUID	= 3438828251625973272L;

	private final GameKeyListener	m_keyListener;
	private String					m_displayString;
	protected final GameModel		m_model;
	private boolean					m_displayCorrect;
	private boolean					m_displayIncorrect;
	private int						m_positiveFeedbackOffset;

	public GameViewPanel(final GameModel p_model)
	{
		m_keyListener = new GameKeyListener(p_model, KeyListenerMode.TASK);
		addKeyListener(m_keyListener);
		addFocusListener(m_keyListener);
		p_model.setKeyListener(m_keyListener);
		setPreferredSize(new Dimension(Constants.AREA_WIDTH,
			Constants.AREA_HEIGHT));
		m_model = p_model;
		setFocusable(true);
	}

	public void clearDisplayString()
	{
		setDisplayString(null);
	}

	public final void clearFeedback()
	{
		m_displayCorrect = false;
		m_displayIncorrect = false;
	}

	protected abstract boolean displayFeedback();

	private void drawBackground(final Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Constants.AREA_WIDTH - 1, Constants.AREA_HEIGHT - 1);

		drawBoundary(g);
	}

	private void drawBoundary(final Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, Constants.AREA_WIDTH - 1, Constants.AREA_HEIGHT - 1);
	}

	private void drawFeedback(final Graphics g)
	{
		if(m_displayCorrect)
		{
			final String output = "Correct!";
			final int width = g.getFontMetrics().stringWidth(output);
			final int xLoc = getWidth() - width
				- Constants.BORDER_IN_PIXELS;
			final int yLoc = m_positiveFeedbackOffset
				+ Constants.BORDER_IN_PIXELS
				+ g.getFontMetrics().getAscent();

			Utilities.drawStringInBox(g, xLoc, yLoc, output, true,
				Constants.WORD_BOX_PAD, Color.GREEN);
		}

		if(m_displayIncorrect)
		{
			final String output = "Incorrect :(";
			final int width = g.getFontMetrics().stringWidth(output);
			final int xLoc = getWidth() - width
				- Constants.BORDER_IN_PIXELS;
			final int yLoc = getHeight()
				- Constants.BORDER_IN_PIXELS;

			Utilities.drawStringInBox(g, xLoc, yLoc, output, true,
				Constants.WORD_BOX_PAD, Color.RED);
		}
	}

	private void drawStringCentered(final Graphics g,
		final String p_string,
		final Component p_source)
	{
		final int width = g.getFontMetrics().stringWidth(
			p_string);
		final int xCoordinate = (p_source.getWidth() - width) / 2;
		g.drawString(p_string, xCoordinate, p_source.getHeight() / 2);
	}

	public abstract void enableNeutralDisplay();

	public GameKeyListener getKeyListener()
	{
		return m_keyListener;
	}

	@Override
	public void paintComponent(final Graphics g)
	{
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D)g).setFont(Constants.FONT);
		drawBackground(g);

		synchronized(this)
		{
			if(m_displayString != null)
			{
				drawStringCentered(g, m_displayString, this);
				return;
			}
		}

		paintThisComponent(g);
		drawFeedback(g);
	}

	public abstract void paintThisComponent(Graphics g);

	public void setDisplayString(final String p_displayString)
	{
		synchronized(this)
		{
			m_displayString = p_displayString;
		}
		repaint();
	}

	protected void setPositiveFeedbackOffset(final int p_pixels)
	{
		m_positiveFeedbackOffset = p_pixels;
	}

	public void showFeedback(final boolean p_success)
	{
		if(p_success)
		{
			m_displayCorrect = true;
		}
		else
		{
			m_displayIncorrect = true;
		}
		repaint();
	}

	public void stateChanged(final ChangeEvent p_e)
	{
		repaint();
	}
}
