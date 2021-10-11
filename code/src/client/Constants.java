/**
 * 
 */
package client;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * @author Ringo
 * 
 */
public class Constants
{
	public static final int			MINIMUM_AGE						= 18;
	public static final int			AREA_WIDTH						= 700;
	public static final int			AREA_HEIGHT						= 350;
	public static final Dimension	AREA_DIMENSION					= new Dimension(
																		AREA_WIDTH,
																		AREA_HEIGHT);
	public static final Font		FONT							= new Font(
																		"Arial",
																		Font.PLAIN,
																		20);
	public static final int			BORDER_IN_PIXELS				= 20;
	public static final Color		LAVENDER						= new Color(
																		160,
																		160,
																		255);
	public static final int			WORD_BOX_PAD					= 5;
	public static final int			NUM_TASKS_PER_AUTO_SESSION		= 15;
	public static final int			NUM_TASKS_PER_TEST_SESSION		= 10;
	public static final int			BUTTON_HEIGHT					= 40;
	public static final int			BUTTON_ROW_WIDTH				= 120;
	public static final int			K_TASK_CELLS_WIDE				= 6;
	public static final int			K_TASK_DEFAULT_BALL_RADIUS		= 10;
	public static final int			K_TASK_BALL_SPEED				= 300;
	public static final int			NUM_COLLISION_BLINKS			= 3;
	public static final int			COLLISION_BLINK_PERIOD_MILLIS	= 500;
	public static final GameType	TEST_TYPE						= null;
	public static final int			PREFERRED_TCP_WIDTH				= BUTTON_ROW_WIDTH
																		+ 2
																		* Constants.BORDER_IN_PIXELS;
	public static final String		VERBAL_TASK_TIME_UP_PROMPT		= "Time's up.  Please choose from the panel on the right.";
	public static final String		MID_TASK_DIFF_QUESTION			= "What's the most fun difficulty level?";
	public static final String		END_TASK_DIFF_QUESTION			= "You're done.  But if you did this again, what difficulty level would you want?";
	public static final String		VERBAL_TASK						= "Verbal Task";
	public static final String		VISUAL_TASK						= "Visual Task";
	public static final String		K_TASK							= "Kinesthetic Task";
	public static final String		TITLE							= "NDNU Video Game Study";
	public static final String		SHORT_TITLE						= "NDNU Study";
	public static final String		VERBAL_DATA_FILE				= "/verbal/VerbalTaskWordList.txt";
	public static final String		DESCRIPTION_FILE				= "/client/Description.html";
	public static final String		INFORMED_CONSENT_FILE			= "/client/InformedConsent.html";
	public static final String		DEBRIEFING_FILE					= "/client/DebriefingStatement.html";
	public static final String		TRANSMISSION_FAIL_FILE			= "/client/TransmissionFail.html";
	public static final String		POST_URL						= "http://www.ndnuvideogamestudy.com/cgi-bin/data/version2/";
	public static final String		POST_FILE						= "postcatcher.php";
	public static final int			GOOD_STATUS_CODE				= 200;

	public static final double		PRACTICE_TARGET_WIN_PCT			= .75;
	public static final boolean		RUN_TUTORIAL					= true;

	public static final String[]	GENDERS							= {
																	"Female",
																	"Male",
			"Transgendered: Female to Male",
			"Transgendered: Male to Female"							};

	public static int				NO_GENDER_DATA					= -1;
	public static final int[]		GENDER_INDICATORS				= {1,
																	2,
																	3,
																	4};
	public static final String[]	INCOMES							= {
			"0 - 25,000",
			"25,000- 50,000",
			"50,000-75,000",
			"75,000-100,000",
			"100,000- and up"										};

	public static int				NO_INCOME_DATA					= -1;
	public static final int[]		INCOME_INDICATORS				= {1,
																	2,
																	3,
																	4,
																	5};

	public static final String[]	RACES							= {
			"African American/Black",
			"Asian American",
			"Caucasian/White",
			"Latino/Latina",
			"Pacific Islander",
			"Other (Please specify)"								};

	public static int				NO_RACE_DATA					= -1;
	public static final int[]		RACE_INDICATORS					= {2,
																	3,
																	5,
																	7,
																	11,
																	13};

	public static double			NO_NEUROTICISM_DATA				= -1;
}