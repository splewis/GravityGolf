package structures;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *  
 * @author Sean Lewis
 *
 */
public class GoalPost {
	
	private static final Color GoalPostColor = Color.WHITE;
	
	private int centerX, centerY;
	private int radius;		
	private int diameter;
	private Point2d center;
	private int drawX, drawY;

	public GoalPost(int x, int y, int r) {
		centerX = x;
		centerY = y;	
		radius = r;		
		initializeComponents();	
	}

	private void initializeComponents() {		
		drawX = centerX - radius;
		drawY = centerY - radius;
		center = new Point2d(centerX, centerY);
		diameter = 2 * radius;		
	}	

	public void draw(int dx, int dy, Graphics2D g) {
		g.setColor( GoalPostColor );
		g.fillOval(drawX + dx, drawY + dy, diameter,  diameter);
	}
	
	public void draw(Graphics2D g) {
		draw(0, 0, g);
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public int getRadius() {
		return radius;
	}
	
	public int getDiameter() {
		return diameter;
	}	
	
	public Point2d getCenter() {
		return center;
	}	

	public int getDrawX() {
		return drawX;
	}

	public int getDrawY() {
		return drawY;
	}	

	public String toString() {
		return "goal(" + centerX +", " + centerY+", " + radius + ")";		
	}
	
}