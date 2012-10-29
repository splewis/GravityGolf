/**
 * @(#)Moon.java
 *
 *
 * @author Sean Lewis
 * @version 1.00 2011/7/26
 */

package structures;

import game.DataReader;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

public class Moon extends Body {
	// Constant rate of moon angular velocity (radians per update)
	private double deltaAngle = 0.0;
	// Parameters:
	private final int startingAngle;
	private final int startingDistance;
	// Higher parameters: (sourced from the body the moon is attached to)
	private final int bodyCenterX, bodyCenterY;
	private final int bodyMass;
	private final int bodyRadius;
	// Parameter-dependent:

	private Point2d center;
	// Variables:
	private double currentAngle;
	private float[] dist = { 0.93f, 0.97f, .995f };
	Color[] colors;

	/**
	 * @param a
	 * @param d
	 * @param r
	 * @param c
	 * @param b
	 */
	public Moon(int a, int d, int r, Color c, Body b) {
		startingAngle = a;
		currentAngle = Math.toRadians(startingAngle);
		startingDistance = d;
		radius = r;
		color = c;
		diameter = 2 * radius;
		bodyCenterX = (int) b.getCenter().x;
		bodyCenterY = (int) b.getCenter().y;
		bodyRadius = (int) b.getRadius();
		bodyMass = (int) b.getMass();
		double x = b.getCenter().x + Math.cos(currentAngle) * d;
		double y = b.getCenter().y - Math.sin(currentAngle) * d;
		center = new Point2d(x, y);
		if (radius < 50) {
			dist[0] = 0.90f;
			dist[1] = 0.95f;
		}
		colors = new Color[3];
		colors[0] = color;
		colors[1] = Color.white;
		colors[2] = Color.BLACK;
	}

	/**
	 * 
	 */
	public void advancedDraw(double dx, double dy, Graphics2D g) {
		RadialGradientPaint gradient = new RadialGradientPaint(
				new Point2D.Double(center.x + dx, center.y + dy), radius, dist,
				colors);

		g.setPaint(gradient);
		g.fillOval((int) Math.round(center.x - radius + dx),
				(int) Math.round(center.y - radius + dy), diameter + 1,
				diameter + 1);
	}

	/**
	 * @param g
	 */
	public void advancedDraw(Graphics2D g) {
		advancedDraw(0, 0, g);
	}

	/**
	 * @return
	 */
	public double getAngle() {
		return currentAngle;
	}

	/**
	 * 
	 */
	public Point2d getCenter() {
		return center;
	}

	/**
	 * @param g
	 */
	public void move(double g) {
		if (deltaAngle == 0.0) {
			deltaAngle = .0075 * Math.sqrt(g * bodyMass
					/ (startingDistance + bodyRadius + radius));
			// F_c = F_g -> mv^2/r = GMm /r^2 -> v^2/r=GM/r^2 -> v^2 = GM/r
		}
		currentAngle -= deltaAngle;
		// System.out.println("angle:" + currentAngle);
		center.x = (bodyCenterX + startingDistance * Math.cos(currentAngle));
		center.y = (bodyCenterY - startingDistance * Math.sin(currentAngle));
	}

	/**
	 * 
	 */
	public String toString() {
		return "moon(" + startingAngle + ", " + startingDistance + ", "
				+ radius + ", " + DataReader.getColorDisplay(color) + ")";
	}

}