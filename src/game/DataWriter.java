package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import structures.*;

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
	 * Returns an encoded value based of the input value and unique key.
	 * @param value the input value
	 * @param key the unique input key
	 * @return the encoded value
	 */
	private static long encode(int value, int key) {
		if (CalcHelp.isPrime(key)) {
			return (value * 3) + key * key * key;
		}
		if (key % 6 == 0) {
			return (value + key) * (key + 5);
		}
		if (key % 4 == 0) {
			return -15 * value + key;
		}
		if (key % 5 == 0) {
			return value * value + key;
		}
		if (key % 7 == 0) {
			return value + key * key * key;
		}
		if (key % 2 == 0) {
			return 10 * value - 20 - key * key;
		}
		return 8 * value - 6 * key * key;
	}

	/**
	 * Saves the current game to a file.
	 * @param file the file to save to.
	 * @throws FileNotFoundException if the file could not be written to
	 */
	public static void saveGame(GameManager game, File file)
			throws FileNotFoundException {
		PrintWriter saveWriter = new PrintWriter(file);
		for (int i = 1; i <= game.getNumberOfLevels(); i++) {
			saveWriter
					.print(DataWriter.encode(game.getLevelSwings(i), i) + " ");
		}
		saveWriter.print(DataWriter.encode(game.getLevelNumber() - 1,
				game.getNumberOfLevels())
				+ " ");
		saveWriter.print(DataWriter.encode(game.getTotalSwings(),
				game.getNumberOfLevels() + 1));
		saveWriter.close();
		GravityGolf.DataWriter.gameSaved(file.getName());
	}

}