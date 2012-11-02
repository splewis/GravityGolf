package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import structures.Level;
import structures.Point2d;

/**
 * 
 * @author Sean
 *
 */
public final class TrailEffect extends GraphicEffect {

	/**
	 * 
	 * @param trailPoints
	 * @param level
	 * @param g
	 */
	public static void draw(ArrayList<Point2d> trailPoints, Level level,
			Graphics g) {
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