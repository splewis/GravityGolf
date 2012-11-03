package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;
import structures.*;
import graphics.*;

/**
 * Manages and tracks the game.
 * @author Sean Lewis
 */
public class GameManager {

	private int currentLevelIndex;
	private ArrayList<Level> levels;
	private int[] swingData;
	private int totalSwings;

	/**
	 * Starts the GameManager, reading in all level data.
	 * @throws IOException
	 */
	public GameManager() throws IOException {
		DataReader reader = new DataReader();
		levels = reader.getLevelData("levels/levels.txt");
		currentLevelIndex = -1;
		swingData = new int[levels.size()];
	}

	/**
	 * Returns the current level number. The menu level is defined as level 0,
	 * and the first level is defined as level 1.
	 * @return the current level number
	 */
	public int getLevelNumber() {
		return currentLevelIndex + 1;
	}

	/**
	 * Returns, excluding the menu level, the number of levels in the game.
	 * @return the number of levels
	 */
	public int getNumberOfLevels() {
		return levels.size();
	}

	/**
	 * Returns the total number of swings taken in the game.
	 * @return total swings taken
	 */
	public int getTotalSwings() {
		return totalSwings;
	}

	/**
	 * Returns the total number of swings taken in the current level.
	 * @return swings taken in this level
	 */
	public int getCurrentLevelSwings() {
		return swingData[currentLevelIndex];
	}

	/**
	 * Returns the number of swings taken in a specific level number.
	 * @param levelNumber a parameter
	 * @return number of swings taken in a level
	 */
	public int getLevelSwings(int levelNumber) {
		return swingData[levelNumber - 1];
	}

	/**
	 * Returns the current level for the game.
	 * @return the current game level
	 */
	public Level getCurrentLevel() {
		if (currentLevelIndex == -1) {
			return MenuScreen.getMenuLevel();
		}
		return levels.get(currentLevelIndex);
	}

	/**
	 * Returns if the game has been finished yet.
	 * @return if the game is over
	 */
	public boolean isGameOver() {
		return currentLevelIndex >= levels.size();
	}

	/**
	 * Loads the next level into the memory.
	 * @return true if the next level was loaded, false is no more levels exist.
	 */
	public boolean nextLevel() {
		currentLevelIndex++;
		if (currentLevelIndex >= levels.size()) {
			GravityGolf.DataWriter.gameFinished(totalSwings);
			return false;
		}
		levels.get(currentLevelIndex).generateLevelData();
		GravityGolf.DataWriter.levelFinished(currentLevelIndex + 1,
				swingData[currentLevelIndex]);
		return true;
	}

	/**
	 * Tells the GameManager a swing has been taken by the player. Updates swing
	 * information.
	 */
	public void swingTaken() {
		swingData[currentLevelIndex]++;
		totalSwings++;
	}


	/**
	 * Loads a file into the game.
	 * @param file the file to read from
	 * @return if the file was successfully loaded
	 * @throws FileNotFoundException if the file could not be read from
	 */
	public boolean loadSave(File file) throws FileNotFoundException {
		int totalswingimport = 0;
		int level = 0;
		boolean cheatingDetected = false;
		Scanner infile = new Scanner(file);

		int[] swingImport = new int[0];

		try {
			swingImport = new int[levels.size()];
			int swingSum = 0;
			for (int i = 0; i < levels.size(); i++) {
				swingImport[i] = DataReader.decode((long) infile.nextInt(), i);
				if (swingImport[i] < 0) {
					cheatingDetected = true;
					break;
				}
				swingSum += swingImport[i];
			}
			level = DataReader.decode(infile.nextInt(), levels.size());
			if (level < 0 || level > levels.size()) {
				cheatingDetected = true;
			}
			totalswingimport = DataReader.decode(infile.nextInt(),
					levels.size() + 1);
			if (totalswingimport != swingSum) {
				cheatingDetected = true;
			}

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			GravityGolf.DataWriter.println(file.getName() + " failed to load.");
			GravityGolf.DataWriter.println(e.toString());
		}
		if (!cheatingDetected) {
			currentLevelIndex = level;
			totalSwings = totalswingimport;
			swingData = swingImport;
			GravityGolf.DataWriter.println(file.getName()
					+ " loaded successfully.");
			infile.close();
			return true;

		}
		JOptionPane.showMessageDialog(null,
				"Cheating detected; will not load file", "Cheating Detected",
				JOptionPane.ERROR_MESSAGE);
		GravityGolf.DataWriter.println(file.getName()
				+ "failed to load - likely cheating.");
		infile.close();
		return false;
	}

}