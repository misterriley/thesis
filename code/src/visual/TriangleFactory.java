/**
 * 
 */
package visual;

import java.util.Random;


/**
 * @author Ringo
 * 
 */
public class TriangleFactory
{
	private static Random		m_random			= new Random();
	private static final double	MIN_ANGLE_DIFF_DEG	= 80;
	private static final double	MIN_ANGLE_DIFF_RAD	= MIN_ANGLE_DIFF_DEG
														* Math.PI / 180;

	private static boolean anglesAreTooClose(final double p_angle1,
		final double p_angle2)
	{
		final double diff = Math.abs(p_angle1 - p_angle2);
		if(diff < MIN_ANGLE_DIFF_RAD)
		{
			return true;
		}

		if(Math.PI * 2 - diff < MIN_ANGLE_DIFF_RAD)
		{
			return true;
		}

		return false;
	}

	public static VisualTaskTriangle createTriangle(final int p_radius)
	{
		final double firstAngle = randomAngle();
		double secondAngle = 0;
		double thirdAngle = 0;

		do
		{
			secondAngle = randomAngle();
		}
		while(anglesAreTooClose(firstAngle, secondAngle));

		do
		{
			thirdAngle = randomAngle();
		}
		while(anglesAreTooClose(firstAngle, thirdAngle)
			|| anglesAreTooClose(secondAngle, thirdAngle));

		return new VisualTaskTriangle(p_radius, firstAngle, secondAngle,
			thirdAngle);
	}

	private static double randomAngle()
	{
		return m_random.nextDouble() * 2 * Math.PI;
	}
}
