package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Object that handles the writing of data for the gamelog. Includes static
 * methods for other writing purposes, such as saving of games and settings.
 * @author Sean Lewis
 */
public class DataWriter {

	private PrintWriter gameWriter;

	/**
	 * Initializes a new DataWriter.
	 * @throws FileNotFoundException
	 */
	public DataWriter() throws FileNotFoundException {
		gameWriter = new PrintWriter(new File("logs/gamelog.txt"));
	}

	/**
	 * Closes the output stream.
	 */
	public void close() {
		gameWriter.close();
	}

	/**
	 * Writes that a level was finished.
	 * @param levelNumber the level that was finished
	 * @param swings the swings taken on that level
	 */
	public void levelFinished(int levelNumber, int swings) {
		gameWriter.println("Level " + Integer.toString(levelNumber)
				+ " complete. " + Integer.toString(swings) + " swings.");
		gameWriter.println();
	}

	/**
	 * Writes that the game was finished.
	 * @param totalSwings the total number of swings taken in the game
	 */
	public void gameFinished(int totalSwings) {
		gameWriter.println("Game complete. " + Integer.toString(totalSwings)
				+ " swings total.");
	}

	/**
	 * Writes that the game was saved
	 * @param fileName the file the game was saved to
	 */
	public void gameSaved(String fileName) {
		gameWriter.println("Game saved to " + fileName + ".");
	}

	/**
	 * Writes that the ball was launched.
	 * @param p the point clicked on screen
	 * @param launchMagnitude the launch magnitude
	 * @param launchAngle the launch angle
	 */
	public void ballLaunched(java.awt.Point p, double launchMagnitude,
			double launchAngle) {
		gameWriter.println("Ball launched. Point: (" + p.getX() + ", "
				+ p.getY() + "). Magnitude: " + launchMagnitude + ", Angle: "
				+ Math.toDegrees(launchAngle));
	}

	/**
	 * Prints a String to the output stream.
	 * @param str a parameter
	 */
	public void println(String str) {
		gameWriter.println(str);
	}

	/**
	 * Writes that a game was loaded.
	 * @param fileName a parameter
	 */
	public void gameLoaded(String fileName) {
		// TODO
	}

	/**
	 * Prints the current game settings to settings.txt.
	 * @throws IOException if settings.txt cannot be written to
	 */
	public static void printSettings(boolean[] settings, int speed)
			throws IOException {
		PrintWriter settingsWriter = new PrintWriter("settings.txt");
		
		settingsWriter.println("effects = "
				+ (settings[GamePanel.EffectsNum] ? "yes" : "no"));
		settingsWriter.println("vectors = "
				+ (settings[GamePanel.VectorsNum] ? "yes" : "no"));
		settingsWriter.println("resultant = "
				+ (settings[GamePanel.ResultantNum] ? "yes" : "no"));
		settingsWriter.println("trail = "
				+ (settings[GamePanel.TrailNum] ? "yes" : "no"));
		settingsWriter.println("warpArrows = "
				+ (settings[GamePanel.WarpArrowsNum] ? "yes" : "no"));

		settingsWriter.println("speed = " + speed);
		settingsWriter.close();
	}

	/**
	 * Prints the current game settings to settings.txt.
	 * @throws IOException if settings.txt cannot be written to
	 */
	public static void printSettings(int[] settings) throws IOException {
		PrintWriter settingsWriter = new PrintWriter("settings.txt");
		settingsWriter.println("effects = "
				+ (settings[GamePanel.EffectsNum] == 1 ? "yes" : "no"));
		settingsWriter.println("vectors = "
				+ (settings[GamePanel.VectorsNum] == 1 ? "yes" : "no"));
		settingsWriter.println("resultant = "
				+ (settings[GamePanel.ResultantNum] == 1 ? "yes" : "no"));
		settingsWriter.println("trail = "
				+ (settings[GamePanel.TrailNum] == 1 ? "yes" : "no"));
		settingsWriter.println("warpArrows = "
				+ (settings[GamePanel.WarpArrowsNum] == 1 ? "yes" : "no"));

		settingsWriter.println("speed = " + settings[settings.length - 1]);
		settingsWriter.close();
	}

}