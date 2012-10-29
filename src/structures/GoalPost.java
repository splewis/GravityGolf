package structures;

import java.awt.Color;

/**
 * @author Sean Lewis
 */
public class GoalPost extends CircularShape {

	private static final Color GoalPostColor = Color.WHITE;

	public GoalPost(int x, int y, int r) {
		center = new Point2d(x, y);
		radius = r;
		color = GoalPostColor;
		initializeVars();
	}

	public String toString() {
		return "goal(" + center.x + ", " + center.y + ", " + radius + ")";
	}

}