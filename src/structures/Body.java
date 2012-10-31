package structures;

import game.DataReader;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * A Body is the default class for an object that is representing a Planet.
 * @author Sean Lewis
 */
public class Body extends CircularShape {

	protected int mass;
	private ArrayList<Moon> moons;
	private boolean reflector = false;

	// for advanced drawing
	private Color[] colors;
	private float[] dist = { 0.93f, 0.97f, 1.0f };
	// 0 - 93 % pure color
	// 93 -97 % fade from color to white
	// 97 -100% fade from white to black
	private int extraRadius;

	/**
	 * 
	 */
	public Body() {
	}

	/**
	 * @param centerX
	 * @param centerY
	 * @param radius
	 * @param color
	 */
	public Body(int centerX, int centerY, int radius, Color color) {
		this(centerX, centerY, radius, color, radius);
		// if no mass specified, the mass is set to the radius
	}

	/**
	 * @param centerX
	 * @param centerY
	 * @param radius
	 * @param color
	 * @param mass
	 */
	public Body(int centerX, int centerY, int radius, Color color, int mass) {
		setCenter(new Point2d(centerX, centerY));
		setRadius(radius);
		setColor(color);
		this.mass = mass;
		
		moons = new ArrayList<Moon>();
		// TODO: put all advanced drawing calculation in a single method used by
		// moon and Body
		if (radius < 60) {
			dist[0] = 0.88f;
			dist[1] = 0.95f;
			extraRadius = (int) (radius / 8.0 - 3.0);
		} else if (radius > 150) {
			dist[0] = 0.95f;
			dist[1] = 0.98f;
			extraRadius = (int) ((dist[2] - dist[1]) * (radius / 2.0));
		}
		colors = new Color[3];
		colors[0] = color;
		colors[1] = Color.WHITE;
		colors[2] = Color.BLACK;
	}

	/**
	 * @param dx
	 * @param dy
	 * @param g
	 */
	public void advancedDraw(double dx, double dy, Graphics2D g) {
		if (radius < 10) {
			draw((int) Math.round(dx), (int) Math.round(dy), g);
		} else {
			g.setPaint(new RadialGradientPaint(new Point2D.Double(
					center.x + dx, center.y + dy), radius + extraRadius, dist,
					colors));
			g.fillOval((int) Math.round(center.x - radius + dx - extraRadius),
					(int) Math.round(center.y - radius + dy - extraRadius), diameter
							+ 2 * extraRadius + 1, diameter + 2 * extraRadius
							+ 1);
		}

	}

	/**
	 * @param m
	 */
	public void addMoon(Moon m) {
		moons.add(m);
	}

	/**
	 * @param b
	 */
	public void setReflector(boolean b) {
		reflector = b;
	}

	/**
	 * @return
	 */
	public boolean isReflector() {
		return reflector;
	}

	/**
	 * @return
	 */
	public int getMass() {
		return mass;
	}

	/**
	 * @return
	 */
	public ArrayList<Moon> getMoons() {
		return moons;
	}

	/**
	 * 
	 */
	public String toString() {
		String str = "body(";
		str += Math.round(center.x) + ", " + Math.round(center.y) + ", "
				+ radius + ", " + DataReader.getColorDisplay(color) + ")";
		for (Moon m : moons) {
			str += "\n" + m.toString();
		}
		return str;
	}

}