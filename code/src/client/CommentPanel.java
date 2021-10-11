/**
 * 
 */
package client;

import games.GameModel;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Ringo
 * 
 */
public class CommentPanel extends DefaultTextView
{
	public CommentPanel(final GameModel p_model)
	{
		super(p_model, null, "Submit");
	}

	@Override
	public JPanel getNorthPanel()
	{
		final JPanel ret = new JPanel();
		ret.add(new JLabel(
			"Feel free to leave a comment with your results.  Thanks again."));
		return ret;
	}
}
