/**
 * 
 */
package client;


import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Utilities
{
	public static void chooseAndSaveFile(final URL p_toSave,
		final Component p_parent)
	{
		final ClientFileChooser chooser = new ClientFileChooser();
		chooser.setSelectedFile(new File(p_toSave.getFile()));
		final FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"HTML Files", "html");
		chooser.setFileFilter(filter);
		final int choice = chooser.showSaveDialog(p_parent);
		if(choice == JFileChooser.APPROVE_OPTION)
		{
			final File saveFile = chooser.getSelectedFile();
			FileWriter fileWriter = null;
			InputStreamReader resourceReader = null;

			try
			{
				resourceReader = new InputStreamReader(
					p_toSave.openStream());
				fileWriter = new FileWriter(saveFile);
			}
			catch(final IOException ex)
			{
				showBadFileIO(p_parent);
				return;
			}

			final BufferedReader reader = new BufferedReader(resourceReader);
			final BufferedWriter writer = new BufferedWriter(fileWriter);
			try
			{
				while(true)
				{
					final String line = reader.readLine();
					if(line == null)
					{
						break;
					}
					writer.write(line);
					writer.newLine();
				}

				writer.flush();
				writer.close();
				reader.close();
			}
			catch(final IOException ex)
			{
				showBadFileIO(p_parent);
				return;
			}

			JOptionPane.showMessageDialog(chooser, "File saved.");
		}
	}

	public static double clamp(final double p_value, final double p_max,
		final double p_min)
	{
		final double max = Math.max(p_min, p_max);
		final double min = Math.min(p_min, p_max);

		if(p_value > max)
		{
			return max;
		}

		if(p_value < min)
		{
			return min;
		}

		return p_value;
	}

	public static void close(final Object p_object)
	{
		if(p_object == null)
		{
			return;
		}

		if(p_object instanceof Reader)
		{
			try
			{
				((Reader)p_object).close();
			}
			catch(final IOException ex)
			{
				ex.printStackTrace();
			}
		}
		else if(p_object instanceof Writer)
		{
			try
			{
				((Writer)p_object).close();
			}
			catch(final IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public static void drawStringInBox(final Graphics g, final int xPos,
		final int yPos,
		final String out, final boolean drawBox, final int wordBoxPad,
		final Color boxColor)
	{
		final int width = g.getFontMetrics().stringWidth(out);

		if(drawBox)
		{
			drawWordBox(g, xPos, yPos, width, wordBoxPad, boxColor);
		}

		g.setColor(Color.BLACK);
		g.drawString(out, xPos, yPos);
	}

	private static void drawWordBox(final Graphics g, final int xPos,
		final int yPos, final int width, final int p_wordBoxPad,
		final Color p_color)
	{
		g.setColor(p_color);
		final int boxHeight = g.getFontMetrics().getAscent()
			+ g.getFontMetrics().getDescent() + 2 * p_wordBoxPad;
		g.fillRoundRect(xPos - p_wordBoxPad, yPos - p_wordBoxPad
			- g.getFontMetrics().getAscent(), width + 2 * p_wordBoxPad,
			boxHeight,
			p_wordBoxPad, p_wordBoxPad);
	}

	public static String getDevLocation(final String p_resource)
	{
		return "/src" + p_resource;
	}

	public static String getTaskNameString(final GameType p_taskType)
	{
		switch(p_taskType)
		{
			case KINESTHETIC:
				return Constants.K_TASK;
			case VERBAL:
				return Constants.VERBAL_TASK;
			case VISUAL:
				return Constants.VISUAL_TASK;
		}

		throw new RuntimeException();
	}

	public static void main(final String[] p_args)
	{
		printCerts();
	}

	public static void printCerts()
	{
		try
		{
			// Load the JDK's cacerts keystore file
			final String filename = System.getProperty("java.home")
				+ "/lib/security/cacerts".replace('/', File.separatorChar);
			final FileInputStream is = new FileInputStream(filename);
			final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			final String password = "changeit";
			keystore.load(is, password.toCharArray());

			// This class retrieves the most-trusted CAs from the keystore
			final PKIXParameters params = new PKIXParameters(keystore);

			// Get the set of trust anchors, which contain the most-trusted CA
			// certificates
			final Iterator<TrustAnchor> it = params.getTrustAnchors().iterator();
			while(it.hasNext())
			{
				final TrustAnchor ta = it.next();
				// Get certificate
				final X509Certificate cert = ta.getTrustedCert();
				System.out.println(cert.getSubjectDN().getName());
			}
		}
		catch(final CertificateException e)
		{}
		catch(final KeyStoreException e)
		{}
		catch(final NoSuchAlgorithmException e)
		{}
		catch(final InvalidAlgorithmParameterException e)
		{}
		catch(final IOException e)
		{}
	}

	public static void showBadFileIO(final Component p_parent)
	{
		JOptionPane.showMessageDialog(p_parent, "File could not be saved.");
	}

	public static void showException(final Exception ex)
	{
		JOptionPane.showMessageDialog(null, ex);

		for(int i = 0; i < 5 && i < ex.getStackTrace().length; i++)
		{
			JOptionPane.showMessageDialog(null, ex.getStackTrace()[i]);
		}
	}

	public Point approximateExitPoint(final Point p_outOfBounds,
		final Point p_lastInBounds, final int p_maxX, final int p_maxY)
	{
		final double deltaX = p_outOfBounds.getX() - p_lastInBounds.getX();
		final double deltaY = p_outOfBounds.getY() - p_lastInBounds.getY();
		int exitX = 0;
		int exitY = 0;

		final boolean outRight = p_outOfBounds.getX() > p_maxX;
		final boolean outLeft = p_outOfBounds.getX() < 0;
		final boolean outUp = p_outOfBounds.getY() < 0;
		final boolean outDown = p_outOfBounds.getY() > p_maxY;

		if(outRight)
		{
			if(outUp)
			{
				return new Point(p_maxX, 0);
			}
			else if(outDown)
			{
				return new Point(p_maxX, p_maxY);
			}
			else
			{
				exitX = p_maxX;
			}
		}
		else if(outLeft)
		{
			if(outUp)
			{
				return new Point(0, 0);
			}
			else if(outDown)
			{
				return new Point(0, p_maxY);
			}
			else
			{
				exitX = 0;
			}
		}

		if(outRight || outLeft)
		{
			return new Point(exitX, (int)(deltaY / deltaX
				* (exitX - p_outOfBounds.getX()) + p_outOfBounds.getY()));
		}

		if(outUp)
		{
			exitY = 0;
		}
		else if(outDown)
		{
			exitY = p_maxY;
		}

		return new Point(
			(int)(deltaX / deltaY * (exitY - p_outOfBounds.getY()) + p_outOfBounds.getX()),
			exitY);
	}
}
