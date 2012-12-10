package game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
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
		DataHandler reader = new DataHandler();
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

		// deletes last level from the memory
		if (currentLevelIndex >= 0) {
			levels.set(currentLevelIndex, null);
		}
		currentLevelIndex++;
		if (currentLevelIndex >= levels.size()) {
			return false;
		}
		levels.get(currentLevelIndex).generateLevelData();
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
	 * @param fileName the file to read from
	 * @throws IOException
	 */
	public static GameManager loadSave(File fileName) throws IOException {
		GameManager result = new GameManager();
		DataHolder d = new DataHolder();
		d = d.loadSave(fileName);
		result.currentLevelIndex = d.currentLevelIndex;
		result.swingData = d.swingData;
		result.totalSwings = d.totalSwings;
		result.levels.get(result.currentLevelIndex).generateLevelData();
		return result;
	}

	/**
	 * Saves the game state to a file.
	 * @param fileName the file name to save to
	 */
	public void save(String fileName) {
		DataHolder d = new DataHolder();
		d.currentLevelIndex = currentLevelIndex;
		d.swingData = swingData;
		d.totalSwings = totalSwings;
		DataHolder.save(d, fileName);
	}

	private static class DataHolder implements Serializable {
		int currentLevelIndex;
		int[] swingData;
		int totalSwings;

		public DataHolder loadSave(File fileName) throws FileNotFoundException {
			FileInputStream fin = null;
			ObjectInputStream oin = null;
			DataHolder result = null;
			try {
				fin = new FileInputStream(fileName);
				oin = new ObjectInputStream(fin);
				result = (DataHolder) oin.readObject();
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println(e);
			} catch (ClassCastException e) {
				System.out.println(e);
			} finally {
				try {
					if (oin != null)
						oin.close();
					else if (fin != null)
						fin.close();
				} catch (IOException e) {
					System.out.println(e);
					e.printStackTrace();
				}
			}
			return result;
		}

		public static void save(DataHolder d, String fileName) {
			FileOutputStream fout = null;
			ObjectOutputStream oout = null;
			try {
				fout = new FileOutputStream(fileName);
				oout = new ObjectOutputStream(fout);
				oout.writeObject(d);
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			} finally {
				try {
					if (oout != null)
						oout.close();
					else if (fout != null)
						fout.close();
				} catch (IOException e) {
					System.out.println(e);
					e.printStackTrace();
				}
			}
		}
	}

}