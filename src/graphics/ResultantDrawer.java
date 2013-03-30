package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import structures.*;

/**
 * Holder class for drawing the gravitational resultant effect.
 * @author Sean Lewis
 */
public final class ResultantDrawer extends GraphicEffect {

	/**
	 * Draws the gravitational resultant from the ball.
	 * @param level the level in the game
	 * @param g the Graphics component to draw with
	 */
	public static void draw(Level level, Graphics g) {

		Point2d shift = level.getShift();
		Ball ball = level.getBall();
		List<Body> bodies = level.getBodies();

		double totalX = 0.0;
		double totalY = 0.0;
		Point2d ballCent = new Point2d(ball.getCenter().x(), ball.getCenter().y());

		for (Body b : bodies) {

			Point2d bodyCent = new Point2d(b.getCenter().x(), b.getCenter().y());
			double angle = CalcHelp.getAngle(ballCent, bodyCent);
			double length = level.getGravityStrength() * ArrowLength
					* b.getRadius() / ballCent.distance(bodyCent) + 5;
			totalX += length * Math.cos(angle);
			totalY -= length * Math.sin(angle);

			for (Moon m : b.getMoons()) {
				Point2d moonCent = new Point2d(m.getCenter().x(), m.getCenter()
						.y());
				angle = CalcHelp.getAngle(ballCent, moonCent);
				length = level.getGravityStrength() * ArrowLength
						* m.getRadius() / ballCent.distance(moonCent) + 5;
				totalX += length * Math.cos(angle);
				totalY -= length * Math.sin(angle);
			}

		}

		g.setColor(Color.blue);
		Point2d tempPt1 = ball.getCenter().translate(shift);
		Point2d tempPt2 = new Point2d(tempPt1.x() + totalX, tempPt1.y() + totalY);
		drawArrow(tempPt1, tempPt2, g);
	}

}