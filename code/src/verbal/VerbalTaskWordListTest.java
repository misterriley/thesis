/**
 * 
 */
package verbal;

import games.GameModel;

import java.awt.FlowLayout;

import javax.swing.JFrame;

import client.GameType;
import client.MainController;


/**
 * @author Ringo
 * 
 */
public class VerbalTaskWordListTest
{
	public static void main(final String[] p_args)
	{
		final JFrame frame = new JFrame("Verbal sequence test");
		final GameModel model = new GameModel(frame);

		while(model.currentTaskType() != GameType.VERBAL)
		{
			model.incrementTaskIndex();
		}

		final VerbalTaskViewPanel vtvp = new VerbalTaskViewPanel(model);
		final VerbalTaskControlPanel vtcp = new VerbalTaskControlPanel(model);
		frame.setLayout(new FlowLayout());
		frame.getContentPane().add(vtvp);
		frame.getContentPane().add(vtcp);
		frame.pack();
		frame.setVisible(true);

		final VerbalTaskWordList vtwl = new VerbalTaskWordList().load();
		final VerbalTaskController vtc = new VerbalTaskController(model);

		while(true)
		{
			final VerbalTaskWordListEntry vtwle = vtwl.nextElement();
			final VerbalTaskDisplayList vtdl = vtc.constructDisplayList(vtwle,
				vtwle.totalWords());
			vtvp.setDisplayList(vtdl);

			MainController.waitForInterrupt();
		}
	}
}
