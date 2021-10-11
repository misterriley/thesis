/**
 * 
 */
package verbal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import client.Constants;
import client.MainController;
import client.Randomizer;
import client.Utilities;

/**
 * @author Ringo
 * 
 */
public class VerbalTaskWordList
{
	private static final String				LINE_DELIMITER	= ",";
	private static final String				TOKEN_DELIMITER	= ":";

	private List<VerbalTaskWordListEntry>	m_entries;
	private int								m_enumIndex;

	public VerbalTaskWordList load()
	{
		m_entries = new ArrayList<VerbalTaskWordListEntry>();
		BufferedReader reader = null;
		try
		{
			final URL dataFile = MainController.class.getResource(Constants.VERBAL_DATA_FILE);
			reader = new BufferedReader(new InputStreamReader(
				dataFile.openStream()));
			while(true)
			{
				final String line = reader.readLine();
				if(line == null)
				{
					break;
				}

				final String[] parts = line.split(LINE_DELIMITER);

				final String targetWord = parts[0];
				final String[] rhymingLookalikes = parts[1].split(TOKEN_DELIMITER);
				final String[] rhymingNonLookalikes = parts[2].split(TOKEN_DELIMITER);
				final String[] eyeRhymes = parts[3].split(TOKEN_DELIMITER);
				final String[] noneOfTheAbove = parts[4].split(TOKEN_DELIMITER);

				final VerbalTaskWordListEntry vtwle = new VerbalTaskWordListEntry(
					targetWord, rhymingLookalikes, rhymingNonLookalikes,
					eyeRhymes, noneOfTheAbove);

				m_entries.add(vtwle);
			}
		}
		catch(final IOException ioex)
		{
			ioex.printStackTrace();
		}
		finally
		{
			Utilities.close(reader);
		}

		return this;
	}

	public VerbalTaskWordListEntry nextElement()
	{
		if(m_enumIndex == m_entries.size())
		{
			m_enumIndex = 0;
			shuffle();
		}

		return m_entries.get(m_enumIndex++);
	}

	public void shuffle()
	{
		final Randomizer r = new Randomizer();
		Collections.shuffle(m_entries);
		for(final VerbalTaskWordListEntry vltwe: m_entries)
		{
			Arrays.sort(vltwe.getEyeRhymes(), r);
			Arrays.sort(vltwe.getNoneOfTheAbove(), r);
			Arrays.sort(vltwe.getRhymingLookalikes(), r);
			Arrays.sort(vltwe.getRhymingNonLookalikes(), r);
		}

		m_enumIndex = 0;
	}
}
