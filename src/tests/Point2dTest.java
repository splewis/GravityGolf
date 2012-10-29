package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import structures.Point2d;

public class Point2dTest {

	static final double epsilon = 1e-8;
	
	@Test
	public void distanceTest() {
		assertEquals(Math.sqrt(2), new Point2d(1,1).getDistance(new Point2d(2, 2)), epsilon);
	}
	
}