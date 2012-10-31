package structures;

import game.DataReader;

import java.awt.Color;

/**
 * Represents a orbiting Body. Always attached to a parent body to which it
 * rotates around.
 * @author Sean Lewis
 */
public class Moon extends Body {
	// Constant rate of moon angular velocity (radians per update)
	private double deltaAngle = 0.0;
	// Parameters:
	private final int startingAngle;
	private final int startingDistance;
	// Higher parameters: (sourced from the body the moon is attached to)
	private final Point2d bodyCenter;
	private final int bodyMass;
	private final int bodyRadius;
	// Parameter-dependent:

	// Variables:
	private double currentAngle;

	/**
	 * @param initialAngle
	 * @param distanceFromBody
	 * @param radius
	 * @param color
	 * @param orbitingBody
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
	}

	/**
	 * Moves the moon based on the gravitational constant.
	 * @param g the gravitational constant
	 */
	public void move(double g) {
		if (deltaAngle == 0.0) {
			deltaAngle = .0075 * Math.sqrt(g * bodyMass
					/ (startingDistance + bodyRadius + radius));
			// F_c = F_g -> mv^2/r = GMm /r^2 -> v^2/r=GM/r^2 -> v^2 = GM/r
		}
		currentAngle -= deltaAngle;
		setCenter(bodyCenter.translate(Math.cos(currentAngle)
				* startingDistance, -Math.sin(currentAngle) * startingDistance));
	}

	/**
	 * Returns a description of this Moon.
	 */
	public String toString() {
		return "moon(" + startingAngle + ", " + startingDistance + ", "
				+ radius + ", " + DataReader.getColorDisplay(color) + ")";
	}

}