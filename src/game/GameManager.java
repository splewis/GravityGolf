package game;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import editor.LevelSolver;
import editor.Randomizer;

import structures.*;
import graphics.*;

/**
 * Manages and tracks the game.
 * @author Sean Lewis
 */
public class GameManager {

	/**
	 * Default location for the level files to be located.
	 */
	public static final String DEFAULT_LEVELS = "levels/levels.txt";
	private static PrintWriter logger;	
	
	private int currentLevelIndex;	
	private Collection<Point> solutions;
	private List<Level> levels;
	private boolean randomLevels; // if game uses dynamic random levels
	private int[] swingData;
	private int totalSwings;

	/**
	 * Initializes a new GameManager with default levels.
	 */
	public GameManager() {
		this(DEFAULT_LEVELS);
	}

	/**
	 * Initializes a new GameManager from a given levels file.
	 * @param levelsFile the text file the levels are defined in
	 */
	public GameManager(String levelsFile) {
		DataHandler reader = new DataHandler();
		try {
			levels = reader.getLevelData(levelsFile);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"The levels.txt file could not be read.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		currentLevelIndex = -1;
		swingData = new int[levels.size()];
		setupLogger();
	}

	/**
	 * Initializes a new GameManager that will use random levels.
	 * @param numRandLevels the number of random levels the game will have
	 */
	public GameManager(int numRandLevels) {
		randomLevels = true;
		levels = new ArrayList<Level>(numRandLevels);
		setupLogger();
		logger.println("Generating " + numRandLevels + " random levels.");
		// assign a task to each random level generation
		class LevelMaker implements Runnable {
			int index;
			public void run() {
				levels.set(index, Randomizer.randomLevel());
				logger.println("Done with level " + (index + 1) + ".");
				logger.flush();
			}
		}

		LevelMaker[] tasks = new LevelMaker[numRandLevels];
		for (int i = 0; i < numRandLevels; i++) {
			levels.add(null);
			tasks[i] = new LevelMaker();
			tasks[i].index = i;
		}

		// parallel computation for each task:
		ExecutorService executor = Executors.newCachedThreadPool();
		for (LevelMaker t : tasks)
			executor.execute(t);
		executor.shutdown();

		try {
			executor.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		logger.println("Done generating all levels");
		logger.flush();
		currentLevelIndex = -1;
		swingData = new int[levels.size()];
	}

	private void setupLogger() {
		new File("logs").mkdir();
		File f = new File("logs/gamelog.txt");
		try {
			logger = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			System.out.println(e);
			e.printStackTrace();
		}
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
	 * Returns the total number of swings taken in the current level. -1 is
	 * returned if there is no current level.
	 * @return swings taken in this level
	 */
	public int getCurrentLevelSwings() {
		try {
			return swingData[currentLevelIndex];
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
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
	 * Returns the solution set for the current level. If the data has not been
	 * computed, an empty list is returned.
	 * @return the current level's solution set, if computed already
	 */
	public Collection<Point> getCurrentSolutions() {
		if (solutions == null) {
			String fileName = "levels/data/level" + getLevelNumber() + ".txt";
			try {
				solutions = LevelSolver.readSolutionSet(fileName);
			} catch (Exception e) {
				System.out.println("Unable to read " + fileName + ".");
				solutions = new ArrayList<Point>();
			}
		}
		return solutions;
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
		solutions = null;
		// deletes last level from the memory
		if (currentLevelIndex >= 0) {
			if (randomLevels)
				levels.get(currentLevelIndex).clearLevelData();
			else
				levels.set(currentLevelIndex, null);
		}

		// advance level data
		currentLevelIndex++;
		if (currentLevelIndex >= levels.size())
			return false;
		Level nextLevel = levels.get(currentLevelIndex);
		nextLevel.generateLevelData();
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
		logger.println("Loading save in " + fileName + ".");
		DataHolder d = new DataHolder();
		d = d.loadSave(fileName);

		GameManager result = null;
		if (d.levelsFile != null) // non-standard level files (random levels)
			result = new GameManager(d.levelsFile);
		else
			result = new GameManager(DEFAULT_LEVELS);

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
		logger.println("Saving to " + fileName + ".");
		DataHolder d = new DataHolder();
		d.currentLevelIndex = currentLevelIndex;
		d.swingData = swingData;
		d.totalSwings = totalSwings;

		// save level data if needed:
		if (randomLevels) {
			String lvlFileName = fileName.substring(0, fileName.indexOf('.'))
					+ ".txt";
			logger.println("Reading level data from " + lvlFileName + ".");
			d.levelsFile = lvlFileName;

			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new File(lvlFileName));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < levels.size(); i++)
				pw.println(levels.get(i));
			pw.close();
		} else {
			d.levelsFile = null;
		}
		DataHolder.save(d, fileName);
	}

	/**
	 * Temporary hold for saving the game state. (does not include levels List)
	 */
	private static class DataHolder implements Serializable {
		int currentLevelIndex;
		int[] swingData;
		int totalSwings;
		String levelsFile;

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