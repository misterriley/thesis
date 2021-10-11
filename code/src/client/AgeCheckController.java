/**
 * 
 */
package client;

import games.GameModel;

import java.util.Calendar;

import javax.swing.JOptionPane;

/**
 * @author Ringo
 * 
 */
public class AgeCheckController
{
	private final GameModel	m_model;

	public AgeCheckController(final GameModel p_model)
	{
		m_model = p_model;
	}

	public int doAgeCheck()
	{
		final Calendar birthday = new DatePicker(m_model,
			"Please enter date of birth:").pickDate();
		final int age = getAge(birthday);

		if(age < Constants.MINIMUM_AGE)
		{
			final String out = "Sorry, but you must be at least "
				+ Constants.MINIMUM_AGE + " to take part in this study.";
			JOptionPane.showMessageDialog(m_model.getMainFrame(), out);
			System.exit(0);
		}

		return age;
	}

	private int getAge(final Calendar p_birthday)
	{
		final Calendar today = Calendar.getInstance();

		int age = today.get(Calendar.YEAR) - p_birthday.get(Calendar.YEAR);
		if(today.get(Calendar.MONTH) < p_birthday.get(Calendar.MONTH))
		{
			age--;
		}
		else if(today.get(Calendar.MONTH) == p_birthday.get(Calendar.MONTH)
			&& today.get(Calendar.DAY_OF_MONTH) < p_birthday.get(Calendar.DAY_OF_MONTH))
		{
			age--;
		}

		return age;
	}
}