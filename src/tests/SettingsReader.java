package tests;

import static org.junit.Assert.*;
import game.DataHandler;
import java.io.*;
import org.junit.*;

public class SettingsReader {
	
	static final String dir = "testFiles/";
	
	@Before
	public void setup() {
		new File(dir).mkdir();		
	}	
	
	@Test
	public void simpleRead() throws FileNotFoundException {
		String fileName = dir + "simpleRead.txt";
		PrintWriter pw = new PrintWriter(new File(fileName));
		pw.println("effects = yes");
		pw.println("vectors = no");
		pw.println("resultant = no");
		pw.println("trail = yes");
		pw.println("warpArrows = no");
		pw.println("speed = 3");
		pw.close();
		assertArrayEquals(new int[] { 1, 0, 0, 1, 0, 3 },
				DataHandler.getSettings(fileName));
	}

	@Test
	public void simpleRead_noTrail() throws FileNotFoundException {
		String fileName = dir + "simpleRead_noTrail.txt";
		PrintWriter pw = new PrintWriter(new File(fileName));
		pw.println("effects = yes");
		pw.println("vectors = no");
		pw.println("resultant = no");
		pw.println("trail = no");
		pw.println("warpArrows = no");
		pw.println("speed = 3");
		pw.close();
		assertArrayEquals(new int[] { 1, 0, 0, 0, 0, 3 },
				DataHandler.getSettings(fileName));
	}

	@Test
	public void badRead() throws FileNotFoundException {
		String fileName = dir + "badRead.txt";
		PrintWriter pw = new PrintWriter(new File(fileName));
		pw.println("effects = yes");
		pw.println("vectodgagrs = no");
		pw.println("41warpArrows = I DON'T KNOW!");
		pw.close();
		// test against default settings
		assertArrayEquals(new int[] { 1, 0, 0, 1, 0, 3 },
				DataHandler.getSettings(fileName));
	}

}