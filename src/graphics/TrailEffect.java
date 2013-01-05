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

	private static ArrayList<Point2d> trailPoints = new ArrayList<Point2d>(100);

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
		double screenXShift = level.getScreenXShift();
		double screenYShift = level.getScreenYShift();
		
		// number of points to draw
		int n = trailPoints.size() - 2;
		g.setColor(Color.green);		
		
		for (int i = 0; i < n; i++) {
			Point2d from = trailPoints.get(i);
			Point2d to   = trailPoints.get(i + 1);
			int fromX = round(from.x + screenXShift);
			int fromY = round(from.y + screenYShift);
			int toX   = round(to.x   + screenXShift);
			int toY   = round(to.y   + screenYShift);
			g.drawLine(fromX, fromY, toX, toY);
		}
	}
	
	// convenience method since Math.round returns a long
	private static int round(double x) {
		return (int) Math.round(x);
	}

}