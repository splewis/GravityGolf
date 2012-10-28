/**
 * A Blockage is a rectangular shape that blocks the motion of a ball. 
 *
 * @author Sean Lewis
 * @version 1.00 2011/8/30
 */

package structures;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

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
	 * @param cX
	 * @param cY
	 * @param xS
	 * @param yS
	 * @param c
	 */
	public Blockage(int cX, int cY, int xS, int yS, Color c) {
		centerX = cX;
		centerY = cY;
		center = new Point2d(centerX, centerY);
		xSize = xS;
		ySize = yS;
		color = c;
		drawX = centerX - xSize;
		drawY = centerY - ySize;
		drawXSize = 2 * xSize;
		drawYSize = 2 * ySize;
		rectangle = new Rectangle(drawX, drawY, drawXSize, drawYSize);
	}

	/**
	 * @param p1
	 * @param p2
	 * @param c
	 */
	public Blockage(java.awt.Point p1, java.awt.Point p2, Color c) {
		drawXSize = Math.abs(p1.x - p2.x);
		xSize = drawXSize / 2;
		drawYSize = Math.abs(p1.y - p2.y);
		ySize = drawYSize / 2;
		centerX = (p1.x + p2.x) / 2;
		centerY = (p1.y + p2.y) / 2;
		center = new Point2d(centerX, centerY);
		color = c;
		drawX = Math.min(p1.x, p2.x);
		drawY = Math.min(p1.y, p2.y);
		rectangle = new Rectangle(drawX, drawY, drawXSize, drawYSize);
	}

	/**
	 * @param dx
	 * @param dy
	 * @param g
	 */
	public void draw(int dx, int dy, Graphics2D g) {

		g.setPaint(new GradientPaint(drawX + dx, drawY + dy, color, drawX
				+ drawXSize, drawY + drawYSize, CalcHelp.getShiftedColor(color,
				40)));

		g.fillRoundRect(drawX + dx, drawY + dy, drawXSize, drawYSize, 15, 15);
	}

	/**
	 * @param g
	 */
	public void draw(Graphics2D g) {
		draw(0, 0, g);
	}

	/**
	 * @param dx
	 * @param dy
	 * @param g
	 */
	public void basicDraw(int dx, int dy, Graphics g) {
		g.setColor(color);
		g.fillRect(drawX + dx, drawY + dy, drawXSize, drawYSize);
	}

	/**
	 * @return
	 */
	public int getCenterX() {
		return centerX;
	}

	/**
	 * @return
	 */
	public int getCenterY() {
		return centerY;
	}

	/**
	 * @return
	 */
	public Point2d getCenter() {
		return center;
	}

	/**
	 * @return
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @return
	 */
	public int getDrawX() {
		return drawX;
	}

	/**
	 * @return
	 */
	public int getDrawY() {
		return drawY;
	}

	/**
	 * @return
	 */
	public int getDrawXSize() {
		return drawXSize;
	}

	/**
	 * @return
	 */
	public int getDrawYSize() {
		return drawYSize;
	}

	/**
	 * @return
	 */
	public int getXSize() {
		return xSize;
	}

	/**
	 * @return
	 */
	public int getYSize() {
		return ySize;
	}

	/**
	 * @param p
	 * @return
	 */
	public boolean intersects(Point2d p) {
		return rectangle.contains(p.getIntegerPoint());
	}

	/**
	 * 
	 */
	public String toString() {
		return "rect(" + centerX + ", " + centerY + ", " + xSize + ", " + ySize
				+ ", " + CalcHelp.getColorDisplay(color) + ")";
	}

}