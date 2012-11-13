package structures;

import game.DataReader;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * A Blockage is a rectangular shape that blocks the motion of a ball. *
 * @author Sean Lewis
 */
public class Blockage {

	// Parameters:
	private final int centerX, centerY;
	private final Point2d center;
	private final int xSize, ySize;
	private final Color color;

	// Parameter-dependent:
	private final int drawX, drawY;
	private final int drawXSize, drawYSize;
	private final Rectangle rectangle;

	/**
	 * Constructs a new Blockage.
	 * @param centerX the center x coordinate
	 * @param centerY the center y coordinate
	 * @param xSize the distance from the center x to the right edge
	 * @param ySize the distance from the center y to the bottom edge
	 * @param color the color
	 */
	public Blockage(int centerX, int centerY, int xSize, int ySize, Color color) {
		this.centerX = centerX;
		this.centerY = centerY;
		center = new Point2d(centerX, centerY);
		this.xSize = xSize;
		this.ySize = ySize;
		this.color = color;
		drawX = centerX - xSize;
		drawY = centerY - ySize;
		drawXSize = 2 * xSize;
		drawYSize = 2 * ySize;
		rectangle = new Rectangle(drawX, drawY, drawXSize, drawYSize);
	}

	/**
	 * Constructs a new Blockage with opposite corners defined.
	 * @param p1 one corner
	 * @param p2 the opposite corner of p1
	 * @param color the color
	 */
	public Blockage(java.awt.Point p1, java.awt.Point p2, Color color) {
		drawXSize = Math.abs(p1.x - p2.x);
		xSize = drawXSize / 2;
		drawYSize = Math.abs(p1.y - p2.y);
		ySize = drawYSize / 2;
		centerX = (p1.x + p2.x) / 2;
		centerY = (p1.y + p2.y) / 2;
		center = new Point2d(centerX, centerY);
		this.color = color;
		drawX = Math.min(p1.x, p2.x);
		drawY = Math.min(p1.y, p2.y);
		rectangle = new Rectangle(drawX, drawY, drawXSize, drawYSize);
	}

	/**
	 * Draws the blockage.
	 * @param dx x transformation distance
	 * @param dy y transformation distance
	 * @param g the Graphics component to draw with
	 */
	public void draw(int dx, int dy, Graphics2D g) {
		g.setPaint(new GradientPaint(drawX + dx, drawY + dy, color, drawX
				+ drawXSize, drawY + drawYSize, CalcHelp.getShiftedColor(color,
				40)));
		g.fillRoundRect(drawX + dx, drawY + dy, drawXSize, drawYSize, 15, 15);
	}

	/**
	 * Draws the blockage with no translation.
	 * @param g the Graphics component to draw with
	 */
	public void draw(Graphics2D g) {
		draw(0, 0, g);
	}

	/**
	 * Returns the x center coordinate.
	 * @return the x center coordinate
	 */
	public int getCenterX() {
		return centerX;
	}

	/**
	 * Returns the y center coordinate.
	 * @return the y center coordinate
	 */
	public int getCenterY() {
		return centerY;
	}

	/**
	 * Returns the center point.
	 * @return the center point
	 */
	public Point2d getCenter() {
		return center;
	}

	/**
	 * Returns the color of the blockage.
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Returns the x coordinate of the upper left corner.
	 * @return the x coordinate of the upper left corner
	 */
	public int getDrawX() {
		return drawX;
	}

	/**
	 * Returns the y coordinate of the upper left corner.
	 * @return the y coordinate of the upper left corner
	 */
	public int getDrawY() {
		return drawY;
	}

	/**
	 * The total y length, twice the value of getYSize()
	 * @return the total vertical length
	 */
	public int getDrawXSize() {
		return drawXSize;
	}

	/**
	 * The total y length, twice the value of getYSize()
	 * @return the total vertical length
	 */
	public int getDrawYSize() {
		return drawYSize;
	}

	/**
	 * Returns the distance from the x center to the right edge.
	 * @return the x size
	 */
	public int getXSize() {
		return xSize;
	}

	/**
	 * Returns the distance from the y center to the bottom edge.
	 * @return the y size
	 */
	public int getYSize() {
		return ySize;
	}

	/**
	 * Returns if a point is inside or contained by this blockage.
	 * @param p a parameter
	 * @return if the point intersects this rectangle
	 */
	public boolean intersects(Point2d p) {
		return rectangle.contains(p.getIntegerPoint());
	}

	/**
	 * Returns the formatted description of the blockage.
	 */
	public String toString() {
		return "rect(" + centerX + ", " + centerY + ", " + xSize + ", " + ySize
				+ ", " + DataReader.getColorDisplay(color) + ")";
	}

}