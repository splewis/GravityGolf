package structures;

import game.DataReader;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * @author Sean
 */
public class Ball extends MovableCircularShape {

	private final Point2d startingLocation;
	private boolean launched;

	/**
	 * 
	 */
	public Ball() {
		this(0, 0, 3, Color.red);
	}

	/**
	 * 
	 * @param centerX
	 * @param centerY
	 * @param radius
	 * @param c
	 */
	public Ball(int centerX, int centerY, int radius, Color c) {
		center = new Point2d(centerX, centerY);
		startingLocation = new Point2d(centerX, centerY);
		velocity = new Vector2d(0, 0);
		color = c;
		this.radius = radius;
		initializeVars();
	}

	/**
	 * 
	 * @param body
	 * @return
	 */
	public ArrayList<Particle> generateParticles(Body body) {
		double cX = center.x;
		double cY = center.y;
		double speed = velocity.getLength()
				* ((body != null) ? (body.getRadius() / 100.0) : 1);
		if (speed < .50)
			speed = .50;
		if (speed > 2.50)
			speed = 2.50;
		if (body != null) {
			while (CalcHelp.intersects(new Point2d(cX, cY), body.getCenter(),
					body.getRadius(), radius)) {
				cX -= velocity.getXComponent();
				cY -= velocity.getYComponent();
			}
		}

		ArrayList<Particle> particles = new ArrayList<Particle>();
		int num = (int) (300 * speed);
		if (num < 150) {
			num = 150;
		} else if (num > 400) {
			num = 400;
		}

		for (int i = 0; i < num; i++) {

			double angle = (Math.random() * 2 * Math.PI);
			int x = (int) cX;
			int y = (int) cY;
			double xSpeed = 0.75 * (Math.random() * speed * Math.cos(angle));
			double ySpeed = 0.75 * (Math.random() * speed * Math.sin(angle));
			Color c;
			double randomInteger = (Math.random());
			if (body == null) {
				c = CalcHelp.getShiftedColor(color, 100);
			} else if (randomInteger < 0.3) {
				c = CalcHelp.getShiftedColor(body.getColor(), 100);
			} else if (randomInteger < 0.40) {
				c = CalcHelp.getShiftedColor(new Color(230, 130, 70), 100);
			} else {
				c = CalcHelp.getShiftedColor(color, 100);
			}

			Particle newParticle = new Particle(x, y, xSpeed, ySpeed, c);
			particles.add(newParticle);

		}
		return particles;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isLaunched() {
		return launched;
	}

	/**
	 * 
	 * @param b
	 */
	public void setLaunched(boolean b) {
		launched = b;
	}

	/**
	 * 
	 */
	public void resetLocation() {
		center = startingLocation;
	}

	/**
	 * 
	 */
	public String toString() {
		String str = "ball(";
		str += startingLocation.x + ", " + startingLocation.y + ", " + radius
				+ ", " + DataReader.getColorDisplay(color) + ")";
		return str;
	}

}