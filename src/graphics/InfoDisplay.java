package graphics;

import game.GamePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DecimalFormat;

import structures.*;
import game.*;

/**
 * Holder class for methods used to display information to the user.
 * @author Sean Lewis
 */
public final class InfoDisplay {
	
	/**
	 * Default decimal formatting for all output from the game.
	 */
	public static final DecimalFormat DecimalFormatter = new DecimalFormat(
			"#0.00");
	
	/**
	 * Font used to display small information such as level and vector data. 
	 */
	private static final Font InfoFont = new Font("Times new Roman", Font.PLAIN,
			12);

	/**
	 * Draws the information about the initial vector given.
	 * @param terminalPoint the terminal point of the initial vector
	 * @param magnitude length of the initial vector
	 * @param angle angle of the initial vector
	 * @param c the color the text should be
	 * @param g the Graphics component to draw with
	 */
	public static void vectorInformation(Point2d terminalPoint,
			double magnitude, double angle, Color c, Graphics g) {
		g.setColor(c);
		g.setFont(InfoFont);
		int xCoord = GamePanel.Width - 90;
		g.drawString("(" + (int) terminalPoint.x + ", " + (int) terminalPoint.y
				+ ")", xCoord, 20);
		g.drawString("Length: " + DecimalFormatter.format(magnitude), xCoord,
				40);
		double degrees = Math.toDegrees(angle);
		g.drawString("Angle: " + DecimalFormatter.format(degrees), xCoord, 60);
	}

	/**
	 * Displays the level information.
	 * @param game the GameManager for the game
	 * @param g the Graphics component to draw with
	 */
	public static void levelInformation(GameManager game, Graphics g) {
		g.setColor(Color.white);
		g.setFont(InfoFont);
		g.drawString(
				"Level " + game.getLevelNumber() + " / "
						+ game.getNumberOfLevels(), 10, 20);
		g.drawString(
				"Swings: " + game.getCurrentLevelSwings() + " / "
						+ game.getTotalSwings(), 10, 40);
	}

	/**
	 * Draws the win screen.
	 * @param game the GameManager for the game
	 * @param g the Graphics component to draw with
	 */
	public static void drawWinScreen(GameManager game, Graphics g) {
		int numLevels = game.getNumberOfLevels();
		g.setColor(Color.black);
		g.fillRect(0, 0, GamePanel.Width + 30, GamePanel.Height);
		g.setFont(new Font("Tahoma", Font.ITALIC, 80));
		g.setColor(Color.blue);
		g.drawString("You win!", 140, 75);
		g.setFont(new Font("Times new Roman", Font.ITALIC, 25));
		g.drawString("It took you a total of " + game.getTotalSwings()
				+ " swings", 530, 75);
		g.drawString("Level", 78, 150);
		g.drawString("swings", 187, 150);
		int xFactor = 200;
		if (numLevels > 40)
			xFactor = 175;
		else if (numLevels > 50)
			xFactor = 160;
		else if (numLevels > 60)
			xFactor = 145;
		for (int i = 0; i < numLevels; i++) {
			g.drawString((i + 1) + ": " + game.getLevelSwings(i + 1), xFactor
					* (i / 10) + 140, 50 * (i % 10) + 150);
		}
	}

	/**
	 * Displays the paused message.
	 * @param g the Graphics component to draw with
	 */
	public static void drawPaused(Graphics g) {
		g.setFont(new Font("Tahoma", Font.ITALIC, 30));
		g.setColor(Color.red);
		g.drawString("PAUSED", 422, 50);
	}

	/**
	 * Displays the message that the level is complete to the user.
	 * @param g the Graphics component to draw with
	 */
	public static void drawNextLevelMessage(Graphics g) {
		g.setFont(new Font("Times new Roman", Font.ITALIC, 25));
		g.setColor(Color.WHITE);
		g.drawString("Click to continue to the next level", 320, 60);
	}

}