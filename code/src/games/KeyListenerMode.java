/**
 * 
 */
package games;

/**
 * @author Ringo
 * 
 */
public enum KeyListenerMode
{
	BUTTON_CYCLING, BUTTONS_AND_SLIDER, SLIDER, TASK, OFF;

	public boolean isButtonMode()
	{
		return this == BUTTON_CYCLING || this == BUTTONS_AND_SLIDER;
	}

	public boolean isSliderMode()
	{
		return this == BUTTONS_AND_SLIDER || this == SLIDER;
	}

	public boolean isUIMode()
	{
		return this == BUTTON_CYCLING || this == BUTTONS_AND_SLIDER
			|| this == SLIDER;
	}
}