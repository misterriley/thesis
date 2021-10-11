/**
 * 
 */
package visual;

/**
 * @author Ringo
 * 
 */
public enum VisualTaskMode
{
	DRAW_TRIANGLES, DRAW_TRIANGLES_AND_CIRCLES, DRAW_FULL_SOLUTION,
	DRAW_CIRCLES;

	/**
	 * @return
	 */
	public boolean isTriangleDrawingMode()
	{
		// TODO Auto-generated method stub
		return this == DRAW_TRIANGLES || this == DRAW_TRIANGLES_AND_CIRCLES
			|| this == DRAW_FULL_SOLUTION;
	}
}
