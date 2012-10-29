package structures;

import game.DataReader;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * @author Sean
 */
public class Ball extends Body {

	private int startX, startY;
	private double xSpeed, ySpeed;
	private boolean launched;

	public Ball() {

	}

	public Ball(int centerX, int centerY, int radius, Color c) {
		startX = centerX;
		startY = centerY;
		center = new Point2d(centerX, centerY);
		color = c;
		this.radius = radius;
		initializeVars();
	}

	public ArrayList<Particle> generateParticles(Body body) {
		double cX = center.x;
		double cY = center.y;
		double speed = Math.hypot(xSpeed, ySpeed)
				* ((body != null) ? (body.getRadius() / 100.0) : 1);
		if (speed < .50)
			speed = .50;
		if (speed > 2.50)
			speed = 2.50;
		if (body != null) {
			while (CalcHelp.intersects(new Point2d(cX, cY), body.getCenter(),
					body.getRadius(), radius)) {
				cX -= xSpeed;
				cY -= ySpeed;
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

	public boolean isLaunched() {
		return launched;
	}

	public void setLaunched(boolean b) {
		launched = b;
	}

	public void resetLocation() {
		center.x = startX;
		center.y = startY;
	}

	public Point2d getCenter() {
		return center;
	}

	public double getXSpeed() {
		return xSpeed;
	}

	public double getYSpeed() {
		return ySpeed;
	}

	public Vector2d getVelocity() {
		return new Vector2d(xSpeed, ySpeed);
	}

	public void setVelocity(Vector2d v) {
		xSpeed = v.getXComponent();
		ySpeed = v.getYComponent();
	}

	public void setSpeed(double xSpeed, double ySpeed) {
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}

	public void setXSpeed(double xSpeed) {
		this.xSpeed = xSpeed;
	}

	public void setYSpeed(double ySpeed) {
		this.ySpeed = ySpeed;
	}

	public void updateLocation() {
		center.x += xSpeed;
		center.y += ySpeed;
	}

	public void updateLocation(double dx, double dy) {
		center.x += dx;
		center.y += dy;
	}

	public void updateSpeed(double dx, double dy) {
		xSpeed += dx;
		ySpeed += dy;
	}

	public void setLocation(double x, double y) {
		center.x = x;
		center.y = y;
	}

	public String toString() {
		String str = "ball(";
		str += startX + ", " + startY + ", " + radius + ", "
				+ DataReader.getColorDisplay(color) + ")";
		return str;
	}

}