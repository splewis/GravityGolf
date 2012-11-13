package structures;

import game.DataReader;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Represents the Movable Ball for the game. Supports resetting operations and
 * capable of generating particles from a collision with another Body.
 * @author Sean Lewis
 */
public class Ball extends MovableCircularShape {

	private final Point2d startingLocation;
	private boolean launched;

	/**
	 * Creates the default red Ball at (0,0) with radius 3.
	 */
	public Ball() {
		this(0, 0, 3, Color.red);
	}

	/**
	 * Creates a ball with specific center, radius, and color values.
	 * @param centerX a parameter
	 * @param centerY a parameter
	 * @param radius a parameter
	 * @param color a parameter
	 */
	public Ball(int centerX, int centerY, int radius, Color color) {
		setCenter(new Point2d(centerX, centerY));
		startingLocation = new Point2d(centerX, centerY);
		setRadius(radius);
		setColor(color);
		setVelocity(new Vector2d(0, 0));
	}

	/**
	 * Generates an ArrayList of Particles that are created from a collision
	 * with a body.
	 * @param body the colliding body.
	 * @return an ArrayList of Particles
	 */
	public ArrayList<Particle> generateParticles(Body body) {
		double cX = center.x;
		double cY = center.y;
		double speed = velocity.getLength()
				* ((body != null) ? (body.getRadius() / 100.0) : 1);
		if (speed < .50)
			speed = .50;
		if (speed > 2.50)
			speed = 2.50;
		if (body != null) {
			double max = Math.pow(body.getRadius() + radius, 2);
			while (body.getCenter().getDistanceSquared(new Point2d(cX, cY)) < max) {
				cX -= velocity.getXComponent();
				cY -= velocity.getYComponent();
			}
		}

		ArrayList<Particle> particles = new ArrayList<Particle>();
		int num = (int) (300 * speed);
		if (num < 150) {
			num = 150;
		} else if (num > 400) {
			num = 400;
		}
		// TODO: way too many magic numbers!
		for (int i = 0; i < num; i++) {

			double angle = (Math.random() * 2 * Math.PI);
			int x = (int) cX;
			int y = (int) cY;
			double xSpeed = 0.75 * (Math.random() * speed * Math.cos(angle));
			double ySpeed = 0.75 * (Math.random() * speed * Math.sin(angle));
			Color c;
			double randomInteger = (Math.random());
			if (body == null) {
				c = CalcHelp.getShiftedColor(color, 100);
			} else if (randomInteger < 0.3) {
				c = CalcHelp.getShiftedColor(body.getColor(), 100);
			} else if (randomInteger < 0.40) {
				c = CalcHelp.getShiftedColor(new Color(230, 130, 70), 100);
			} else {
				c = CalcHelp.getShiftedColor(color, 100);
			}

			Particle newParticle = new Particle(x, y, xSpeed, ySpeed, c);
			particles.add(newParticle);

		}
		return particles;
	}

	/**
	 * Returns if the ball has been launched yet.
	 * @return if the ball has been launched yet
	 */
	public boolean isLaunched() {
		return launched;
	}

	/**
	 * Sets if the ball has been launched yet.
	 * @param b a parameter
	 */
	public void setLaunched(boolean b) {
		launched = b;
	}

	/**
	 * Resets the ball to its location as was defined in its construction.
	 */
	public void resetLocation() {
		center = new Point2d(startingLocation.x, startingLocation.y);
	}

	/**
	 * Returns the description of the Ball object.
	 */
	public String toString() {
		String str = "ball(";
		str += startingLocation.x + ", " + startingLocation.y + ", " + radius
				+ ", " + DataReader.getColorDisplay(color) + ")";
		return str;
	}

	@Override
	/**
	 * Returns a hash code for this Ball.
	 */
	public int hashCode() {
		return toString().hashCode();
	}
}