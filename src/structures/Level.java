package structures;

import game.GamePanel;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A Level is the generic structure for each level's storage information.
 * @author Sean Lewis
 *
 */
public class Level {
	
	public static int numLevels = 0;
	
	private int levelIndex = 0;
	
	/**
	 * A constant for how many stars should be put in the level. As the number increased, there are more stars.
	 */
	public static final int StarFactor = 7000; 
	
	private static final int extraX = 100;
	private static final int extraY = 100;

	private Ball ball;
	private final ArrayList<Body> bodies;
	private final ArrayList<GoalPost> goals;
	private final ArrayList<WarpPoint> warps;
	private final ArrayList<Blockage> blockages;
	private final ArrayList<Rectangle> blockageRects;
	private final double followFactor, gravityStrength;

	private double screenXShift, screenYShift;
	private boolean ballInWarp, hittingBlockage;
	private ArrayList<Star> stars;	
	
	private BufferedImage image;
	private ArrayList<java.awt.Point> solutions;
	
	public int xMin;
	public int xMax;
	public int yMin;
	public int yMax;
	
	
	/**
	 * Creates a new Level with blank values.
	 */
	public Level() {
		ball = new Ball();
		bodies = new ArrayList<Body>();
		goals = new ArrayList<GoalPost>();
		warps = new ArrayList<WarpPoint>();
		blockages = new ArrayList<Blockage>();
		blockageRects = new ArrayList<Rectangle>();
		followFactor = 0.0;
		gravityStrength = 0.0;
	}
	
	public Level(Ball b, ArrayList<Body> bod, ArrayList<WarpPoint> ws, ArrayList<GoalPost> gs, ArrayList<Blockage> bs, double ff, double g) {
		levelIndex = numLevels;
		numLevels++;
		ball = b;
		bodies = bod;
		goals = gs;
		warps = ws;
		blockages = bs;
		blockageRects = new ArrayList<Rectangle>(); 
		for(Blockage bl: blockages) {
			blockageRects.add(new Rectangle(bl.getDrawX(), bl.getDrawY(), bl.getDrawXSize(), bl.getDrawYSize()));
		}		
		followFactor = ff;
		gravityStrength = g;
		screenXShift = (followFactor == 0) ? 0 : ((500 - ball.getCenterX()) / followFactor);
		screenYShift = (followFactor == 0) ? 0 : ((350 - ball.getCenterY()) / followFactor);	
	}	
	
	public boolean isInitialized() {
		return image != null;
	}	
	
	public void generateLevelData() {	
		
		stars = new ArrayList<Star>();
		if(followFactor == 0) {
			// arbitrary values, see formula below - using 1 would lead to division by 0, and using numbers <1.25 would make a huge area
			xMin = -extraX;
			xMax = GamePanel.Width + extraX;
			yMin = -extraY;
			yMax = GamePanel.Height + extraY;
		} 
		else if(followFactor < 1.5) {
			// arbitrary values, see formula below - using 1 would lead to division by 0, and using numbers <1.25 would make a huge area
			xMin = -1500;
			xMax = 1500;
			yMin = -1200;
			yMax = 1200;
		} else {
			double oneMinusInverse = 1.0 - 1.0 / followFactor;
			xMin = (int) Math.floor((-GamePanel.Width/2  / followFactor) 	   				 / oneMinusInverse);	
			xMax = (int) Math.ceil( (-GamePanel.Width/2  / followFactor + GamePanel.Width)   / oneMinusInverse);	
			yMin = (int) Math.floor((-GamePanel.Height/2 / followFactor)		  			 / oneMinusInverse);	
			yMax = (int) Math.ceil( (-GamePanel.Height/2 / followFactor + GamePanel.Height)  / oneMinusInverse);		
		}
		
		double area = (xMax - xMin) * (yMax - yMin) + 10000;		
		int numStars = (int) (area / StarFactor);		
		for(int i = 0; i < numStars; i++) {
			int x = CalcHelp.randomInteger(xMin - extraX, xMax + extraX);
			int y = CalcHelp.randomInteger(yMin - extraY, yMax + extraY);
			stars.add(new Star(x, y));	
		}
		
		image = new BufferedImage(xMax - xMin + extraX * 2, yMax - yMin+ extraY * 2, BufferedImage.TYPE_INT_RGB);				
		Graphics2D g = (Graphics2D) image.getGraphics();				
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		for(Star s: stars) {
			s.draw(-xMin + extraX, -yMin + extraY, g);
		}	
		for(WarpPoint w: warps) {
			w.draw(-xMin + extraX, -yMin + extraY, g);
		}	
		for(Body b: bodies) {
			b.advancedDraw(-xMin + extraX, -yMin+ extraY, g);
		}		
		for(Blockage b: blockages) {
			b.draw(-xMin + extraX, -yMin + extraY, g);
		}		
		for(GoalPost gp: goals) {
			gp.draw(-xMin + extraX, -yMin+ extraY, g);
		}				
	}	
	
	public ArrayList<Star> getStars() {
		return stars;
	}
		
	public void draw(Graphics2D g) {		
		draw(0, 0, g);
	}
	
	public void draw(int xs, int ys, Graphics2D g) {		
		g.drawImage(image, xs + xMin - extraX, ys + yMin - extraY, null);
	}	
	
	public void clearBallLocation() {
		while(getBodyIntersection() != null || isOutOfBounds()) {
			ball.updateLocation(-ball.getXSpeed() / 10.0, -ball.getYSpeed() / 10.0);
		}
	}	

	public Ball getBall() {
		return ball;
	}				
	
	public ArrayList<Body> getBodies() {
		return bodies;
	}	
	
	/*
	 * Checks if p is intersecting with any Body
	 *   returns first intersection
	 *   if no intersection, returns null
	 */
	public Body getBodyIntersection(Point2d p) {		
		for(Body b: bodies) {			
			if(CalcHelp.intersects(b.getCenter(), p, b.getRadius(), 0)) {			
				return b;
			}			
			for(Moon m: b.getMoons()) {
				if(CalcHelp.intersects(m.getCenter(), p, m.getRadius(), 0)) {
					return m;
				}				
			}			
		}		
		return null;		
	}	
		
	/*
	 * Checks if the ball is intersecting with any Body
	 *   returns first intersection
	 *   if no intersection, returns null
	 *   ignores bodies marked as reflectors
	 */
	public Body getBodyIntersection() {		
		for(Body b: bodies) {			
			if(!b.isReflector() && CalcHelp.intersects(b.getCenter(), ball.getCenter(), b.getRadius(), ball.getRadius())) {			
				return b;
			}			
			for(Moon m: b.getMoons()) {
				if(CalcHelp.intersects(m.getCenter(), ball.getCenter(), m.getRadius(), ball.getRadius())) {
					return m;
				}				
			}			
		}		
		return null;		
	}						

	public ArrayList<Blockage> getBlockages() {
		return blockages;
	}	
	
	/*
	 * Checks if the ball is intersecting with any Blockage
	 *   returns first intersection
	 *   if no intersection, returns null
	 */
	public Blockage getBlockageIntersection() {
		for(Blockage b: blockages) {
			if( b.intersects(ball.getCenter()) ) {
				return b;
			}
		}
		return null;		
	}	
	
	public double getFollowFactor() {
		return followFactor;
	}
	
	public ArrayList<GoalPost> getGoalPosts() {
		return goals;
	}			
	
	public double getGravityStrength() {
		return gravityStrength;
	}				

	public double getScreenXShift() {
		return screenXShift;
	}				
	
	public double getScreenYShift() {
		return screenYShift;
	}					
	
	public ArrayList<WarpPoint> getWarpPoints() {
		return warps;
	}		
		
	public boolean isOutOfBounds(Point2d p) {
		return p.x + screenXShift < 0 || p.x + screenXShift > GamePanel.Width 
		    || p.y + screenYShift < 0 || p.y + screenYShift > GamePanel.Height - 20;
	}
	public boolean isOutOfBounds() {
		return ball.getCenterX() + screenXShift < 0 || ball.getCenterX() + screenXShift > GamePanel.Width 
		    || ball.getCenterY() + screenYShift < 0 || ball.getCenterY() + screenYShift > GamePanel.Height - 20;
	}
	public boolean xOutOfBounds(double x) {
		return x + screenXShift < 0 || x + screenXShift > GamePanel.Width;		
	}
	public boolean yOutOfBounds(double y) {
		return y + screenYShift < 0 || y + screenYShift > GamePanel.Width;		
	}

	public boolean inGoalPost() {
		for(GoalPost g: goals) {
			if( CalcHelp.intersects(ball.getCenter(), g.getCenter(), ball.getRadius(), g.getRadius()) ) {
				return true;
			}
		}		
		return false;
	}
	
	public void reset()	{
		ball.resetLocation();
		ball.setLaunched(false);
		screenXShift = (followFactor == 0) ? 0 : ((500 - ball.getCenterX()) / followFactor);
		screenYShift = (followFactor == 0) ? 0 : ((350 - ball.getCenterY()) / followFactor);	
	}

	public boolean timeToReset() {
		return isOutOfBounds() || (getBodyIntersection() != null);
	}

	public void updateLevel() {	
		double sumXForce = 0.0;
		double sumYForce = 0.0;			
		// Gravity Vector components:
		double angle;              // direction
		double gravitationalForce; // magnitude	
							
		for(Body b: bodies) {	
			// Planetary effect on ball
			if(ball.isLaunched()) {				
				angle = CalcHelp.getAngle( ball.getCenter(), b.getCenter() );	
				gravitationalForce = CalcHelp.getAcceleration(ball.getCenter(), b, gravityStrength);				
				sumXForce += Math.cos(angle) * gravitationalForce;
				sumYForce -= Math.sin(angle) * gravitationalForce; 
			}						
			// Moon effect on ball
			// Moon movement
			for(Moon m: b.getMoons()) {					
				m.move(gravityStrength);									
				if(ball.isLaunched()) {			
					angle = CalcHelp.getAngle(ball.getCenter(), m.getCenter());	
					gravitationalForce = CalcHelp.getAcceleration(ball.getCenter(), m, gravityStrength);				
					sumXForce += Math.cos(angle) * gravitationalForce;
					sumYForce -= Math.sin(angle) * gravitationalForce;
				}									
			}
			// Reflector collision and resolution
			if(b.isReflector() && CalcHelp.intersects(ball.getCenter(), b.getCenter(), ball.getRadius(), b.getRadius())) {
				Vector2d v_i = ball.getVelocity();
				Vector2d v_p = v_i.projection(Math.atan((ball.getCenterY() - b.getCenterY() )/ (ball.getCenterX() - b.getCenterX())));
				Vector2d v_f = v_i.subtract(v_p.multiply(2));
				ball.setVelocity(v_f);
				while(CalcHelp.intersects(ball.getCenter(), b.getCenter(), ball.getRadius(), b.getRadius())) {
					ball.updateLocation();
				}
			}			
		}	
		
				
		boolean inAnyWarp = false;		
		for(int i = 0; i < warps.size(); i++) {			
			WarpPoint wp = warps.get(i);
			boolean intersecting = CalcHelp.intersects(ball.getCenter(), wp.getCenter(), WarpPoint.Radius, ball.getRadius());
			if(intersecting && !ballInWarp) {
				ballInWarp = true;				
				if(warps.size() != 1) {
					WarpPoint nextWarp = new WarpPoint();					
					if(i + 1 != warps.size()) {
						nextWarp = warps.get(i + 1);
					} else { 
						nextWarp = warps.get(0);				
					}	
					ball.setLocation( nextWarp.getCenterX(), nextWarp.getCenterY() );
					inAnyWarp = true;
					break;													
				}				
			} else if(intersecting) {
				inAnyWarp = true;
			}
		}
													
		if(!inAnyWarp) {
			ballInWarp = false;
		}
		
		boolean thisTime = false;
		for(Rectangle r: blockageRects ) {
			if(r.contains(ball.getCenter().getIntegerPoint())) {
				if(!hittingBlockage) {					
					double xSpeed = ball.getXSpeed();
					double ySpeed = ball.getYSpeed();								
					boolean sureUpDown = ball.getCenterX() > r.getX() + 3 && ball.getCenterX() < r.getX() + r.getWidth() - 3;				
					boolean sureLeftRight = ball.getCenterY() > r.getY() + 3 && ball.getCenterY() < r.getY() + r.getHeight() - 3;	
					if(sureLeftRight) {
						xSpeed *= -1;
					} else if(sureUpDown){
						ySpeed *= -1;
					} else {						
						// Else: pick the bigger side: if rect X > rect Y - vertical						
						if(r.getWidth() > r.getHeight()) {
							ySpeed *= -1;							
						} else {							
							xSpeed *= -1;
						}						
					}
					hittingBlockage = true;
					
					if(!timeToReset()) {		
						ball.setSpeed(xSpeed, ySpeed);								
						while(getBlockageIntersection() != null) {
							ball.updateLocation();
						}	
					}
				}				
				thisTime = true;					
				break;
			}
		}
		
		if(!thisTime && ball.isLaunched()) {
			hittingBlockage = false;		
			ball.updateSpeed(sumXForce, sumYForce);			
			ball.updateLocation();			
		}	
		
		screenXShift = (followFactor == 0) ? 0 : ((500 - ball.getCenterX()) / followFactor);
		screenYShift = (followFactor == 0) ? 0 : ((350 - ball.getCenterY()) / followFactor);	
	}	
         
	public void calculateSolutionSet(double max) {
		solutions = new ArrayList<java.awt.Point>();
		long t1 = System.currentTimeMillis();
		double sqr = max*max;
		for(int x = (int)(ball.getCenterX() - max); x <= (int)(ball.getCenterX()  + max); x++) {
			if(xOutOfBounds(x)) {
				continue;
			}
			for(int y = (int)(ball.getCenterY() - max); y <= (int)(ball.getCenterY()  +  max); y++) {
				if(yOutOfBounds(y)) {
					continue;
				}
				Point2d p = new Point2d(x, y);
				if( Math.pow(p.x - ball.getCenterX(), 2) + Math.pow(p.y - ball.getCenterY(), 2) <= sqr) {
					
					if(possibleWin(p, max)) {
						solutions.add(p.getIntegerPoint());
					}
				}
			}
		}	
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("levels/data/level" + (levelIndex + 1) + ".txt"));
		} catch (FileNotFoundException fnf) {
		}
		for(java.awt.Point p : solutions) {
			pw.println(p.x + " " + p.y);
		}
		pw.close();
		System.out.println( (System.currentTimeMillis() - t1)/1000.0 + " s");	
	}
	
	/*
	 * Runs with O(n^2) runtime
	 * Returns UNSHIFTED points
	 */
	public ArrayList<java.awt.Point> getSolutionSet() {
		if(solutions != null) {
			return solutions;
		}

		solutions = new ArrayList<java.awt.Point>();
		try {
			Scanner infile = new Scanner(new File("levels/data/level" + (levelIndex + 1) + ".txt"));
			while(infile.hasNext()) {
				solutions.add(new java.awt.Point(infile.nextInt(), infile.nextInt()));
			}	
			infile.close();
			return solutions;

		} catch(Exception e) {
			return solutions;
		}

	}	

	public boolean possibleWin(java.awt.Point p, double max) {
		// for a screen (game) coordinate
		return possibleWin(new Point2d(p).translate(-screenXShift, -screenYShift), max);
	}
	
	public boolean possibleWin(Point2d p, double max) {
		// for non-translated points		
		if(!onScreen(p)) return false;
				double mag = CalcHelp.getDistance(ball.getCenter(), p);
		if(mag > max) {
			mag = max;
		}
		double ang = CalcHelp.getAngle(ball.getCenter(), p);
		Level cloneLevel = new Level(new Ball((int)ball.getCenterX(), (int)ball.getCenterY(), ball.getRadius(), ball.getColor()), 
				bodies, warps, goals, blockages, followFactor, gravityStrength);
		double xLength =  Math.cos(ang) * mag;
		double yLength = -Math.sin(ang) * mag;
		cloneLevel.getBall().setSpeed(xLength / 200, yLength / 200); 
		cloneLevel.getBall().setLaunched(true);		
		long t = System.currentTimeMillis();
		while(true) {
			cloneLevel.updateLevel();	
			if(cloneLevel.inGoalPost()) {
				return true;
			}	
			if(cloneLevel.timeToReset()) { 
				return false;
			}
			if(System.currentTimeMillis() - t > 10) {
				return false;
			}
		}		
	}

	public boolean onScreen(Point2d p) {
		return p.x + screenXShift > 0 && p.x + screenXShift < 1000 && p.y + screenYShift > 0 && p.y + screenYShift < 700;
	}
	
	public String toString() {
		String str = ball.toString() + "\n";		
		for(Body b: bodies) {
			str += b.toString() + "\n";
		}		
		for(WarpPoint w: warps) {
			str += w.toString() + "\n";
		}		
		for(Blockage b: blockages) {
			str += b.toString() + "\n";
		}
		for(GoalPost g: goals) {
			str += g.toString() + "\n";
		}			
		str += "level(" + followFactor + ", " + gravityStrength + ")\n";		
		return str;		
	}	
	
}