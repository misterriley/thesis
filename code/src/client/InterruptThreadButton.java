/**
 * 
 */
package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * @author Ringo
 * 
 */
public class InterruptThreadButton extends JButton
	implements ActionListener
{
	private static final long	serialVersionUID	= -4454044141749748140L;
	private final Thread		m_thread;

	public InterruptThreadButton(final String p_string, final Thread p_thread)
	{
		super(p_string);
		m_thread = p_thread;
		addActionListener(this);
	}

	public void actionPerformed(final ActionEvent p_e)
	{
		m_thread.interrupt();
	}
}
