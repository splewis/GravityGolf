package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import structures.CalcHelp;
import structures.Point2d;

public class CalcHelpTest {

	static final double epsilon = 1e-8;

	@Test
	public void getAccelerationTest() {
		Point2d source = new Point2d(0, 0);

		Point2d loc = new Point2d(0, 1);
		assertEquals(1, CalcHelp.getAcceleration(source, loc, 1, 1), epsilon);

		loc = new Point2d(3, 4);
		assertEquals(10.0 * 20.0 / 25.0,
				CalcHelp.getAcceleration(source, loc, 10, 20), epsilon);

		loc = new Point2d(-5, -5);
		assertEquals(-5.0 * 3.0 / 50.0,
				CalcHelp.getAcceleration(source, loc, -5, 3), epsilon);

	}
}