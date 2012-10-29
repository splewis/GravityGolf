package structures;

import java.awt.Color;
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
			"images/spiralImage.gif");

	public WarpPoint() {
		this(0, 0);
	}

	public WarpPoint(int x, int y) {
		center = new Point2d(x, y);
		radius = RADIUS;
		initializeVars();
	}

	public void draw(int dx, int dy, Graphics g, Color c) {
		g.drawImage(Image, (int) (center.x - radius + dx), 
				           (int) (center.y - radius + dy), null);
	}

	public void draw(Graphics g) {
		draw(0, 0, g);
	}

	public String toString() {
		return "warp(" + center.x + ", " + center.y + ")";
	}

}