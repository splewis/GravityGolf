package structures;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Sean Lewis
 */
public class Particle extends MovableCircularShape {

	/**
	 * 
	 * @param x
	 * @param y
	 * @param xs
	 * @param ys
	 * @param c
	 */
	public Particle(int x, int y, double xs, double ys, Color c) {
		setCenter(new Point2d(x, y));
		setVelocity(new Vector2d(xs, ys));
		setColor(c);
		setRadius(1);
	}

	/**
	 * 
	 */
	public void draw(double dx, double dy, Graphics g) {
		g.setColor(color);
		g.fillOval((int) Math.round(center.x - 0.5 + dx),
				(int) Math.round(center.y - 0.5 + dy), 1, 1);
	}

}