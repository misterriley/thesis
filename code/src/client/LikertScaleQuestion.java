/**
 * 
 */
package client;

import games.GameModel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author Ringo
 * 
 */
public class LikertScaleQuestion
{
	private class LSQKeyListener
		implements KeyListener
	{
		public void keyPressed(final KeyEvent p_arg0)
		{
			final int index = getIndexOfSelection();
			int newIndex = index;

			switch(p_arg0.getKeyCode())
			{
				case KeyEvent.VK_RIGHT:
					newIndex = Math.min(index + 1,
						m_radioButtons.getButtonCount() - 1);
					break;
				case KeyEvent.VK_LEFT:
					newIndex = Math.max(index - 1, 0);
					break;
			}

			if(newIndex != index)
			{
				final Enumeration<AbstractButton> buttonEnum = m_radioButtons.getElements();
				for(int i = 0; i < newIndex; i++)
				{
					buttonEnum.nextElement();
				}
				m_radioButtons.clearSelection();
				m_radioButtons.setSelected(buttonEnum.nextElement().getModel(),
					true);
			}
		}

		public void keyReleased(final KeyEvent p_arg0)
		{

		}

		public void keyTyped(final KeyEvent p_arg0)
		{

		}

	}

	public static enum ScaleType
	{
		AGREE, LIKELY
	};

	public static final int			LSCALE_TYPE_AGREE	= 0;
	public static final int			LSCALE_TYPE_LIKELY	= 1;

	private static final String[]	AGREE_TYPE_LABELS	= {"Disagree strongly",
														"Disagree a little",
			"Neither agree nor disagree", "Agree a little", "Agree strongly"};

	private static final String[]	LIKELY_TYPE_LABELS	= {"Very unlikely",
														"Unlikely",
			"Equally likely as not",
			"Likely",
			"Very likely"
															};

	private static String[] getLabels(final ScaleType p_type)
	{
		switch(p_type)
		{
			case AGREE:
				return AGREE_TYPE_LABELS;
			case LIKELY:
				return LIKELY_TYPE_LABELS;
			default:
				throw new RuntimeException();
		}
	}

	public static void main(final String[] p_args)
	{
		final GameModel m = new GameModel(new JFrame());
		final LikertScaleQuestion lsq = new LikertScaleQuestion(m, "WHAT???",
			ScaleType.AGREE);
		lsq.getResponse();
		lsq.setPrompt("HUH???");
		lsq.getResponse();
	}

	private final JLabel	m_promptLabel;

	private final GameModel	m_model;

	private ButtonGroup		m_radioButtons;

	public LikertScaleQuestion(final GameModel p_model, final String p_prompt,
		final ScaleType p_type)
	{
		m_model = p_model;
		final JFrame mainFrame = p_model.getMainFrame();
		final Container contentPane = mainFrame.getContentPane();

		mainFrame.setVisible(false);
		contentPane.removeAll();
		contentPane.setLayout(new BorderLayout());

		addOKButton();
		m_promptLabel = new JLabel(p_prompt);
		final JPanel top = new JPanel();
		top.add(m_promptLabel);
		mainFrame.add(top, BorderLayout.NORTH);
		addRadioButtons(p_type);
		mainFrame.setTitle(Constants.TITLE);

		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		contentPane.addKeyListener(new LSQKeyListener());
		contentPane.requestFocus();
	}

	private void addOKButton()
	{
		final JPanel bottom = new JPanel();

		JButton OKButton = null;
		bottom.add(OKButton = new InterruptThreadButton("OK",
			Thread.currentThread()));

		m_model.getMainFrame().getContentPane().add(bottom, BorderLayout.SOUTH);
		m_model.getMainFrame().getRootPane().setDefaultButton(OKButton);
	}

	private void addRadioButtons(final ScaleType p_type)
	{
		final JPanel mid = new JPanel();
		final String[] labels = getLabels(p_type);

		m_radioButtons = new ButtonGroup();
		m_radioButtons.add(new JRadioButton(labels[0], true));
		for(int i = 1; i < labels.length; i++)
		{
			m_radioButtons.add(new JRadioButton(labels[i]));
		}
		final Enumeration<AbstractButton> groupEnum = m_radioButtons.getElements();
		while(groupEnum.hasMoreElements())
		{
			mid.add(groupEnum.nextElement());
		}
		m_model.getMainFrame().getContentPane().add(mid, BorderLayout.CENTER);
	}

	private int getIndexOfSelection()
	{
		final Enumeration<AbstractButton> groupEnum = m_radioButtons.getElements();
		for(int i = 0; groupEnum.hasMoreElements(); i++)
		{
			if(groupEnum.nextElement().isSelected())
			{
				return i;
			}
		}

		throw new RuntimeException();
	}

	public int getResponse()
	{
		m_model.getMainFrame().setVisible(false);
		m_model.getMainFrame().pack();
		m_model.getMainFrame().setVisible(true);
		MainController.waitForInterrupt();
		return getIndexOfSelection() + 1;
	}

	public void setPrompt(final String p_prompt)
	{
		m_promptLabel.setText(p_prompt);
	}
}