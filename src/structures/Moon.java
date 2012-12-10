package structures;

import game.DataHandler;

import java.awt.Color;

/**
 * Represents a orbiting Body. Always attached to a parent body to which it
 * rotates around.
 * @author Sean Lewis
 */
public class Moon extends Body {
	// Parameters:
	private final int startingAngle;
	private final int startingDistance;

	// Higher parameters: (sourced from the body the moon is attached to)
	private final Point2d bodyCenter;
	private final int bodyMass;
	private final int bodyRadius;

	// Variables:
	private double currentAngle;

	/**
	 * Creates a new moon.
	 * @param initialAngle the initial angle formed from the Body the moon is
	 *        attached to
	 * @param distanceFromBody the distance the moon will stay at from the Body
	 * @param radius the radius of the Moon
	 * @param color the Color of the Moon
	 * @param orbitingBody the Body the Moon will orbit
	 */
	public Moon(int initialAngle, int distanceFromBody, int radius,
			Color color, Body orbitingBody) {
		startingAngle = initialAngle;
		currentAngle = Math.toRadians(startingAngle);
		startingDistance = distanceFromBody;
		setColor(color);
		setRadius(radius);
		bodyCenter = orbitingBody.getCenter();
		bodyRadius = (int) orbitingBody.getRadius();
		bodyMass = (int) orbitingBody.getMass();
		setCenter(bodyCenter.translate(Math.cos(currentAngle)
				* distanceFromBody, -Math.sin(currentAngle) * distanceFromBody));
		computeColoring();
	}

	/**
	 * Moves the moon based on the gravitational constant.
	 * @param g the gravitational constant
	 */
	public void move(double g) {
		double deltaAngle = .0075 * Math.sqrt(g * bodyMass
				/ (startingDistance + bodyRadius + radius));
		// F_c = F_g -> mv^2/r = GMm /r^2 -> v^2/r=GM/r^2 -> v^2 = GM/r

		currentAngle -= deltaAngle;
		setCenter(bodyCenter.translate(Math.cos(currentAngle)
				* startingDistance, -Math.sin(currentAngle) * startingDistance));
	}

	/**
	 * Returns a description of this Moon.
	 */
	public String toString() {
		return "moon(" + startingAngle + ", " + startingDistance + ", "
				+ radius + ", " + DataHandler.getColorDisplay(color) + ")";
	}
	
	public int hashCode() {
		return toString().hashCode();
	}
}