package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import structures.*;

/**
 * @author Sean Lewis
 */
public final class ResultantDrawer extends GraphicEffect {

	/**
	 * 
	 * @param level
	 * @param g
	 */
	public static void draw(Level level, Graphics g) {

		double screenXShift = level.getScreenXShift();
		double screenYShift = level.getScreenYShift();
		Ball ball = level.getBall();
		List<Body> bodies = level.getBodies();

		double totalX = 0.0;
		double totalY = 0.0;
		Point2d ballCent = new Point2d(ball.getCenter().x, ball.getCenter().y);

		for (Body b : bodies) {

			Point2d bodyCent = new Point2d(b.getCenter().x, b.getCenter().y);
			double angle = CalcHelp.getAngle(ballCent, bodyCent);
			double length = level.getGravityStrength() * ArrowLength
					* b.getRadius() / ballCent.getDistance(bodyCent) + 5;
			totalX += length * Math.cos(angle);
			totalY -= length * Math.sin(angle);

			for (Moon m : b.getMoons()) {
				Point2d moonCent = new Point2d(m.getCenter().x, m.getCenter().y);
				angle = CalcHelp.getAngle(ballCent, moonCent);
				length = level.getGravityStrength() * ArrowLength
						* m.getRadius() / ballCent.getDistance(moonCent) + 5;
				totalX += length * Math.cos(angle);
				totalY -= length * Math.sin(angle);
			}

		}

		g.setColor(Color.blue);
		Point2d tempPt1 = new Point2d(ball.getCenter().x + screenXShift,
				ball.getCenter().y + screenYShift);
		Point2d tempPt2 = new Point2d(tempPt1.x + totalX, tempPt1.y + totalY);
		drawArrow(tempPt1, tempPt2, ArrowDistanceFromBall, ArrowSize, g);
	}

}