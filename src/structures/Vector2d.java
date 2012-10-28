package structures;

/**
 * Representation of a two dimensional mathematical vector. Supports typical
 * operations on vectors in an immutable fashion. <li>Note that the angle of a
 * vector is defined as being measured in the counter-clockwise direction on the
 * x-axis. <li>Angles are always stored in radians.
 * @author Sean Lewis
 */

public class Vector2d {

	private double xComp;
	private double yComp;
	private double length;
	private double angle;

	/**
	 * Creates a zero vector.
	 */
	public Vector2d() {
		this(0.0, 0.0);
	}

	/**
	 * Creates a new vector by defining its perpendicular components.
	 * @param xComponent the magnitude of the horizontal component
	 * @param yComponent the magnitude of the vertical component
	 */
	public Vector2d(double xComponent, double yComponent) {
		xComp = xComponent;
		yComp = yComponent;
		calculateProperties();
	}

	/**
	 * Creates a new vector by defining its magnitude and direction
	 * @param magnitude the magnitude of the vector
	 * @param angle the angle of the vector
	 * @param dummy dummy to differentiate between constructors
	 */
	public Vector2d(double magnitude, double angle, boolean dummy) {
		length = magnitude;
		this.angle = angle;
		calculateComponenets();
	}

	/**
	 * Creates a new vector with an initial and terminal point.
	 * @param p1 the initial point
	 * @param p2 the terminal point
	 */
	public Vector2d(Point2d p1, Point2d p2) {
		xComp = p2.x - p1.x;
		yComp = p2.y - p1.y;
		calculateProperties();
	}

	private void calculateComponenets() {
		xComp = length * Math.cos(angle);
		yComp = length * Math.sin(angle);
	}

	private void calculateProperties() {
		angle = CalcHelp.getAngle(xComp, yComp);
		length = Math.hypot(xComp, yComp);
	}

	/**
	 * Returns the vector sum of this and another vector.
	 * @param v a vector
	 * @return the sum
	 */
	public Vector2d add(Vector2d v) {
		return new Vector2d(xComp + v.getXComponent(), yComp
				+ v.getYComponent());
	}

	/**
	 * Returns the vector difference of this and another vector.
	 * @param v a vector
	 * @return the difference
	 */
	public Vector2d subtract(Vector2d v) {
		return new Vector2d(xComp - v.getXComponent(), yComp
				- v.getYComponent());
	}

	/**
	 * Returns the scalar multiplication of this and a constant.
	 * @param k a constant
	 * @return the product
	 */
	public Vector2d multiply(double k) {
		return new Vector2d(xComp * k, yComp * k);
	}

	/**
	 * Returns the dot product (scalar multiplication) of this and another
	 * vector
	 * @param v a vector
	 * @return the dot product
	 */
	public double dot(Vector2d v) {
		return xComp * v.getXComponent() + yComp * v.getYComponent();
	}

	/**
	 * Returns the positive, acute angle between this and another vector.
	 * @param v a vector
	 * @return the angle between the vectors
	 */
	public double angleBetween(Vector2d v) {
		return Math.acos(dot(v) / (length * v.getLength()));
	}

	/**
	 * Returns the projection of this vector onto v
	 * @param v a vector
	 * @return the projection onto v
	 */
	public Vector2d projection(Vector2d v) {
		return v.multiply(this.dot(v) / (v.dot(v)));

	}

	/**
	 * Returns the projection of this vector in the direction of an angle
	 * @param t an angle
	 * @return the projection directed in t's direction
	 * @deprecated use the projection method with vector parameter instead
	 */
	public Vector2d projection(double t) {
		return new Vector2d(length * Math.cos(angle - t), t, true);
	}

	/**
	 * Returns the horizontal component of this vector
	 * @return the horizontal component
	 */
	public double getXComponent() {
		return xComp;
	}

	/**
	 * Sets the horizontal component of this vector
	 * @param xComponent the new horizontal component
	 */
	public void setXComponent(double xComponent) {
		this.xComp = xComponent;
		calculateProperties();
	}

	/**
	 * Returns the vertical component of this vector
	 * @return the vertical component
	 */
	public double getYComponent() {
		return yComp;
	}

	/**
	 * Sets the vertical component of this vector
	 * @param yComponent the new vertical component
	 */
	public void setYComponent(double yComponent) {
		this.yComp = yComponent;
		calculateProperties();
	}

	/**
	 * Returns the magnitude of this vector
	 * @return the magnitude
	 */
	public double getLength() {
		return length;
	}

	/**
	 * Sets the magnitude of this vector
	 * @param length the new magnitude
	 */
	public void setLength(double length) {
		this.length = length;
		calculateComponenets();
	}

	/**
	 * Returns the angle of this vector
	 * @return the angle
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Sets the angle of this vector
	 * @param angle the new angle
	 */
	public void setAngle(double angle) {
		this.angle = angle;
		calculateComponenets();
	}

	/**
	 * Returns a mathematical description of this vector with unit vector
	 * notation.
	 */
	public String toString() {
		return "<" + xComp + "i" + ((yComp >= 0) ? (" + ") : (" - "))
				+ Math.abs(yComp) + "j>";
	}

}
