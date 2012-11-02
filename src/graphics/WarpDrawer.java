package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import structures.*;

/**
 * @author Sean Lewis
 */
public final class WarpDrawer extends GraphicEffect {

	/**
	 * 
	 * @param level
	 * @param g
	 */
	public static void draw(Level level, Graphics g) {
		g.setColor(Color.white);
		ArrayList<WarpPoint> warps = level.getWarpPoints();
		double screenXShift = level.getScreenXShift();
		double screenYShift = level.getScreenYShift();

		for (int i = 1; i < warps.size(); i++) {
			Point2d p1 = warps.get(i - 1).getCenter()
					.translate(screenXShift, screenYShift);
			Point2d p2 = warps.get(i).getCenter()
					.translate(screenXShift, screenYShift);

			drawArrow(p1, p2, 0, 25, g);
		}

	}

}