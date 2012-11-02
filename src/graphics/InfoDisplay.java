package graphics;

import java.awt.Graphics;
import java.text.DecimalFormat;

import structures.Point2d;

/**
 * 
 * @author Sean
 *
 */
public final class InfoDisplay {

	// Fonts n' shit
	public static final DecimalFormat DecimalFormatter = new DecimalFormat(
			"#0.00");

	/**
	 * 
	 * @param terminalPoint
	 * @param magnitude
	 * @param angle
	 * @param g
	 */
	public static void vectorInformation(Point2d terminalPoint,
			double magnitude, double angle, Graphics g) {
		g.drawString("(" + (int) terminalPoint.x + ", " + (int) terminalPoint.y
				+ ")", 910, 20);
		g.drawString("Length: " + DecimalFormatter.format(magnitude), 910, 40);
		double degrees = Math.toDegrees(angle);
		g.drawString("Angle: " + DecimalFormatter.format(degrees), 910, 60);
	}

	/**
	 * 
	 * @param currentLevel
	 * @param numLevels
	 * @param currentSwings
	 * @param totalSwings
	 * @param g
	 */
	public static void levelInformation(int currentLevel, int numLevels,
			int currentSwings, int totalSwings, Graphics g) {
		g.drawString("Level " + currentLevel + " / " + numLevels, 10, 20);
		g.drawString("Swings: " + currentSwings + " / " + totalSwings, 10, 40);
	}
}