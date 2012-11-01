package graphics;

import java.awt.Graphics;

import structures.CalcHelp;
import structures.Level;
import structures.Point2d;

/**
 * 
 * @author Sean Lewis
 *
 */
public abstract class GraphicEffect {

	protected static final int ArrowDistanceFromBall = 4;
	protected static final int ArrowSize = 12;
	protected static final int ArrowLength = 200;
	
	/**
	 * 
	 */
	protected boolean on = false;

	/**
	 * 
	 * @param level
	 * @param g
	 */
	public abstract void draw(Level level, Graphics g);

	/**
	 * 
	 * @param on
	 */
	public void changeSetting(boolean on) {
		this.on = on;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isOn() {
		return on;
	}

	/**
	 * 
	 * @param p1
	 * @param p2
	 * @param drawingOffset
	 * @param arrowSize
	 * @param g
	 */
	protected void drawArrow(Point2d p1, Point2d p2, int drawingOffset,
			int arrowSize, Graphics g) {

		double ang = CalcHelp.getAngle(p1, p2);

		// Shifts away from the center of the ball, so the line starts a little
		// past the edge of the ball
		double xShift = Math.cos(ang) * (drawingOffset);
		double yShift = -Math.sin(ang) * (drawingOffset);

		g.drawLine((int) Math.round(p1.x + xShift),
				(int) Math.round(p1.y + yShift), (int) Math.round(p2.x),
				(int) Math.round(p2.y));

		double angleOne = ang - Math.PI / 4;
		// double angleTwo = ang - Math.PI / 4;

		double cos1 = Math.cos(angleOne);
		double sin1 = Math.sin(angleOne);

		g.drawLine((int) Math.round(p2.x), (int) Math.round(p2.y),
				(int) Math.round(p2.x - arrowSize * cos1),
				(int) Math.round(p2.y + arrowSize * sin1));

		g.drawLine((int) Math.round(p2.x), (int) Math.round(p2.y),
				(int) Math.round(p2.x + arrowSize * sin1),
				(int) Math.round(p2.y + arrowSize * cos1));

	}

}