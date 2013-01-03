package tests;

import static org.junit.Assert.*;
import game.DataHandler;

import java.awt.Color;
import java.io.*;
import org.junit.*;

public class DataHandlerTests {
	
	static final String dir = "testFiles/";
	
	@Before
	public void setup() {
		new File(dir).mkdir();		
	}	
	
	@Test
	public void simpleSettingsRead() throws FileNotFoundException {
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
	public void simpleSettingsRead_noTrail() throws FileNotFoundException {
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
	public void badSettingsRead() throws FileNotFoundException {
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

	@Test
	public void readColorTests() {
		assertEquals(Color.red, DataHandler.readColor("red"));
		assertEquals(Color.green, DataHandler.readColor("green"));
		assertEquals(Color.blue, DataHandler.readColor("blue"));
		assertEquals(Color.white, DataHandler.readColor("white"));
		assertEquals(Color.black, DataHandler.readColor("black"));
		assertEquals(Color.yellow, DataHandler.readColor("yellow"));
		assertEquals(Color.cyan, DataHandler.readColor("cyan"));
		
		assertEquals(new Color(1,2,3), DataHandler.readColor("Color(1/ 2/ 3)"));
		assertEquals(new Color(3,2,1), DataHandler.readColor("Color(3/ 2/ 1)"));
		assertEquals(new Color(1,2,3), DataHandler.readColor("Color(1/ 2/ 3))"));
		assertEquals(new Color(3,2,1), DataHandler.readColor("Color(3/ 2/ 1))"));
		
		assertEquals(null, DataHandler.readColor("Color(3.1/ 2/ 1)"));
		assertEquals(null, DataHandler.readColor("Color(4/ 3/ 2/ 1)"));
		assertEquals(null, DataHandler.readColor("Color(1/ 2)"));
		
		assertEquals(true, same(Color.red));
		assertEquals(true, same(Color.green));
		assertEquals(true, same(Color.blue));
		assertEquals(true, same(Color.white));
		assertEquals(true, same(Color.yellow));
		assertEquals(true, same(Color.cyan));
		assertEquals(true, same(new Color(1,2,3)));
		assertEquals(true, same(new Color(100,2,3)));
		assertEquals(true, same(new Color(1,32,35)));		
	}
	
	private boolean same(Color c) {
		return DataHandler.readColor(DataHandler.getColorDisplay(c)).equals(c);
	}
	
	
	
	
}