package graphics;

import game.GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import structures.Ball;
import structures.Blockage;
import structures.Body;
import structures.GoalPost;
import structures.Level;
import structures.Vector2d;
import structures.WarpPoint;

/**
 * Class for the Menu Screen functions.
 * @author Sean Lewis
 */
public final class MenuScreen {

	private static final String[] instructionStrings = {
			"P: pause",
			"R: reset", 
			"Right arrow: speed up",
			"Left arrow: slow down",
			// break
			"V: show gravity vectors",
			"D: show gravity resultant", 
			"T: show ball trail",
			"E: show special effects" };
	private static Level menuLevel = null;

	/**
	 * Returns the start level that the game uses.
	 * @return the menu Level
	 */
	public static Level getMenuLevel() {
		if (menuLevel == null) {
			Ball b = new Ball(340, 335, 3, Color.red);
			ArrayList<Body> bod = new ArrayList<Body>();
			bod.add(new Body(495, 335, 100, Color.magenta));
			ArrayList<WarpPoint> ws = new ArrayList<WarpPoint>();
			ArrayList<GoalPost> gs = new ArrayList<GoalPost>();
			ArrayList<Blockage> bs = new ArrayList<Blockage>();
			b.setLaunched(true);
			b.accelerate(new Vector2d(0.0, 1.8));
			menuLevel = new Level(b, bod, ws, gs, bs, 0, 3.5);
			menuLevel.generateLevelData();
		}
		return menuLevel;
	}

	/**
	 * Draws the menu screen and its information.
	 * @param menuLevel the level that is displayed
	 * @param settings the current settings in the game
	 * @param g the Graphics component to draw with
	 */
	public static void draw(Level menuLevel, boolean[] settings, Graphics g) {
		if (settings[GamePanel.VectorsNum]) 
			GravityVectorsEffect.draw(menuLevel, g);

		if (settings[GamePanel.ResultantNum])
			ResultantDrawer.draw(menuLevel, g);

		g.setColor(Color.blue);
		g.setFont(new Font("Tahoma", Font.ITALIC, 80));
		g.drawString("Gravity Golf", 275, 100);

		g.setFont(new Font("Times new Roman", Font.ITALIC, 25));
		g.setColor(Color.blue);

		for (int i = 0; i < 4; i++) {
			g.drawString(instructionStrings[i], 50, 60 * i + 235);
		}
		for (int i = 4; i < instructionStrings.length; i++) {
			g.drawString(instructionStrings[i], 700, 60 * (i - 4) + 235);
		}

		g.setFont(new Font("Times new Roman", Font.ITALIC, 20));
		g.setColor(Color.green);
		g.drawString("Your goal is to give the ball an initial velocity that "
				+ "allows it reach the white goal.", 140, 550);

		g.setColor(Color.white);
		g.drawString("Chose an option below to begin", 345, 590);
		g.drawString("1.00", 10, 630);
		g.drawString("1/1/2013", 10, 660);
	}

}