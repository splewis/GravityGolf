package structures;

import game.DataHandler;
import java.awt.Color;

/**
 * Represents the Movable Ball for the game. Supports resetting operations and
 * capable of generating particles from a collision with another Body.
 * <p>
 * The ball's text format is as follows:
 * <li>ball(centerX, centerY, radius, color)
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
	 * Constructs a Ball in the Ball's text format.
	 * @param spec
	 */
	public Ball(String spec) {
		String[] data = spec.substring(5, spec.length() - 1).split(",");
		int x = Integer.parseInt(data[0]);
		int y = Integer.parseInt(data[1]);
		int r;
		Color c;
		if (data.length > 2) {
			r = Integer.parseInt(data[2]);
			c = DataHandler.readColor(data[3]);
		} else {
			r = 3;
			c = Color.red;
		}
		setCenter(new Point2d(x, y));
		startingLocation = new Point2d(x, y);
		setRadius(r);
		setColor(c);
		setVelocity(new Vector2d(0, 0));
	}

	/**
	 * Creates a red ball with specific center, radius, and color values.
	 * @param centerX a parameter
	 * @param centerY a parameter
	 * @param radius a parameter
	 */
	public Ball(int centerX, int centerY, int radius) {
		this(centerX, centerY, radius, Color.red);
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
	 * Returns the text-formatted description for the Ball.
	 */
	public String toString() {
		String str = "ball(";
		str += startingLocation.x + ", " + startingLocation.y + ", " + radius
				+ ", " + DataHandler.getColorDisplay(color) + ")";
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