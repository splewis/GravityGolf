package structures;

import java.awt.Color;
import java.util.Random;

/**
 * Class that provides several frequently used methods across classes
 * @author Sean Lewis
 */
public final class CalcHelp {

	private static final Random randomGenerator = new Random();

	/**
	 * Creates a new color based on the specified component values. If the
	 * values are out of the appropriate color bounds, they are adjusted to fit
	 * in. (-10 becomes 0, 260 becomes 255, etc.)
	 * @param r the red value
	 * @param g the green value
	 * @param b the blue value
	 * @return a new Color
	 */
	public static Color correctColor(int r, int g, int b) {
		int newR = r;
		if (r < 0)
			newR = 0;
		if (r > 255)
			newR = 255;

		int newG = g;
		if (g < 0)
			newG = 0;
		if (g > 255)
			newG = 255;

		int newB = b;
		if (b < 0)
			newB = 0;
		if (b > 255)
			newB = 255;

		return new Color(newR, newG, newB);
	}

	/**
	 * Returns a random (with normal distribution) double.
	 * @param mean the mean of the distribution
	 * @param standardDeviation the standard deviation of the distribution
	 * @return a random double form a normal distribution
	 */
	public static double gaussianDouble(double mean, double standardDeviation) {
		return randomGenerator.nextGaussian() * standardDeviation + mean;
	}

	/**
	 * Returns a random (with normal distribution) double that is guaranteed to
	 * lie between two values.
	 * @param mean the mean of the distribution
	 * @param standardDeviation the standard deviation of the distribution
	 * @param min the smallest value that may be returned
	 * @param max the largest value that may be returned
	 * @return a random double form a normal distribution
	 */
	public static double gaussianDoubleInRange(double mean,
			double standardDeviation, double min, double max) {
		if (min > max)
			throw new IllegalArgumentException("The min parameter "
					+ "must be less than or equal to the max parameter.");
		double rand = gaussianDouble(mean, standardDeviation);
		if (rand < min)
			return min;
		if (rand > max)
			return max;
		return rand;
	}

	/**
	 * Returns a random (with normal distribution) int.
	 * @param mean the mean of the distribution
	 * @param standardDeviation the standard deviation of the distribution
	 * @return a random int form a normal distribution
	 */
	public static int gaussianInteger(double mean, double standardDeviation) {
		return (int) Math.round(gaussianDouble(mean, standardDeviation));
	}

	/**
	 * Returns a random (with normal distribution) double that is guaranteed to
	 * lie between two values.
	 * @param mean the mean of the distribution
	 * @param standardDeviation the standard deviation of the distribution
	 * @param min the smallest value that may be returned
	 * @param max the largest value that may be returned
	 * @return a random double form a normal distribution
	 */
	public static int gaussianIntegerInRange(double mean,
			double standardDeviation, int min, int max) {
		if (min > max) 
			throw new IllegalArgumentException("The min parameter "
					+ "must be less than or equal to the max parameter.");
		int rand = gaussianInteger(mean, standardDeviation);
		if (rand < min)
			return min;
		if (rand > max)
			return max;
		return rand;
	}

	/**
	 * Calculates what quadrant an angle (in radians) is in. Uses the following
	 * conventions: <li>[0, pi/2) - quadrant I <li>[pi/2, pi) - quadrant II <li>
	 * [pi, 3pi/2) - quadrant III <li>[3pi/2, 2pi) - quadrant IV
	 * <p>
	 * For angles outside of [0, 2pi), equivalent coterminal angles are used.
	 * @param angle an angle, in radians
	 * @return the quadrant of angle
	 */
	public static int quadrant(double angle) {
		while (angle < 0)
			angle += 2 * Math.PI;
		while (angle > 2 * Math.PI)
			angle -= 2 * Math.PI;

		if (angle < Math.PI / 2)
			return 1;
		if (angle < Math.PI)
			return 2;
		if (angle < 3 * Math.PI / 2)
			return 3;
		return 4;
	}

	/**
	 * Returns the gravitational acceleration experienced by an object.
	 * @param location the location of the object experiencing the acceleration
	 * @param sourceLocation the location of where the source of gravity comes
	 *        from
	 * @param strength the strength (typically the mass) of the source
	 * @param g the gravitational constant
	 * @return the magnitude of the acceleration felt
	 */
	public static double getAcceleration(Point2d location,
			Point2d sourceLocation, double strength, double g) {
		return g * strength / location.getDistanceSquared(sourceLocation);
	}

	/**
	 * Calculates the angle (in radians), formed by (0,0) and (xDif, yDif).
	 * @param xDif the x value
	 * @param yDif the y value
	 * @return the angle, in the range [0, 2pi)
	 */
	public static double getAngle(double xDif, double yDif) {
		double angle = Math.atan(-yDif / xDif);
		if (xDif > 0)
			return angle;
		if (xDif < 0)
			return angle + Math.PI;
		if (yDif < 0)
			return Math.PI / 2;
		if (yDif > 0)
			return -Math.PI / 2;
		return 0;
	}

	/**
	 * Calculates the angle formed between two points. The angle is measured
	 * from the first point to the second, against the horizontal line on which
	 * the first point sits, measured from the right and counter-clockwise.
	 * @param p1 the first point
	 * @param p2 the second point
	 * @return the angle, in the the range [0, 2pi)
	 */
	public static double getAngle(Point2d p1, Point2d p2) {
		return getAngle(p2.x - p1.x, p2.y - p1.y);
	}

	/**
	 * Returns a new Color with each component randomly shifted by at most d.
	 * The values are adjusted so that the new Color is valid.
	 * @param color the base color
	 * @param maxDifference the maximum change for any one color
	 * @return a color randomly shifted by at most maxDifference
	 */
	public static Color getShiftedColor(Color color, int maxDifference) {
		int r = randomInteger(color.getRed() - maxDifference, color.getRed()
				+ maxDifference);
		if (r < 0 || r > 255)
			r = color.getRed();

		int g = randomInteger(color.getGreen() - maxDifference,
				color.getGreen() + maxDifference);
		if (g < 0 || g > 255)
			g = color.getGreen();

		int b = randomInteger(color.getBlue() - maxDifference, color.getBlue()
				+ maxDifference);
		if (b < 0 || b > 255)
			b = color.getBlue();

		return new Color(r, g, b);
	}

	/**
	 * Randomly calculates a double in the range [a, b) with a completely random
	 * distribution.
	 * @param a the minimum value (inclusive)
	 * @param b the maximum value (exclusive)
	 * @return random double in the range
	 */
	public static double randomDouble(double a, double b) {
		return Math.random() * (b - a) + a;
	}

	/**
	 * Randomly calculates an int in the range [a, b) with a completely random
	 * distribution.
	 * @param a the minimum value (inclusive)
	 * @param b the maximum value (exclusive)
	 * @return random int in the range
	 */
	public static int randomInteger(int a, int b) {
		return (int) Math.round(Math.random() * (b - a) + a);
	}

	/**
	 * Randomly returns -1 or 1.
	 * @return -1 or 1, randomly
	 */
	public static int randomSign() {
		int x = (int) Math.signum(Math.random() - 0.5);
		if (x == 0)
			return 1;
		return x;
	}

}