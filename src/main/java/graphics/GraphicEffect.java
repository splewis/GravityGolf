package graphics;

import java.awt.Graphics;
import structures.CalcHelp;
import structures.Point2d;

/**
 * Class for representing extra graphical effects on screen.
 * @author Sean Lewis
 */
public abstract class GraphicEffect {

	/**
	 * Constant value that multiplies the length of drawn arrows so they are
	 * longer.
	 */
	public static final int ArrowLength = 200;

	/**
	 * Draws an arrow between two points with default properties: offest of 4
	 * and arrow head length of 12.
	 * @param p1 the initial point
	 * @param p2 the terminal point
	 * @param g the Graphics Component to draw with
	 */
	public static void drawArrow(Point2d p1, Point2d p2, Graphics g) {
		GraphicEffect.drawArrow(p1, p2, 4, 12, g);
	}

	/**
	 * Draws an arrow between two points.
	 * @param p1 the initial point 
	 * @param p2 the terminal point
	 * @param drawingOffset the offset distance from the initial to start
	 *        drawing the point
	 * @param arrowSize the length of each tail at the terminal point
	 * @param g the Graphics component to draw with
	 */
	public static void drawArrow(Point2d p1, Point2d p2, int drawingOffset,
			int arrowSize, Graphics g) {
		double ang = CalcHelp.getAngle(p1, p2);

		// Shifts away from the center of the ball, so the line starts a little
		// past the edge of the ball
		double xShift = Math.cos(ang) * (drawingOffset);
		double yShift = -Math.sin(ang) * (drawingOffset);

		g.drawLine((int) Math.round(p1.x() + xShift),
				(int) Math.round(p1.y() + yShift), (int) Math.round(p2.x()),
				(int) Math.round(p2.y()));

		double angle = ang - Math.PI / 4;
		double cosine = Math.cos(angle);
		double sine = Math.sin(angle);

		g.drawLine((int) Math.round(p2.x()), (int) Math.round(p2.y()),
				(int) Math.round(p2.x() - arrowSize * cosine),
				(int) Math.round(p2.y() + arrowSize * sine));

		g.drawLine((int) Math.round(p2.x()), (int) Math.round(p2.y()),
				(int) Math.round(p2.x() + arrowSize * sine),
				(int) Math.round(p2.y() + arrowSize * cosine));

	}

}