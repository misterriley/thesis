/**
 * 
 */
package client;

import games.GameModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JButton;

/**
 * @author Ringo
 * 
 */
public class SaveFileButton extends JButton
	implements ActionListener
{
	private static final long	serialVersionUID	= -2037037900108135870L;
	private final URL			m_fileToSave;
	private final GameModel		m_model;

	public SaveFileButton(final GameModel p_model, final URL p_fileToSave)
	{
		super("Save File");
		addActionListener(this);
		m_fileToSave = p_fileToSave;
		m_model = p_model;
	}

	public void actionPerformed(final ActionEvent p_e)
	{
		Utilities.chooseAndSaveFile(m_fileToSave, m_model.getMainFrame());
	}
}
