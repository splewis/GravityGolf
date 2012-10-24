package structures;

import java.awt.Color;
import java.awt.Graphics2D;

public class Particle {
	
	private double startX, startY;
	private Point2d center;
	private double centerX, centerY;
	private double radius;	
	private int diameter;	
	private Color color;	
	private double xSpeed, ySpeed;
	
	/*
	 * Creates a Body with center (x, y), diameter 1, xSpeed xs, ySpeed ys, and Color c
	 *   - Used for particles on collision
	 */
	public Particle(int x, int y, double xs, double ys, Color c) { 
		startX = x;
		startY = y;
		centerX = x;
		centerY = y;
		center = new Point2d(centerX, centerY);
		xSpeed = xs;
		ySpeed = ys;
		color = c;
		radius = 0.5;
		diameter = (int) (2 * radius);
	}
	
	public Particle(int x, int y, int radius, Color c) {
		startX = x;
		startY = y;
		centerX = x;
		centerY = y;
		center = new Point2d(centerX, centerY);
		color = c;
		this.radius = radius;
		diameter = 2 * radius;		
	}
	
	public void draw(double dx, double dy, Graphics2D g) {
		g.setColor(color);
		g.fillOval((int) Math.round(centerX - radius + dx),
			       (int) Math.round(centerY - radius + dy),
			       diameter, diameter);
	}	
	
	public Point2d getCenter() {
		return center;
	}

	public double getCenterX() {
		return centerX;
	}

	public double getCenterY() {
		return centerY;
	}

	public void multiplySpeed(double x) {
		xSpeed *= x;
		ySpeed *= x;
	}
	
	public void reset() {
		setLocation(startX, startY);
		setSpeed(0.0, 0.0);
	}

	public void updateLocation() {		
		centerX += xSpeed;
		centerY += ySpeed;
		center = new Point2d(centerX, centerY);
	}

	public void updateSpeed(double x, double y) {
		xSpeed += x;
		ySpeed += y;		
	}

	public double getRadius() {
		return radius;
	}	

	public double getXSpeed() {
		return xSpeed;
	}	

	public double getYSpeed() {
		return ySpeed;
	}	
	
	public Color getColor() {
		return color;
	}

	public void setLocation(double startX2, double startY2) {
		centerX = startX2;
		centerY = startY2;
		center = new Point2d(centerX, centerY);
	}

	public void setSpeed(double x, double y) {
		xSpeed = x;
		ySpeed = y;
	}

	public void setXSpeed(double s) {
		xSpeed = s;
	}

	public void setYSpeed(double s) {
		ySpeed = s;
	}		
	
}