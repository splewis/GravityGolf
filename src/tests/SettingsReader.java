package tests;

import static org.junit.Assert.*;
import game.DataHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.junit.Test;

public class SettingsReader {

	@Test
	public void simpleRead() throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File("settings.txt"));
		pw.println("effects = yes");
		pw.println("vectors = no");
		pw.println("resultant = no");
		pw.println("trail = yes");
		pw.println("warpArrows = no");
		pw.println("speed = 3");
		pw.close();
		assertArrayEquals(new int[] { 1, 0, 0, 1, 0, 3 },
				DataHandler.getSettings());
	}

	@Test
	public void simpleRead_noTrail() throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File("settings.txt"));
		pw.println("effects = yes");
		pw.println("vectors = no");
		pw.println("resultant = no");
		pw.println("trail = no");
		pw.println("warpArrows = no");
		pw.println("speed = 3");
		pw.close();
		assertArrayEquals(new int[] { 1, 0, 0, 0, 0, 3 },
				DataHandler.getSettings());
	}

	@Test
	public void badRead() throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new File("settings.txt"));
		pw.println("effects = yes");
		pw.println("vectodgagrs = no");
		pw.println("41warpArrows = I DON'T KNOW!");
		pw.close();
		// test against default settings
		assertArrayEquals(new int[] { 1, 0, 0, 1, 0, 3 },
				DataHandler.getSettings());
	}

}