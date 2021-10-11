/**
 * 
 */
package verbal;

import games.GameModel;
import games.GameViewPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import client.Constants;
import client.Utilities;

/**
 * @author Ringo
 * 
 */
public class VerbalTaskViewPanel extends GameViewPanel
{
	static final long				serialVersionUID	= -1337995572575290971L;

	private VerbalTaskMode			m_currentMode;
	private VerbalTaskDisplayList	m_displayList;
	private int						m_dividerHeight;
	private int						m_secondsLeft;
	private boolean					m_displayTimer;

	public VerbalTaskViewPanel(final GameModel p_model)
	{
		super(p_model);
	}

	@Override
	protected boolean displayFeedback()
	{
		return true;
	}

	private synchronized void drawTargetWord(final Graphics g)
	{
		if(m_displayList != null)
		{
			g.setColor(Color.BLACK);
			final String out = "Target word: ";
			final int xPos = Constants.BORDER_IN_PIXELS;
			final int yPos = Constants.BORDER_IN_PIXELS
				+ (g.getFontMetrics().getAscent()
				- g.getFontMetrics().getDescent());
			g.drawString(out, xPos, yPos);

			g.setColor(Constants.LAVENDER);

			final int width = g.getFontMetrics().stringWidth(out);
			g.drawString(m_displayList.getTargetWord(), xPos + width, yPos);
		}
	}

	private void drawTimer(final Graphics g)
	{
		if(m_currentMode == VerbalTaskMode.DISPLAY_WORDS && m_displayTimer)
		{
			final String out = "Timer: " + m_secondsLeft;
			final int width = g.getFontMetrics().stringWidth(out);
			final int xPos = getWidth() - width - Constants.BORDER_IN_PIXELS;
			final int yPos = getHeight() - Constants.BORDER_IN_PIXELS;
			Utilities.drawStringInBox(g, xPos, yPos, out, true,
				Constants.WORD_BOX_PAD,
				Constants.LAVENDER);
		}
	}

	public synchronized void drawWordList(final Graphics g)
	{
		if(m_displayList != null)
		{
			final int ascent = g.getFontMetrics().getAscent();
			final int descent = g.getFontMetrics().getDescent();
			final int firstYOffset = m_dividerHeight
				+ Constants.BORDER_IN_PIXELS
				+ ascent - descent;
			final int betweenYOffset = Constants.BORDER_IN_PIXELS + ascent;
			int xPos = Constants.BORDER_IN_PIXELS;
			int yPos = firstYOffset;

			final ArrayList<VerbalTaskDisplayListEntry> entries = m_displayList.getEntries();

			int maxWidth = 0;
			for(int i = 0; i < entries.size(); i++)
			{
				final String out = entries.get(i).getString();
				final boolean drawBox = m_currentMode == VerbalTaskMode.DISPLAY_ANSWERS
					&& entries.get(i).rhymesWithTarget();
				Utilities.drawStringInBox(g, xPos, yPos, out, drawBox,
					Constants.WORD_BOX_PAD, Constants.LAVENDER);

				maxWidth = Math.max(maxWidth, g.getFontMetrics().stringWidth(
					out));

				yPos += betweenYOffset;
				if(yPos >= getHeight())
				{
					xPos += (Constants.BORDER_IN_PIXELS + maxWidth);
					maxWidth = 0;
					yPos = firstYOffset;
				}
			}
		}
	}

	@Override
	public void enableNeutralDisplay()
	{
		m_displayTimer = false;
		setDisplayList(null);
	}

	public synchronized VerbalTaskDisplayList getDisplayList()
	{
		return m_displayList;
	}

	@Override
	public void paintThisComponent(final Graphics g)
	{
		g.setColor(Color.BLACK);
		m_dividerHeight = 2 * Constants.BORDER_IN_PIXELS
			+ g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent();
		setPositiveFeedbackOffset(m_dividerHeight);
		g.drawLine(0, m_dividerHeight, Constants.AREA_WIDTH, m_dividerHeight);

		drawTargetWord(g);
		drawWordList(g);
		drawTimer(g);
	}

	public synchronized void setDisplayList(
		final VerbalTaskDisplayList p_displayList)
	{
		m_displayList = p_displayList;
		repaint();
	}

	public void setDisplayTimer(final boolean p_displayTimer)
	{
		m_displayTimer = p_displayTimer;
	}

	public void setSecondsLeft(final int p_secondsLeft)
	{
		m_secondsLeft = p_secondsLeft;
		repaint();
	}

	public void setTaskMode(final VerbalTaskMode p_mode)
	{
		m_currentMode = p_mode;
		repaint();
	}
}
