package structures;

import java.awt.Color;
import java.awt.Graphics;

/**
 * A small (pixel-sized) object that is used for the collision graphic effect.
 * @author Sean Lewis
 */
public class Particle extends MovableCircularShape {

	/**
	 * Creates a new Particle at the given position with the given velocity.
	 * @param xPosition the x coordinate
	 * @param yPosition the y coordinate
	 * @param xSpeed the magnitude of speed in the horizontal direction
	 * @param ySpeed the magnitude of speed in the vertical direction
	 * @param color the Color of the Particle
	 */
	public Particle(int xPosition, int yPosition, double xSpeed, double ySpeed,
			Color color) {
		setCenter(new Point2d(xPosition, yPosition));
		setVelocity(new Vector2d(xSpeed, ySpeed));
		setColor(color);
		setRadius(1);
	}

	public void draw(double dx, double dy, Graphics g) {
		g.setColor(color);
		g.fillOval((int) Math.round(center.x - 0.5 + dx),
				(int) Math.round(center.y - 0.5 + dy), 1, 1);
	}

}