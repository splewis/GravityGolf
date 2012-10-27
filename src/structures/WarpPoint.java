package structures;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
 
/**
 * A WarpPoint is a structure that allows the ball to be teleported to the next warpPoint available.
 * @author Sean Lewis
 *
 */
public class WarpPoint {

	/*
	 * The Radius of all WarpPoints.
	 */
	public static final int Radius = 15;
	
	/*
	 * The Image for all WarpPoints.
	 */
	public static final Image Image = Toolkit.getDefaultToolkit().getImage("images/spiralImage.gif");
	
	private int centerX, centerY;
	private Point2d center;	

	public WarpPoint() {
		this(0, 0);
	}

	public WarpPoint(int x, int y) {
		centerX = x;
		centerY = y;
		center = new Point2d(centerX, centerY);
	}	
	
	public void draw(int dx, int dy, Graphics2D g) {
		g.drawImage(Image, centerX - Radius + dx, centerY - Radius + dy, null);	
	}

	public void draw(Graphics2D g) {
		g.drawImage(Image, centerX - Radius, centerY - Radius, null);		
	}

	public Point2d getCenter() {
		return center;
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public int getRadius() {
		return Radius;
	}

	public String toString() {
		return "warp(" + centerX + ", " + centerY + ")";
	}	
	
}