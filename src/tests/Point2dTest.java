package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import structures.Point2d;

/**
 * JUnit test suite for non-trivial operations on <code>Point2d</code> objects.
 * @author Sean Lewis
 */
public class Point2dTest {

	static final double epsilon = 1e-8;

	// helper method to create a point
	private Point2d p(double x, double y) {
		return new Point2d(x, y);
	}

	@Test
	public void originVerfication() {
		// test on x/y origin values
		assertEquals(0.0, Point2d.ORIGIN.x, epsilon);
		assertEquals(0.0, Point2d.ORIGIN.y, epsilon);
		assertEquals(true, Point2d.ORIGIN.equals(new Point2d(0, 0), epsilon));

		// check that constructor yields origin coordinate
		assertEquals(true, Point2d.ORIGIN.equals(new Point2d(), epsilon));
	}

	@Test
	public void integerPointTest() {
		assertEquals(new java.awt.Point(1, 3),
				new Point2d(1.2, 2.6).getIntegerPoint());
		assertEquals(new java.awt.Point(0, -1),
				new Point2d(.4, -1.1).getIntegerPoint());
		assertEquals(new java.awt.Point(-9, 15),
				new Point2d(-9.5, 15.4).getIntegerPoint());
	}

	@Test
	public void distanceTest() {
		// Distance between (0,0) and (0,1) should be 1
		assertEquals(1, p(0, 0).getDistance(p(0, 1)), epsilon);

		// Distance between (1,1) and (2,2) should be sqrt 2
		assertEquals(Math.sqrt(2), p(1, 1).getDistance(p(2, 2)), epsilon);

		// Distance between (3,4) and (0,0) should be 5
		assertEquals(5, p(3, 4).getDistance(p(0, 0)), epsilon);

		// Distance between (1, 1) and (1,1) should be 0
		assertEquals(0, p(1, 1).getDistance(p(1, 1)), epsilon);
	}

	@Test
	public void distanceSquaredTest() {
		// Distance between (0,0) and (0,1) should be 1
		assertEquals(1, p(0, 0).getDistance(p(0, 1)), epsilon);

		// Distance^2 between (1,1) and (2,2) should be 2
		assertEquals(2.0, p(1, 1).getDistanceSquared(p(2, 2)), epsilon);

		// Distance^2 between (3,4) and (0,0) should be 25
		assertEquals(25, p(3, 4).getDistanceSquared(p(0, 0)), epsilon);

		// Distance^2 between (1, 1) and (1,1) should be 0
		assertEquals(0, p(1, 1).getDistanceSquared(p(1, 1)), epsilon);

		// Distance^2 between (100, 100) and (150,150) should be 2500
		assertEquals(2500, p(100, 100).getDistanceSquared(p(100, 150)), epsilon);
	}

	@Test
	public void withinDist() {
		assertEquals(true, p(0,0).withinDistance(p(1, 1), 5));
		assertEquals(true, p(-5,-5).withinDistance(p(5, 5), 50));
		assertEquals(true, p(2,-3).withinDistance(p(3, 5), 11));
		assertEquals(false, p(0,0).withinDistance(p(1, 1), 1));
		assertEquals(false, p(1,2).withinDistance(p(3, 4), 1));
	}
}