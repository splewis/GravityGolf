package graphics;

import game.GamePanel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import structures.Ball;
import structures.Blockage;
import structures.Body;
import structures.GoalPost;
import structures.Level;
import structures.WarpPoint;

/**
 * 
 * @author Sean
 *
 */
public final class MenuScreen {

	private static final String[] instructionStrings = { "H: Help", "P: pause",
			"R: reset", "Right arrow: speed up",
			"Left arrow: slow down",
			// break
			"S: hide stars", "V: show gravity vectors",
			"D: show gravity resultant", "T: show ball trail",
			"E: show special effects" };

	/**
	 * 
	 * @return
	 */
	public static Level getMenuLevel() {
		Ball b = new Ball(340, 335, 3, Color.red);
		ArrayList<Body> bod = new ArrayList<Body>();
		bod.add(new Body(495, 335, 100, Color.magenta));
		ArrayList<WarpPoint> ws = new ArrayList<WarpPoint>();
		ArrayList<GoalPost> gs = new ArrayList<GoalPost>();
		ArrayList<Blockage> bs = new ArrayList<Blockage>();
		return new Level(b, bod, ws, gs, bs, 0, 3.5);
	}
	
	/**
	 * 
	 * @param menuLevel
	 * @param settings
	 * @param g
	 */
	public static void draw(Level menuLevel, boolean[] settings, Graphics g) {
		if (settings[GamePanel.VectorsNum]) {
			GravityVectorsEffect.draw(menuLevel, g);
		}
		if (settings[GamePanel.ResultantNum]) {
			ResultantDrawer.draw(menuLevel, g);
		}

		g.setColor(Color.blue);
		g.setFont(GamePanel.TitleFont);
		g.drawString("Gravity Golf", 275, 100);

		g.setFont(GamePanel.MediumFont);
		g.setColor(Color.blue);
		if (settings[GamePanel.VectorsNum]) {
			instructionStrings[6] = instructionStrings[6].replace("show",
					"hide");
		} else {
			instructionStrings[6] = instructionStrings[6].replace("hide",
					"show");
		}
		if (settings[GamePanel.ResultantNum]) {
			instructionStrings[7] = instructionStrings[7].replace("show",
					"hide");
		} else {
			instructionStrings[7] = instructionStrings[7].replace("hide",
					"show");
		}
		if (settings[GamePanel.TrailNum]) {
			instructionStrings[8] = instructionStrings[8].replace("show",
					"hide");
		} else {
			instructionStrings[8] = instructionStrings[8].replace("hide",
					"show");
		}
		if (settings[GamePanel.EffectsNum]) {
			instructionStrings[9] = instructionStrings[9].replace("show",
					"hide");
		} else {
			instructionStrings[9] = instructionStrings[9].replace("hide",
					"show");
		}

		for (int i = 0; i < 5; i++) {
			g.drawString(instructionStrings[i], 50, 60 * i + 235);
		}
		for (int i = 5; i < instructionStrings.length; i++) {
			g.drawString(instructionStrings[i], 700, 60 * (i - 5) + 235);
		}

		g.setFont(GamePanel.SmallFont);
		g.setColor(Color.green);
		g.drawString(
				"Your goal is to give the ball an initial velocity that allows it reach the white goal.",
				140, 590);

		g.setColor(Color.white);
		g.drawString("Press Space to begin", 373, 630);
		g.drawString("0.01", 10, 630);
		g.drawString("11/1/2012", 10, 660);
	}

}