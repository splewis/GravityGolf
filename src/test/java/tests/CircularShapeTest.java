package tests;

import static org.junit.Assert.assertEquals;
import java.awt.Color;
import org.junit.Test;
import structures.*;

/**
 * Tests for operations performed on a <code>CircularShape<\code> object.
 * @author Sean Lewis
 */
public class CircularShapeTest {

	@Test
	public void intersectionTestFalse() {
		Body b1 = new Body(100, 100, 20, Color.red);
		Body b2 = new Body(100, 150, 20, Color.red);
		assertEquals(false, b1.intersects(b2));

		Body b3 = new Body(100, 100, 20, Color.red);
		Ball b4 = new Ball(100, 150, 20, Color.red);
		assertEquals(false, b3.intersects(b4));

		Body b5 = new Body(100, 100, 20, Color.red);
		GoalPost b6 = new GoalPost(100, 150, 20);
		assertEquals(false, b5.intersects(b6));

		Ball b7 = new Ball(100, 115, 3, Color.red);
		GoalPost b8 = new GoalPost(100, 150, 20);
		assertEquals(false, b7.intersects(b8));
	}

	@Test
	public void intersectionTestTrue() {
		Body b1 = new Body(100, 100, 70, Color.red);
		Body b2 = new Body(100, 150, 20, Color.red);
		assertEquals(true, b1.intersects(b2));

		Body b3 = new Body(100, 100, 20, Color.red);
		Ball b4 = new Ball(100, 110, 20, Color.red);
		assertEquals(true, b3.intersects(b4));

		Body b5 = new Body(100, 100, 100, Color.red);
		GoalPost b6 = new GoalPost(100, 150, 20);
		assertEquals(true, b5.intersects(b6));

		Ball b7 = new Ball(100, 148, 3, Color.red);
		GoalPost b8 = new GoalPost(100, 150, 20);
		assertEquals(true, b7.intersects(b8));
	}

}