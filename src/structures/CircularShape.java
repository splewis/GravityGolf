package structures;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Abstraction for any object that is to be drawn in a circular way. Supports
 * the geometry of the shape, drawing the shape, and changing the values of the
 * Geometry variables.
 * @author Sean Lewis
 */
public abstract class CircularShape {

	/**
	 * The current center point for the circle.
	 */
	protected Point2d center;
	/**
	 * The primary color used for drawing.
	 */
	protected Color color;
	/**
	 * Radius of the circle.
	 */
	protected int radius;
	/**
	 * The square of the radius.
	 */
	protected int radiusSquared;
	/**
	 * The diameter, or twice the radius.
	 */
	protected int diameter;	

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
		g.fillOval((int) (center.x - radius + dx),
				(int) (center.y - radius + dy), diameter, diameter);
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
	 * @param color the new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Sets the new radius. Also computes the diameter and radiusSquared values.
	 * @param radius the new radius
	 */
	public void setRadius(int radius) {
		this.radius = radius;
		diameter = 2 * radius;
		radiusSquared = radius * radius;
	}

	/**
	 * Moves the circle by settings its center.
	 * @param center the new center
	 */
	public void setCenter(Point2d center) {
		this.center = center;
	}

	/**
	 * Returns if this intersects another CircularShape.
	 * @param circle another shape
	 * @return true if they overlap, false otherwise
	 */
	public boolean intersects(CircularShape circle) {
		double distSquared = getCenter().getDistanceSquared(circle.getCenter());
		return distSquared < Math.pow((getRadius() + circle.getRadius()), 2);
	}

}