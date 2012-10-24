/**
 * A Body is the default class for an object that is representing a Planet.
 *
 *
 * @author Sean Lewis
 * @version 1.00 2011/7/26
 */ 

package structures;
 
import general.Point2d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Body {

	protected Color color;	
	protected int radius;	
	protected int radiusSq;
	protected int diameter;	

	private int centerX, centerY;
	private int leftX, topY;	
	private int mass;
	private Point2d center;
	private ArrayList<Moon> moons; 
	
	private boolean reflector = false;
	
	// for advanced drawing
	private Color[] colors;
	private float[] dist = {0.93f, 0.97f, 1.0f};
	// 0 - 93 % pure color
	// 93 -97 % fade from color to white
	// 97 -100% fade from white to black
	private int extraRadius;
			

	public Body() {		
	}	

	public Body(int centerX, int centerY, int radius, Color color) {
		this(centerX, centerY, radius, color, radius);
		// if no mass specified, the mass is set to the radius
	}

	public Body(int centerX, int centerY, int radius, Color color, int mass) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		radiusSq = radius * radius;
		this.color = color;	
		this.mass = mass;
		initializeComponents();	
	}		
	 		
	private void initializeComponents() {		
		leftX = centerX - radius;
		topY = centerY - radius;
		diameter = 2 * radius;		
		moons = new ArrayList<Moon>();	
		center = new Point2d(centerX, centerY);
		if(radius < 60) {
			dist[0] = 0.88f;
			dist[1] = 0.95f;
			extraRadius = radius / 8 - 3;
		} else if(radius > 150) {
			dist[0] = 0.95f;
			dist[1] = 0.98f;
			extraRadius = (int) ((dist[2] - dist[1]) * (radius / 2.0));
		}
		colors = new Color[3];
		colors[0] = color;
		colors[1] = Color.WHITE;
		colors[2] = Color.BLACK;
		
	}	

	public void advancedDraw(double dx, double dy, Graphics2D g) {
		if(radius < 10) {
			basicDraw((int) Math.round(dx), (int) Math.round(dy), g);
		}
		else {
			g.setPaint(new RadialGradientPaint(new Point2D.Double(centerX + dx, centerY + dy), radius + extraRadius, dist, colors));
			g.fillOval((int) Math.round(leftX + dx - extraRadius), (int) Math.round(topY + dy - extraRadius), 
						diameter + 2*extraRadius + 1, diameter + 2*extraRadius + 1);			
		}
		
	}
	
	public void basicDraw(int dx, int dy, Graphics2D g) {
		g.setColor(color);
		g.fillOval( (int)(leftX + dx), (int)(topY + dy), diameter, diameter);
	}

	public void addMoon(Moon m) {
		moons.add(m);
	}	

	public void setReflector(boolean b) {
		reflector = b;
	}
	
	public boolean isReflector() {
		return reflector;
	}
	
	public double getCenterX() {
		return centerX;
	}

	public double getCenterY() {
		return centerY;
	}

	public Point2d getCenter() {
		return center;
	}

	public Color getColor() {			
		return color;
	}

	public int getRadiusSq() {
		return radiusSq;
	}
	
	public int getDiameter() {
		return diameter;
	}			
	
	public int getMass() {
		return mass;
	}

	public ArrayList<Moon> getMoons() {
		return moons;
	}

	public int getRadius() {
		return radius;
	}

	public void setLocation(int x, int y) {
		centerX = x;
		centerY = y;	
		leftX = centerX - radius;
		topY = centerY - radius;
	}	

	public String toString() {		
		String str = "body(";		
		str += Math.round(centerX) + ", " + Math.round(centerY)+ ", " + radius + ", " + CalcHelp.getColorDisplay(color) + ")";
		for(Moon m: moons) {
			str += "\n" + m.toString();
		}		
		return str;
	}	
	
}