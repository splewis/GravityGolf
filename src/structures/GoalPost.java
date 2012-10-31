package structures;

import java.awt.Color;

/**
 * @author Sean Lewis
 */
public class GoalPost extends CircularShape {

	private static final Color GoalPostColor = Color.WHITE;

	public GoalPost(int x, int y, int r) {
		setCenter(new Point2d(x, y));
		setRadius(r);
		setColor(GoalPostColor);
	}

	public String toString() {
		return "goal(" + center.x + ", " + center.y + ", " + radius + ")";
	}

}