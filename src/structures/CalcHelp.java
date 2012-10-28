package structures;

import java.awt.Color;
import java.util.Random;

import structures.Body;

/**
 * Class that provides several frequently used methods across classes
 * @author Sean Lewis
 */

public final class CalcHelp {

	private static final Random randomGenerator = new Random();

	public static Color correctColor(int r, int g, int b) {
		int newR = r;
		if (r < 0)
			newR = 0;
		if (r > 255)
			newR = 255;

		int newG = g;
		if (g < 0)
			newG = 0;
		if (g > 255)
			newG = 255;

		int newB = b;
		if (b < 0)
			newB = 0;
		if (b > 255)
			newB = 255;

		return new Color(newR, newG, newB);
	}

	/**
	 * Returns the decoded value of value through the key.
	 * @param value the input value
	 * @param key the unique input key
	 * @return the decoded value
	 */
	public static int decode(long value, int key) {
		if (isPrime(key)) {
			return (int) ((value - key * key * key) / 3);
		}
		if (key % 6 == 0) {
			return (int) (value / (key + 5) - key);
		}
		if (key % 4 == 0) {
			return (int) (value - key) / -15;
		}
		if (key % 5 == 0) {
			return (int) Math.sqrt(value - key);
		}
		if (key % 7 == 0) {
			return (int) (value - key * key * key);
		}
		if (key % 2 == 0) {
			return (int) (value + key * key + 20) / 10;
		}
		return (int) (value + 6 * key * key) / 8;
	}

	/**
	 * Returns an encoded value based of the input value and unique key.
	 * @param value the input value
	 * @param key the unique input key
	 * @return the encoded value
	 */
	public static long encode(int value, int key) {
		if (isPrime(key)) {
			return (value * 3) + key * key * key;
		}
		if (key % 6 == 0) {
			return (value + key) * (key + 5);
		}
		if (key % 4 == 0) {
			return -15 * value + key;
		}
		if (key % 5 == 0) {
			return value * value + key;
		}
		if (key % 7 == 0) {
			return value + key * key * key;
		}
		if (key % 2 == 0) {
			return 10 * value - 20 - key * key;
		}
		return 8 * value - 6 * key * key;
	}

	public static boolean isPrime(int num) {
		if (num <= 1) {
			return false;
		}
		double sq = Math.sqrt(num);
		for (int i = 2; i <= sq; i++) {
			if (num % i == 0)
				return false;
		}
		return true;
	}

	public static double gaussianDouble(double mean, double standardDeviation) {
		return randomGenerator.nextGaussian() * standardDeviation + mean;
	}

	public static int gaussianInteger(double mean, double standardDeviation) {
		return (int) Math.round(randomGenerator.nextGaussian()
				* standardDeviation + mean);
	}

	public static int quadrant(double angle) {
		while (angle < 0)
			angle += 2 * Math.PI;
		while (angle > 0)
			angle -= 2 * Math.PI;

		if (angle < Math.PI / 2)
			return 1;
		if (angle < Math.PI)
			return 2;
		if (angle < 3 * Math.PI / 2)
			return 3;
		return 4;
	}

	public static double getAcceleration(Body b1, Body b2, double g) {
		return getAcceleration(b1.getCenter(), b2, g);
	}

	public static double getAcceleration(Point2d p1, Particle p, double g) {
		return g * p.getRadius() / getDistanceSquared(p1, p.getCenter()); // G
																			// m1
																			// m2
																			// /
																			// d^2,
																			// m1
																			// =
																			// 1,
																			// m2
																			// =
																			// radius
	}

	public static double getAcceleration(Point2d p, Body b, double g) {
		return g * b.getMass() / getDistanceSquared(p, b.getCenter()); // G m1
																		// m2 /
																		// d^2,
																		// m1 =
																		// 1, m2
																		// =
																		// radius
		/*
		 * Note: mass1 is ignored for the sake of calculation Because: F_net =
		 * m*a -> a = F_net / m (F_net = the sum of graviational forces = F_g)
		 * F_g = (G*m1*m2 / d^2) F_net = (G*m1*m2 / d^2) a = (G*m1*m2 / d^2) /
		 * m1 a = (G*m2 / d^2) Thus: the mass of the ball has no effect of the
		 * acceleration due to gravity upon it - thanks galileo!
		 */
	}

	public static double getAngle(double xDif, double yDif) {
		double angle = Math.atan(-yDif / xDif);
		if (xDif > 0) {
			return angle;
		}
		if (xDif < 0) {
			return angle + Math.PI;
		}
		return Math.signum(-yDif) * Math.PI / 2.0;
	}

	public static double getAngle(Point2d p1, Point2d p2) {
		return getAngle(p2.x - p1.x, p2.y - p1.y);
	}

	public static String getColorDisplay(Color c) {
		if (c.equals(Color.red))
			return "red";
		if (c.equals(Color.black))
			return "black";
		if (c.equals(Color.blue))
			return "blue";
		if (c.equals(Color.cyan))
			return "cyan";
		if (c.equals(Color.gray))
			return "gray";
		if (c.equals(Color.green))
			return "green";
		if (c.equals(Color.magenta))
			return "magenta";
		if (c.equals(Color.orange))
			return "orange";
		if (c.equals(Color.pink))
			return "pink";
		if (c.equals(Color.yellow))
			return "yellow";
		if (c.equals(new Color(128, 0, 128)))
			return "purple";
		if (c.equals(new Color(127, 0, 255)))
			return "violet";
		return "Color(" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue()
				+ ")";
	}

	public static double getDistance(Point2d p1, Point2d p2) {
		return Math.hypot(p1.x - p2.x, p1.y - p2.y);
	}

	public static double getDistance(java.awt.Point p1, java.awt.Point p2) {
		return Math.hypot(p1.getX() - p2.getX(), p1.getY() - p2.getY());
	}

	public static double getDistanceSquared(Point2d p1, Point2d p2) {
		return Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
	}

	public static Color getShiftedColor(Color c, int d) {
		int r = randomInteger(c.getRed() - d, c.getRed() + d);
		if (r < 0 || r > 255)
			r = c.getRed();

		int g = randomInteger(c.getGreen() - d, c.getGreen() + d);
		if (g < 0 || g > 255)
			g = c.getGreen();

		int b = randomInteger(c.getBlue() - d, c.getBlue() + d);
		if (b < 0 || b > 255)
			b = c.getBlue();

		return new Color(r, g, b);
	}

	public static boolean intersects(Point2d p1, Point2d p2, int r1, int r2) {
		return getDistanceSquared(p1, p2) < Math.pow((r1 + r2), 2);
	}

	public static boolean intersects(Point2d p1, Point2d p2, int r) {
		return getDistanceSquared(p1, p2) < r * r;
	}

	public static double randomDouble(double a, double b) {
		return Math.random() * (b - a) + a;
	}

	public static int randomInteger(int a, int b) {
		return (int) Math.round(Math.random() * (b - a) + a);
	}

	public static int randomInteger(double a, double b) {
		return (int) Math.round(Math.random() * (b - a) + a);
	}

	public static int randomSign() {
		int x = (int) Math.signum(Math.random() - 0.5);
		if (x == 0)
			return 1;
		return x;
	}

	public static Color readColor(String str) {
		str = str.toLowerCase();
		if (str.equals("red"))
			return Color.red;
		if (str.equals("black"))
			return Color.black;
		if (str.equals("blue"))
			return Color.blue;
		if (str.equals("cyan"))
			return Color.cyan;
		if (str.equals("gray"))
			return Color.gray;
		if (str.equals("green"))
			return Color.green;
		if (str.equals("magenta"))
			return Color.magenta;
		if (str.equals("orange"))
			return Color.orange;
		if (str.equals("pink"))
			return Color.pink;
		if (str.equals("yellow"))
			return Color.yellow;
		if (str.equals("purple"))
			return new Color(128, 0, 128);
		if (str.equals("violet"))
			return new Color(127, 0, 255);

		String r = str.substring(str.indexOf("(") + 1, str.indexOf(","));
		String g = str.substring(str.indexOf(",") + 1, str.lastIndexOf(","));
		String b = str.substring(str.lastIndexOf(",") + 1, str.indexOf(")"));

		return new Color(Integer.parseInt(r), Integer.parseInt(g),
				Integer.parseInt(b));
	}

}