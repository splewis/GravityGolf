package structures;

/**
 * @author Sean Lewis
 */
public abstract class MovableCircularShape extends CircularShape {

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
		center.setX(center.getX() + velocity.getXComponent());
		center.setY(center.getY() + velocity.getYComponent());
	}

	/**
	 * Changes the velocity by adding an acceleration vector to it.
	 * @param acceleration the acceleration vector to add to velocity
	 */
	public void accelerate(Vector2d acceleration) {
		velocity = velocity.add(acceleration);
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