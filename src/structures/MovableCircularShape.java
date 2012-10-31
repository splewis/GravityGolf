package structures;

/**
 * Abstraction for Circular Shapes that are movable. Adds support for velocity.
 * @author Sean Lewis
 */
public abstract class MovableCircularShape extends CircularShape {

	/**
	 * The velocity vector for the object.
	 */
	protected Vector2d velocity;

	/**
	 * Moves the shape based on the current velocity vector.
	 */
	public void move() {
		move(velocity);
	}

	/**
	 * Moves the shape based on the parameter velocity vector.
	 * @param velocity the vector to move position by
	 */
	public void move(Vector2d velocity) {
		move(velocity, 1.0);
	}

	/**
	 * Moves the object by dt * velocity
	 * @param velocity the vector to move by
	 * @param dt the value to multiply velocity by when adding
	 */
	public void move(Vector2d velocity, double dt) {
		center.setX(center.getX() + dt * velocity.getXComponent());
		center.setY(center.getY() + dt * velocity.getYComponent());
	}

	/**
	 * Changes the velocity by adding an acceleration vector to it.
	 * @param acceleration the acceleration vector to add to velocity
	 */
	public void accelerate(Vector2d acceleration) {
		accelerate(acceleration, 1.0);
	}

	/**
	 * Changes the velocity by dt * acceleration
	 * @param acceleration the vector to accelerate by
	 * @param dt the value to multiply acceleration by when adding vectors
	 */
	public void accelerate(Vector2d acceleration, double dt) {
		velocity = velocity.add(acceleration.multiply(dt));
	}

	/**
	 * Returns the current velocity vector.
	 * @return the velocity
	 */
	public Vector2d getVelocity() {
		return velocity;
	}

	/**
	 * Sets the velocity vector.
	 * @param velocity the new velocity
	 */
	public void setVelocity(Vector2d velocity) {
		this.velocity = velocity;
	}

}