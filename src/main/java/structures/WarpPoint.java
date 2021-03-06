package structures;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * A WarpPoint is a structure that allows the ball to be teleported to the next
 * warpPoint available.
 * @author Sean Lewis
 */
public class WarpPoint extends CircularShape {

	/**
	 * The Radius of all WarpPoints.
	 */
	public static final int RADIUS = 15;

	/**
	 * The Image for all WarpPoints.
	 */
	public static final Image Image = Toolkit.getDefaultToolkit().getImage(
			"src/resources/images/spiralImage.gif");

	/**
	 * Creates a new WarpPoint at (0,0).
	 */
	public WarpPoint() {
		this(0, 0);
	}

	/**
	 * Creates a new WarpPoint at (x, y)
	 * @param x the center x coordinate
	 * @param y the center y coordinate
	 */
	public WarpPoint(int x, int y) {
		center = new Point2d(x, y);
		setRadius(RADIUS);
	}

	@Override
	public void draw(double dx, double dy, Graphics g) {
		g.drawImage(Image, (int) (center.x() - radius + dx), (int) (center.y()
				- radius + dy), null);
	}

	/**
	 * Returns the description of this WarpPoint.
	 */
	public String toString() {
		return "warp(" + center.x() + ", " + center.y() + ")";
	}

}