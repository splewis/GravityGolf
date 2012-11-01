package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import structures.Ball;
import structures.Body;
import structures.CalcHelp;
import structures.Level;
import structures.Moon;
import structures.Point2d;

/**
 * 
 * @author Sean Lewis
 *
 */
public class GravityVectorsEffect extends GraphicEffect {

	public void draw(Level level, Graphics g) {
		
		List<Body> bodies = level.getBodies();
		Ball ball = level.getBall();
		double screenXShift = level.getScreenXShift();
		double screenYShift = level.getScreenYShift();

		g.setColor(Color.white);

		Point2d ballCent = new Point2d(ball.getCenter().x, ball.getCenter().y);
		for (Body b : bodies) {
			Point2d bodyCent = b.getCenter();
			double angle = CalcHelp.getAngle(ballCent, bodyCent);
			double length = level.getGravityStrength() * ArrowLength
					* b.getRadius() / ballCent.getDistance(bodyCent) + 5;
			Point2d p2 = new Point2d(ballCent.x + screenXShift + length
					* Math.cos(angle), ballCent.y + length * -Math.sin(angle)
					+ screenYShift);
			Point2d ballPt = new Point2d(ballCent.x + screenXShift, ballCent.y
					+ screenYShift);
			drawArrow(ballPt, p2, ArrowDistanceFromBall, ArrowSize, g);
			
			for (Moon m : b.getMoons()) {
				Point2d moonCent = m.getCenter();
				angle = CalcHelp.getAngle(ballCent, moonCent);
				length = level.getGravityStrength() * ArrowLength
						* m.getRadius() / ballCent.getDistance(moonCent) + 5;
				p2 = new Point2d(ballCent.x + screenXShift + length
						* Math.cos(angle), ballCent.y + length
						* -Math.sin(angle) + screenYShift);
				drawArrow(ballPt, p2, ArrowDistanceFromBall, ArrowSize, g);
			}
			
		}

	}

}