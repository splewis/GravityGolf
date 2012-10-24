package structures;

/**
 * 
 * @author Sean Lewis
 *
 */

public class Point2d {
			
	public double x;	
	public double y;
	
	
	 // Initializes a new Point2d at the origin, (0, 0).	 
	public Point2d() {
		x = 0.0;
		y = 0.0;	
	}	
	
	public Point2d(double x, double y) {
		this.x = x;
		this.y = y;	
	}
	
	public Point2d(java.awt.Point p) {
		this.x = p.x;
		this.y = p.y;	
	}	
	
	public java.awt.Point getIntegerPoint() {
		return new java.awt.Point((int) Math.round(x), (int) Math.round(y));
	}
	
	public double getDistance(Point2d p) {
		return Math.hypot(x - p.x, y - p.y);
	}
		
	public double getDistanceSquared(Point2d p) {
		return Math.pow(x - p.y, 2.0) + Math.pow(y - p.y, 2.0);
	}
	
	public Point2d translate(double dx, double dy) {
		return new Point2d(x + dx, y + dy);
	}
		
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	// Returns a mathematical, String representation of this Point2d
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	
	
}