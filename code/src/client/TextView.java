/**
 * 
 */
package client;

import games.GameModel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;

/**
 * @author Ringo
 * 
 */
public abstract class TextView
{
	private class TVKeyListener
		implements KeyListener
	{
		private final JScrollPane	m_scrollPane;

		public TVKeyListener(final JScrollPane p_scrollPane)
		{
			m_scrollPane = p_scrollPane;
		}

		public void keyPressed(final KeyEvent p_arg0)
		{
			final JScrollBar scrollBar = m_scrollPane.getVerticalScrollBar();
			final int value = scrollBar.getValue();
			int newValue = value;

			switch(p_arg0.getKeyCode())
			{
				case KeyEvent.VK_UP:
					newValue = Math.max(value - SCROLL_SPEED, 0);
					break;
				case KeyEvent.VK_DOWN:
					newValue = Math.min(value + SCROLL_SPEED,
						scrollBar.getMaximum());
					break;
			}

			if(newValue != value)
			{
				scrollBar.setValue(newValue);
			}
		}

		public void keyReleased(final KeyEvent p_arg0)
		{

		}

		public void keyTyped(final KeyEvent p_arg0)
		{

		}

	}

	private static final int	SCROLL_SPEED	= 5;

	protected final GameModel	m_model;
	private final String		m_fileName;
	protected final URL			m_toDisplay;
	private JEditorPane			m_editorPane;

	/**
	 * @param p_model
	 * @param p_fileToDisplay
	 */
	public TextView(final GameModel p_model, final String p_fileToDisplay)
	{
		m_model = p_model;
		m_toDisplay = p_fileToDisplay == null ? null : MainController.class.getResource(p_fileToDisplay);
		m_fileName = p_fileToDisplay == null ? null : new File(p_fileToDisplay).getName();
	}

	public abstract JPanel getButtonPanel();

	public abstract JButton getDefaultButton();

	public abstract JPanel getNorthPanel();

	public String getText()
	{
		return m_editorPane.getText();
	}

	public void setText(final String p_text)
	{
		m_editorPane.setText(p_text);
	}

	private void setTitle()
	{
		final String tag = m_fileName == null ? "" : " - "
			+ m_fileName;
		m_model.getMainFrame().setTitle(Constants.TITLE + tag);
	}

	public void showText(final boolean p_isHTML, final boolean p_isEditable)
	{
		final JFrame mainFrame = m_model.getMainFrame();
		mainFrame.setVisible(false);
		final Container contentPane = mainFrame.getContentPane();
		m_editorPane = new JEditorPane();
		final JScrollPane scrollPane = new JScrollPane(m_editorPane);
		final JPanel buttonPanel = getButtonPanel();

		contentPane.removeAll();

		scrollPane.setPreferredSize(Constants.AREA_DIMENSION);

		if(p_isHTML)
		{
			m_editorPane.setEditorKit(new HTMLEditorKit());
		}

		if(m_toDisplay != null)
		{
			try
			{
				m_editorPane.setPage(m_toDisplay);
			}
			catch(final IOException ex)
			{
				Utilities.showException(ex);
				return;
			}
		}

		m_editorPane.setEditable(p_isEditable);

		contentPane.setLayout(new BorderLayout());
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		final JPanel northPanel = getNorthPanel();
		if(northPanel != null)
		{
			contentPane.add(northPanel, BorderLayout.NORTH);
		}

		mainFrame.setTitle(Constants.TITLE);
		mainFrame.getRootPane().setDefaultButton(getDefaultButton());
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);

		contentPane.addKeyListener(new TVKeyListener(scrollPane));
		contentPane.requestFocus();
		setTitle();

		mainFrame.setVisible(true);

		MainController.waitForInterrupt();
	}
}
