package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import structures.Level;
import structures.Point2d;

/**
 * Holder class for the trail drawing effect
 * @author Sean Lewis
 */
public final class TrailEffect extends GraphicEffect {

	private static ArrayList<Point2d> trailPoints = new ArrayList<Point2d>();

	/**
	 * Adds a point the to current trail.
	 * @param point a parameter
	 */
	public static void addTrailPoint(Point2d point) {
		trailPoints.add(point);
	}

	/**
	 * Clears all points from the saved trail.
	 */
	public static void resetPoints() {
		trailPoints.clear();
	}

	/**
	 * Draws the trail effect.
	 * @param trailPoints the list of points in the trail
	 * @param level the current Level
	 * @param g the Graphics component to draw with
	 */
	public static void draw(Level level, Graphics g) {
		try {
			double screenXShift = level.getScreenXShift();
			double screenYShift = level.getScreenYShift();
			Color c = Color.green;
			g.setColor(c);
			for (int i = 0; i < trailPoints.size() - 2; i++) {
				if (trailPoints.get(i) != null
						&& trailPoints.get(i + 1) != null) {
					g.drawLine(
							(int) (Math.round(trailPoints.get(i).x
									+ screenXShift)),
							(int) (Math.round(trailPoints.get(i).y
									+ screenYShift)),
							(int) (Math.round(trailPoints.get(i + 1).x
									+ screenXShift)),
							(int) (Math.round(trailPoints.get(i + 1).y
									+ screenYShift)));

				}
			}
		} catch (Exception e) {

		}

	}

}