package structures;

import java.awt.Color;
import java.awt.Graphics;

public abstract class CircularShape {

	protected Point2d center;
	protected Point2d topLeft;
	protected Color color;
	protected int radius;
	protected int radiusSquared;
	protected int diameter;

	/**
	 * Initializes the radiusSqured, diameter, and topLeft values.
	 */
	protected void initializeVars() {
		setRadius(radius);
		topLeft = new Point2d(center.x - radius, center.y - radius);
	}

	/**
	 * Draws the shape with the shape's default color.
	 * @param dx a translation x-difference
	 * @param dy a translation y-difference
	 * @param g the Graphics component to draw with
	 */
	public void draw(double dx, double dy, Graphics g) {
		draw(dx, dy, g, color);
	}

	/**
	 * Draws the shape with the shape's default color.
	 * @param g the Graphics component to draw with
	 */
	public void draw(Graphics g) {
		draw(0, 0, g, color);
	}

	/**
	 * Draws the shape.
	 * @param dx a translation x-difference
	 * @param dy a translation y-difference
	 * @param g the Graphics component to draw with
	 * @param color the color to use
	 */

	public void draw(double dx, double dy, Graphics g, Color color) {
		g.setColor(color);
		g.fillOval((int) (center.x - radius + dx), (int) (center.y - radius + dy),
				diameter, diameter);
	}

	/**
	 * Draws the shape.
	 * @param g the Graphics component to draw with
	 * @param color the color to use
	 */
	public void draw(Graphics g, Color color) {
		draw(0, 0, g, color);
	}

	/**
	 * Returns the center coordinate.
	 * @return the center
	 */
	public Point2d getCenter() {
		return center;
	}

	/**
	 * Returns the color.
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Returns the radius.
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Returns the square of the radius.
	 * @return the square of the radius
	 */
	public int getRadiusSquared() {
		return radiusSquared;
	}

	/**
	 * Returns the diameter.
	 * @return the diameter
	 */
	public int getDiameter() {
		return diameter;
	}

	/**
	 * Sets the new color.
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Sets the new radius.
	 * @param radius
	 */
	public void setRadius(int radius) {
		this.radius = radius;
		diameter = 2 * radius;
		radiusSquared = radius * radius;
	}

	/**
	 * Returns if this intersects another CircularShape.
	 * @param circle another shape
	 * @return true if they overlap, false otherwise
	 */
	public boolean intersects(CircularShape circle) {
	//	return CalcHelp.intersects(center, circle.getCenter(), radius, circle.getRadius());
		double distSquared = getCenter().getDistanceSquared(circle.getCenter());
		return distSquared < Math.pow((getRadius() + circle.getRadius()), 2);
	}	

}