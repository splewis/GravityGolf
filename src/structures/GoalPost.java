package structures;

import java.awt.Color;

/**
 * CircularShape representing the goal circle in Levels. Colored White by
 * default.
 * @author Sean Lewis
 */
public class GoalPost extends CircularShape {

	private static final Color GoalPostColor = Color.WHITE;

	/**
	 * Creates a new GoalPost with center (x, y) and radius r.
	 * @param x the x coordinate of the center
	 * @param y the y coordinate of the center
	 * @param r the radius
	 */
	public GoalPost(int x, int y, int r) {
		setCenter(new Point2d(x, y));
		setRadius(r);
		setColor(GoalPostColor);
	}

	/**
	 * Returns the description of the GoalPost.
	 */
	public String toString() {
		return "goal(" + center.x + ", " + center.y + ", " + radius + ")";
	}

}