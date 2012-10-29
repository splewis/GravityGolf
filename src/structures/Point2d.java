package structures;

/**
 * @author Sean Lewis
 */
public class Point2d {

	/**
	 * The origin point (0,0).
	 */
	public static final Point2d ORIGIN = new Point2d(0.0, 0.0);

	/**
	 * The x-coordinate of this point.
	 */
	public double x;

	/**
	 * The y-coordinate of this point.
	 */
	public double y;

	/**
	 * Creates a new point at (0.0).
	 */
	public Point2d() {
		x = 0.0;
		y = 0.0;
	}

	/**
	 * Creates the point at (x,y)
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 */
	public Point2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Creates a new point based on p's coordinates
	 * @param p a java.awt.Point
	 */
	public Point2d(java.awt.Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	/**
	 * Creates a integer-based point by rounding this point's coordinates.
	 * @return a point with rounded coordinates
	 */
	public java.awt.Point getIntegerPoint() {
		return new java.awt.Point((int) Math.round(x), (int) Math.round(y));
	}

	/**
	 * Returns the he distance between this and another point.
	 * @param p another point
	 * @return the the distance between points.
	 */
	public double getDistance(Point2d p) {
		return Math.hypot(x - p.x, y - p.y);
	}

	/**
	 * Returns the square of the distance between this and another point.
	 * @param p another point
	 * @return the square of the distance between points.
	 */
	public double getDistanceSquared(Point2d p) {
		return Math.pow(x - p.y, 2.0) + Math.pow(y - p.y, 2.0);
	}

	/**
	 * Returns the translation of this point by dx and dy.
	 * @param dx the x-difference
	 * @param dy the y-difference
	 * @return the translated point
	 */
	public Point2d translate(double dx, double dy) {
		return new Point2d(x + dx, y + dy);
	}

	/**
	 * Returns the x component
	 * @return the x component
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets the x component
	 * @param x a parameter
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Returns the y component
	 * @return the y component
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the y component
	 * @param y a parameter
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Returns a mathematical, String representation of this point in the form
	 * (x, y).
	 */
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/**
	 * Returns if two points have the same perpendicular components, to within
	 * epsilon precision.
	 * @param p a point
	 * @param epsilon maximum allowed difference
	 * @return if the objects represent the same point
	 */
	public boolean equals(Point2d p, double epsilon) {
		double xDif = x - p.x;
		double yDif = y - p.y;
		return Math.abs(xDif) <= epsilon && Math.abs(yDif) <= epsilon;
	}

}