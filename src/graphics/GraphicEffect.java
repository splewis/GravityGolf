package graphics;

import java.awt.Graphics;
import structures.CalcHelp;
import structures.Point2d;

/**
 * Abstract class for representing extra graphical effects on screen.
 * @author Sean Lewis
 */
public abstract class GraphicEffect {

	/**
	 * 
	 */
	public static final int ArrowDistanceFromBall = 4;
	
	/**
	 * 
	 */
	public static final int ArrowSize = 12;
	
	/**
	 * 
	 */
	public static final int ArrowLength = 200;

	/**
	 * 
	 * @param p1
	 * @param p2
	 * @param g
	 */
	public static void drawArrow(Point2d p1, Point2d p2, Graphics g) {
		GraphicEffect.drawArrow(p1, p2, ArrowDistanceFromBall, ArrowSize, g);
	}

	/**
	 * @param p1
	 * @param p2
	 * @param drawingOffset
	 * @param arrowSize
	 * @param g
	 */
	public static void drawArrow(Point2d p1, Point2d p2, int drawingOffset,
			int arrowSize, Graphics g) {

		double ang = CalcHelp.getAngle(p1, p2);

		// Shifts away from the center of the ball, so the line starts a little
		// past the edge of the ball
		double xShift = Math.cos(ang) * (drawingOffset);
		double yShift = -Math.sin(ang) * (drawingOffset);

		g.drawLine((int) Math.round(p1.x + xShift),
				(int) Math.round(p1.y + yShift), (int) Math.round(p2.x),
				(int) Math.round(p2.y));

		double angle = ang - Math.PI / 4;

		double cosine = Math.cos(angle);
		double sine = Math.sin(angle);

		g.drawLine((int) Math.round(p2.x), (int) Math.round(p2.y),
				(int) Math.round(p2.x - arrowSize * cosine),
				(int) Math.round(p2.y + arrowSize * sine));

		g.drawLine((int) Math.round(p2.x), (int) Math.round(p2.y),
				(int) Math.round(p2.x + arrowSize * sine),
				(int) Math.round(p2.y + arrowSize * cosine));

	}

}