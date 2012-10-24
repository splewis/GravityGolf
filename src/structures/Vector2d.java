package structures;

/**
 * 
 * @author Sean Lewis
 *
 */

public class Vector2d {
			
	private double xComp;
	private double yComp;
	private double length;
	private double angle;
	
	/**
	 * Initializes a new Vector2d that is a unit vector along the x-axis (1i + 0j).
	 */
	public Vector2d() {
		this(1.0, 0.0);
	}
	
	public Vector2d(double x, double y) {
		xComp = x;
		yComp = y;
		calculateProperties();
	}
	
	public Vector2d(double magnitude, double angle, boolean dummy) {
		length = magnitude;
		this.angle = angle;
		calculateComponenets();
	}
	
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
	
	public Vector2d add(Vector2d v) {
		return new Vector2d(xComp + v.getXComponent(), yComp + v.getYComponent());
	}
	
	public Vector2d subtract(Vector2d v) {
		return new Vector2d(xComp - v.getXComponent(), yComp - v.getYComponent());
	}
	
	public Vector2d negate() {
		return new Vector2d(-xComp, -yComp);
	}
	
	public Vector2d multiply(double k) {
		return new Vector2d(xComp * k, yComp * k);
	}
	
	public Vector2d divide(double k) {
		return new Vector2d(xComp / k, yComp / k);
	}
	
	public double dot(Vector2d v) {
		return xComp * v.getXComponent() + yComp * v.getYComponent();
	}

	public Vector2d normalize() {
		return new Vector2d(xComp / length, yComp / length);
	}
	
	public double angleBetween(Vector2d v) {
		return Math.acos(dot(v) / (length * v.getLength()));
	}
	
	public Vector2d projection(Vector2d v) {
		return v.multiply(this.dot(v) / (v.dot(v)));
			
	}
	
	public Vector2d projection(double t) {
		return new Vector2d(length * Math.cos(angle - t), t, true);	
		//return projection(new Vector2d(Math.cos(t), Math.sin(t)));
	}
	
	public double getXComponent() {
		return xComp;
	}
	
	public void setXComponent(double xComponent) {
		this.xComp = xComponent;
		calculateProperties();
	}

	public double getYComponent() {
		return yComp;
	}
	
	public void setYComponent(double yComponent) {
		this.yComp = yComponent;
		calculateProperties();
	}

	public double getLength() {
		return length;
	}
	
	public void setLength(double length) {
		this.length = length;
		calculateComponenets();		
	}

	public double getAngle() {
		return angle;
	}
	
	public void setAngle(double angle) {
		this.angle = angle;
		calculateComponenets();
	}

	public String toString() {
		return "<" + xComp + "i" + ((yComp >= 0) ? (" + ") : (" - ")) + Math.abs(yComp) + "j>";
	}

}
