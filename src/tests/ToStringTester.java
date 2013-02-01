package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import structures.*;

/**
 * Tests the functionality of all structures.*.toString() methods.
 * @author Sean Lewis
 */
public class ToStringTester {

	@Test
	public void ballToString() {
		Ball b = new Ball(1, -2, 10, Color.blue);
		assertEquals("ball(1, -2, 10, blue)" ,b.toString());
		b = new Ball(15, 100, 11, new Color(1,2,3));
		assertEquals("ball(15, 100, 11, Color(1/ 2/ 3))", b.toString());	
	}
	
	@Test
	public void bodyToString() {
		Body b = new Body(1, -2, 10, Color.blue, 100);
		assertEquals("body(1, -2, 10, blue, 100)" ,b.toString());
		b = new Body(15, 100, 11, new Color(1,2,3));
		assertEquals("body(15, 100, 11, Color(1/ 2/ 3), 11)", b.toString());
	}
	
	@Test
	public void goalToString() {
		GoalPost g = new GoalPost(100, 201, 15);
		assertEquals("goal(100, 201, 15)", g.toString());		
	}
	
	@Test
	public void blockageToString() {
		Blockage b = new Blockage(1, 2, 3, 4, Color.green);
		assertEquals("rect(1, 2, 3, 4, green)", b.toString());
		b = new Blockage(101, 201, 301, 401, new Color(100, 99, 98));
		assertEquals("rect(101, 201, 301, 401, Color(100/ 99/ 98))", b.toString());		
	}
	
	@Test
	public void levelToString() {
		Ball b = new Ball(100, 200, 3);
		Body bod1 = new Body(400, 500, 100, Color.green);
		Body bod2 = new Body(800, 700, 50, Color.cyan);
		List<Body> bodies = new ArrayList<Body>();
		bodies.add(bod1);
		bodies.add(bod2);
		GoalPost g1 = new GoalPost(100, 600, 30);
		List<GoalPost> goals = new ArrayList<GoalPost>();
		goals.add(g1);
		Level l = new Level(b, bodies, null, goals, null, 2.0, 3.0);
		String expected = "ball(100, 200, 3, red)\n";
		expected += "body(400, 500, 100, green, 100)\n";
		expected += "body(800, 700, 50, cyan, 50)\n";
		expected += "goal(100, 600, 30)\n";
		expected += "level(2.0, 3.0)\n";
		assertEquals(expected, l.toString());
	}
	
}