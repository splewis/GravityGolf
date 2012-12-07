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

	@Test
	public void distanceTest() {
		// Distance between (0,0) and (0,1) should be 1
		assertEquals(1, new Point2d(0, 0).getDistance(new Point2d(0, 1)),
				epsilon);

		// Distance between (1,1) and (2,2) should be sqrt 2
		assertEquals(Math.sqrt(2),
				new Point2d(1, 1).getDistance(new Point2d(2, 2)), epsilon);

		// Distance between (3,4) and (0,0) should be 5
		assertEquals(5, new Point2d(3, 4).getDistance(new Point2d(0, 0)),
				epsilon);

		// Distance between (1, 1) and (1,1) should be 0
		assertEquals(0, new Point2d(1, 1).getDistance(new Point2d(1, 1)),
				epsilon);
	}

	@Test
	public void distanceSquaredTest() {
		// Distance between (0,0) and (0,1) should be 1
		assertEquals(1, new Point2d(0, 0).getDistance(new Point2d(0, 1)),
				epsilon);

		// Distance^2 between (1,1) and (2,2) should be 2
		assertEquals(2.0,
				new Point2d(1, 1).getDistanceSquared(new Point2d(2, 2)),
				epsilon);

		// Distance^2 between (3,4) and (0,0) should be 25
		assertEquals(25,
				new Point2d(3, 4).getDistanceSquared(new Point2d(0, 0)),
				epsilon);

		// Distance^2 between (1, 1) and (1,1) should be 0
		assertEquals(0,
				new Point2d(1, 1).getDistanceSquared(new Point2d(1, 1)),
				epsilon);

		// Distance^2 between (100, 100) and (150,150) should be 2500
		assertEquals(
				2500,
				new Point2d(100, 100).getDistanceSquared(new Point2d(100, 150)),
				epsilon);
	}

}