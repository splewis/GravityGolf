/**
 *  DataReader provides the functionality for the importing of level and setting data.
 *  
 *	Input angles are in DEGREES, all calculations within the program use RADIANS. 
 *  Data file input syntax:
 *		Moons must be below the body to which they are to be attached to.
 *		Custom colors may be defined using "Color(r#, g#, b#)".
 *			Acceptable colors: red, black, blue, cyan, gray, green, magenta, orange, pink, yellow, purple, violet
 *		Spaces do not matter in the file.
 *		Levels MUST be finished with the level() line.
 *		"Comments" may be put in the file; the line should start with a "//" if it is a comment.
 *
 *		ball(centerX, centerY, radius, color)
 *			OR ball(centerX, centerY) to make a ball with constant radius 3 and color red
 *		body(centerX, centerY, radius, color)
 *		body(centerX, centerY, radius, color, mass)
 *		refl(centerX, centerY, radius, color) <----  creates a "reflector" body
 *		moon(startingAngle, distanceFromBody, radius, color)
 *		warp(centerX, centerY)
 *		rect(centerX, centerY, xLength/2, yLength/2, color)
 *		rec2(leftX  , topY   , width    , height   , color)
 *		goal(centerX, centerY, radius)
 *		level(followFactor, gravityStrengthFactor)
 *			OR level(gravityStrengthFactor) - followFactor is set to 0
 *			OR level() - followFactor set to 0 and gravityStrengthFactor set to 1
 *
 *		followFactor and gravityStrengthFactor are the ONLY VALUES that may be DOUBLES.
 *			As followFactor DECREASES, the screen will follow the ball more (1 = always on center of the ball)
 *			As followFactor INCREEASES, the screen will follow the ball less
 *          If followFactor is 0, screen shifting is disabled
 *
 *
 */

package game;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import structures.*;

/**
 * @author Sean Sean Lewis
 */
public class DataReader {

	private PrintWriter pw;
	private boolean errorFound = false;

	/**
	 * @param mainFile
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Level> getLevelData(String mainFile) throws IOException {
		pw = new PrintWriter(new File("logs/datalog.txt"));
		ArrayList<Level> levels = new ArrayList<Level>();
		long startTime = System.currentTimeMillis();
		levels.addAll(readFile(new File(mainFile)));
		pw.println();
		pw.println("Done reading all data. Took "
				+ (System.currentTimeMillis() - startTime) + " ms.");
		pw.close();
		return levels;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public int[] getSettings() throws IOException {

		int[] settings = { 1, 0, 0, 0, 1, 0, 3 };
		try {
			Scanner infile = new Scanner(new File("settings.txt"));
			int i = 0;
			while (i < 6) {
				settings[i++] = (infile.nextLine().contains("yes")) ? (1) : (0);
			}
			String l = infile.nextLine().toLowerCase().replaceAll(" ", "");
			settings[6] = Integer.parseInt(l.substring(l.indexOf("=") + 1));
			infile.close();
		} catch (Exception e) {

			PrintWriter settingsWriter = new PrintWriter(new File(
					"settings.txt"));
			settingsWriter.println("advancedgraphics = yes");
			settingsWriter.println("vectors = no");
			settingsWriter.println("resultant = no");
			settingsWriter.println("trail = no");
			settingsWriter.println("effects = yes");
			settingsWriter.println("warpArrows = no");
			settingsWriter.println("speed = 3");
			settingsWriter.close();
		}
		return settings;
	}

	private ArrayList<Level> readFile(File file) throws IOException {
		long t = System.currentTimeMillis();
		ArrayList<Level> levels = new ArrayList<Level>();
		Scanner infile = new Scanner(file);

		Ball b = new Ball();
		ArrayList<Body> bod = new ArrayList<Body>();
		ArrayList<WarpPoint> warppts = new ArrayList<WarpPoint>();
		ArrayList<GoalPost> gs = new ArrayList<GoalPost>();
		ArrayList<Blockage> bs = new ArrayList<Blockage>();
		double followF = 0;
		double g = 0;
		int currentLineN = 0;

		try {
			while (infile.hasNext()) {
				currentLineN++;
				String line = infile.nextLine().toLowerCase().trim()
						.replaceAll(" ", "");

				if (line.length() > 2 && !line.substring(0, 2).equals("//")) {
					String[] data;
					// Read ball data in
					if (line.substring(0, 4).equals("ball")) {

						data = line.substring(5, line.length() - 1).split(",");
						int x = Integer.parseInt(data[0]);
						int y = Integer.parseInt(data[1]);
						int r;
						Color c;
						if (data.length > 2) {
							r = Integer.parseInt(data[2]);
							c = readColor(data[3]);
						} else {
							r = 3;
							c = Color.red;
						}
						b = new Ball(x, y, r, c);
					} else if (line.substring(0, 4).equals("refl")) {

						data = line.substring(5, line.length() - 1).split(",");
						int x = Integer.parseInt(data[0]);
						int y = Integer.parseInt(data[1]);
						int r = Integer.parseInt(data[2]);
						Color c = readColor(data[3]);
						Body tempBod;
						if (data.length > 4) {
							tempBod = new Body(x, y, r, c,
									Integer.parseInt(data[4]));
						} else {
							tempBod = new Body(x, y, r, c);
						}
						tempBod.setReflector(true);
						bod.add(tempBod);
					}
					// Read planet data in
					else if (line.substring(0, 4).equals("body")) {

						data = line.substring(5, line.length() - 1).split(",");
						int x = Integer.parseInt(data[0]);
						int y = Integer.parseInt(data[1]);
						int r = Integer.parseInt(data[2]);
						Color c = readColor(data[3]);
						if (data.length > 4) {
							bod.add(new Body(x, y, r, c, Integer
									.parseInt(data[4])));
						} else {
							bod.add(new Body(x, y, r, c));
						}
					}
					// read moon data in - attach to previous body
					else if (line.substring(0, 4).equals("moon")) {
						data = line.substring(5, line.length() - 1).split(",");
						int a = Integer.parseInt(data[0]);
						int r = Integer.parseInt(data[2]);
						// radius of ball and moon are added to the distance
						// (moon parameter is the distance from center to
						// center)
						int d = Integer.parseInt(data[1])
								+ bod.get((bod.size() - 1)).getRadius() + r;
						Color c = readColor(data[3]);
						Moon m = new Moon(a, d, r, c, bod.get(bod.size() - 1));
						bod.get((bod.size() - 1)).addMoon(m); // attaches moon
																// to last body
																// in bod
					}
					// read warp data in
					else if (line.substring(0, 4).equals("warp")) {
						data = line.substring(5, line.length() - 1).split(",");
						int x = Integer.parseInt(data[0]);
						int y = Integer.parseInt(data[1]);
						warppts.add(new WarpPoint(x, y));
					}
					// read goal data in
					else if (line.substring(0, 4).equals("goal")) {
						data = line.substring(5, line.length() - 1).split(",");
						int x = Integer.parseInt(data[0]);
						int y = Integer.parseInt(data[1]);
						int r = Integer.parseInt(data[2]);
						gs.add(new GoalPost(x, y, r));
					}
					// read top-left input blockage data in
					else if (line.substring(0, 4).equals("rec2")) {
						data = line.substring(5, line.length() - 1).split(",");
						int leftX = Integer.parseInt(data[0]);
						int topY = Integer.parseInt(data[1]);
						int width = Integer.parseInt(data[2]);
						int height = Integer.parseInt(data[3]);
						int sX = width / 2;
						int sY = height / 2;
						int cX = leftX + sX;
						int cY = topY + sY;
						Color c = readColor(data[4]);
						bs.add(new Blockage(cX, cY, sX, sY, c));
					}
					// read default blockage data in
					else if (line.substring(0, 4).equals("rect")) {
						data = line.substring(5, line.length() - 1).split(",");
						int cX = Integer.parseInt(data[0]);
						int cY = Integer.parseInt(data[1]);
						int sX = Integer.parseInt(data[2]);
						int sY = Integer.parseInt(data[3]);
						Color c = readColor(data[4]);
						bs.add(new Blockage(cX, cY, sX, sY, c));
					}
					// read final level data in
					else if (line.substring(0, 5).equals("level")) {

						data = line.substring(6, line.length() - 1).split(",");
						if (data.length == 0) {
							followF = 0;
							g = 1;
						} else if (data.length == 1) {
							followF = 0;
							g = Double.parseDouble(data[0]);
						} else {
							followF = Double.parseDouble(data[0]);
							g = Double.parseDouble(data[1]);

						}
						levels.add(new Level(b, bod, warppts, gs, bs, followF,
								g));

						b = new Ball();
						bod = new ArrayList<Body>();
						warppts = new ArrayList<WarpPoint>();
						gs = new ArrayList<GoalPost>();
						bs = new ArrayList<Blockage>();
					} else {
						pw.println("Failed to read" + file
								+ " - Invalid type at line " + currentLineN
								+ ".");
						pw.close();
						JOptionPane.showMessageDialog(null,
								"GravityGolf is unable to load " + file + ".",
								"Error", JOptionPane.ERROR_MESSAGE);
						errorFound = true;
					}
				}
			}
		} catch (Exception e) {
			pw.println("Failed to read " + file + " - Invalid entry at line "
					+ currentLineN + ".");
			pw.println(e);
			System.out.println("Failed to read " + file
					+ " - Invalid entry at line " + currentLineN + ".");
			System.out.println(e);
			e.printStackTrace();
			pw.close();
			// System.exit(0);
			if (!errorFound)
				JOptionPane.showMessageDialog(null,
						"GravityGolf is unable to load " + file + " at line "
								+ currentLineN + ".", "Error",
						JOptionPane.ERROR_MESSAGE);
			errorFound = true;
			infile.close();
			return null;
		}
		pw.println("Done reading " + file + ". Took "
				+ (System.currentTimeMillis() - t) + " ms.");
		infile.close();
		return levels;

	}

	/**
	 * Returns if the last reading produced any error.
	 * @return true of false
	 */
	public boolean wasErrorFound() {
		return errorFound;
	}

	/**
	 * Returns a String representation of this color.
	 * @param c a Color
	 * @return a String that contains the three pigment values
	 */
	public static String getColorDisplay(Color c) {
		if (c.equals(Color.red))
			return "red";
		if (c.equals(Color.black))
			return "black";
		if (c.equals(Color.blue))
			return "blue";
		if (c.equals(Color.cyan))
			return "cyan";
		if (c.equals(Color.gray))
			return "gray";
		if (c.equals(Color.green))
			return "green";
		if (c.equals(Color.magenta))
			return "magenta";
		if (c.equals(Color.orange))
			return "orange";
		if (c.equals(Color.pink))
			return "pink";
		if (c.equals(Color.yellow))
			return "yellow";
		if (c.equals(new Color(128, 0, 128)))
			return "purple";
		if (c.equals(new Color(127, 0, 255)))
			return "violet";
		return "Color(" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue()
				+ ")";
	}

	/**
	 * Reads in a Sting representation of a Color and creates the appropriate
	 * Color object.
	 * @param str a parameter
	 * @return the Color object specified by the input String
	 */
	public static Color readColor(String str) {
		str = str.toLowerCase();
		if (str.equals("red"))
			return Color.red;
		if (str.equals("black"))
			return Color.black;
		if (str.equals("blue"))
			return Color.blue;
		if (str.equals("cyan"))
			return Color.cyan;
		if (str.equals("gray"))
			return Color.gray;
		if (str.equals("green"))
			return Color.green;
		if (str.equals("magenta"))
			return Color.magenta;
		if (str.equals("orange"))
			return Color.orange;
		if (str.equals("pink"))
			return Color.pink;
		if (str.equals("yellow"))
			return Color.yellow;
		if (str.equals("purple"))
			return new Color(128, 0, 128);
		if (str.equals("violet"))
			return new Color(127, 0, 255);

		// TODO: error handling
		
		String r = str.substring(str.indexOf("(") + 1, str.indexOf(","));
		String g = str.substring(str.indexOf(",") + 1, str.lastIndexOf(","));
		String b = str.substring(str.lastIndexOf(",") + 1, str.indexOf(")"));

		return new Color(Integer.parseInt(r), Integer.parseInt(g),
				Integer.parseInt(b));
	}

}