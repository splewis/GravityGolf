package structures;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

/**
 * A Star is a visual component that is simply a colored circle that is drawn in
 * the background.
 * @author Sean Lewis
 */
public class Star implements Serializable  {

	private java.awt.Point point;
	private Color color;
	private int size;

	/**
	 * Creates a new start at (x,y) and sets its color.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Star(int x, int y) {
		point = new java.awt.Point(x, y);
		size = CalcHelp.gaussianInteger(1.5, 1.05);
		if (size < 1)
			size = 1;

		int r = CalcHelp.gaussianInteger(240, 30);
		int g = CalcHelp.gaussianInteger(240, 30);
		int b = CalcHelp.gaussianInteger(240, 40);
		color = CalcHelp.correctColor(r, g, b);
	}

	/**
	 * Draws the star shifted by dx and dy.
	 * @param dx the x translation
	 * @param dy the y translation
	 * @param g the Graphics component to draw with
	 */
	public void draw(int dx, int dy, Graphics g) {
		g.setColor(color);
		g.fillOval(point.x + dx, point.y + dy, size, size);
	}

	/**
	 * Draws the star.
	 * @param g the Graphics component to draw with
	 */
	public void draw(Graphics g) {
		draw(0, 0, g);
	}

	/**
	 * Returns the center point of the star.
	 * @return the center
	 */
	public java.awt.Point getPoint() {
		return point;
	}

}
