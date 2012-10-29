package tests;

import static org.junit.Assert.*;

import org.junit.Test;
import structures.Vector2d;

public class Vector2dTest {

	static final double epsilon = 1e-8;
	
	@Test
	public void dotProductTests() {
		// <1,0> dot <0,1> should be 0
		assertEquals(0, new Vector2d(1, 0).dot(new Vector2d(0, 1)), epsilon);			
		
		// <1,1> dot <-1,-1> should be -2
		assertEquals(-2, new Vector2d(1, 1).dot(new Vector2d(-1, -1)), epsilon);	
		
		// <10,0> dot <0, 2> should be 0
		assertEquals(0, new Vector2d(10, 0).dot(new Vector2d(0, 2)), epsilon);	
		
		// <1,5> dot <5, -10> should be -45
		assertEquals(-45, new Vector2d(1, 5).dot(new Vector2d(5, -10)), epsilon);					
	}
	
	@Test
	public void projectionTests() {
		// <1,1>   in the direction of <3, 1>  = <6/5,  2/5>
		Vector2d v1 = new Vector2d(1, 1);
		Vector2d v2 = new Vector2d(3, 1);
		Vector2d proj = new Vector2d(1.2, 0.4);
		assertEquals(true, v1.projection(v2).equals(proj, epsilon));

		// <1,1> in the direction of <-1, -1>  = <1, 1>
		v1 = new Vector2d(1, 1);
		v2 = new Vector2d(-1, -1);
		proj = new Vector2d(1, 1);
		assertEquals(true, v1.projection(v2).equals(proj, epsilon));	

		// <1,0>  in the direction of <0, 1> = <0, 0>
		v1 = new Vector2d(1, 0);
		v2 = new Vector2d(0, 1);
		proj = new Vector2d(0, 0);
		assertEquals(true, v1.projection(v2).equals(proj, epsilon));			
	}
	
}