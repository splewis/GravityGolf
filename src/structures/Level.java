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
 */
public class Level {

	/**
	 * The total number of levels in the game.
	 */
	// TODO: this dependency should be removed
	public static int numLevels = 0;

	private int levelIndex = 0;

	/**
	 * A constant for how many stars should be put in the level. As the number
	 * increased, there are more stars.
	 */
	private static final int StarFactor = 7000;

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

	private int xMin;
	private int xMax;
	private int yMin;
	private int yMax;

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

	/**
	 * Creates a new level
	 */
	public Level(Ball ball, ArrayList<Body> bodies, ArrayList<WarpPoint> warps,
			ArrayList<GoalPost> goals, ArrayList<Blockage> blockages,
			double followfactor, double gravityStrength) {
		levelIndex = numLevels;
		numLevels++;
		this.ball = ball;
		this.bodies = bodies;
		this.goals = goals;
		this.warps = warps;
		this.blockages = blockages;
		this.blockageRects = new ArrayList<Rectangle>();
		for (Blockage bl : blockages) {
			this.blockageRects.add(new Rectangle(bl.getDrawX(), bl.getDrawY(),
					bl.getDrawXSize(), bl.getDrawYSize()));
		}
		this.followFactor = followfactor;
		this.gravityStrength = gravityStrength;
		this.screenXShift = (followFactor == 0) ? 0
				: ((500 - ball.getCenter().x) / followFactor);
		this.screenYShift = (followFactor == 0) ? 0
				: ((350 - ball.getCenter().y) / followFactor);
	}

	/**
	 * Performs all level computation so that it can be drawn.
	 */
	public void generateLevelData() {
		if (followFactor == 0) {
			// arbitrary values, see formula below - using 1 would lead to
			// division by 0, and using numbers <1.25 would make a huge area
			xMin = -extraX;
			xMax = GamePanel.Width + extraX;
			yMin = -extraY;
			yMax = GamePanel.Height + extraY;
		} else if (followFactor < 1.5) {
			// arbitrary values, see formula below - using 1 would lead to
			// division by 0, and using numbers <1.25 would make a huge area
			xMin = -1500;
			xMax = 1500;
			yMin = -1200;
			yMax = 1200;
		} else {
			double oneMinusInverse = 1.0 - 1.0 / followFactor;
			xMin = (int) Math.floor((-GamePanel.Width / 2 / followFactor)
					/ oneMinusInverse);
			xMax = (int) Math
					.ceil((-GamePanel.Width / 2 / followFactor + GamePanel.Width)
							/ oneMinusInverse);
			yMin = (int) Math.floor((-GamePanel.Height / 2 / followFactor)
					/ oneMinusInverse);
			yMax = (int) Math
					.ceil((-GamePanel.Height / 2 / followFactor + GamePanel.Height)
							/ oneMinusInverse);
		}

		// stars are never recalculated
		if (stars == null) {
			stars = new ArrayList<Star>();
			double area = (xMax - xMin) * (yMax - yMin) + 10000;
			int numStars = (int) (area / StarFactor);
			for (int i = 0; i < numStars; i++) {
				int x = CalcHelp.randomInteger(xMin - extraX, xMax + extraX);
				int y = CalcHelp.randomInteger(yMin - extraY, yMax + extraY);
				stars.add(new Star(x, y));
			}
		}

		image = new BufferedImage(xMax - xMin + extraX * 2, yMax - yMin
				+ extraY * 2, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		for (Star s : stars) {
			s.draw(-xMin + extraX, -yMin + extraY, g);
		}
		for (WarpPoint w : warps) {
			w.draw(-xMin + extraX, -yMin + extraY, g);
		}
		for (Body b : bodies) {
			b.draw(-xMin + extraX, -yMin + extraY, g);
		}
		for (Blockage b : blockages) {
			b.draw(-xMin + extraX, -yMin + extraY, g);
		}
		for (GoalPost gp : goals) {
			gp.draw(-xMin + extraX, -yMin + extraY, g);
		}
	}

	/**
	 * Returns the ArrayList of stars.
	 * @return the ArrayList of stars
	 */
	public ArrayList<Star> getStars() {
		return stars;
	}

	/**
	 * Draws the Level with no translation.
	 * @param g the Graphics component to draw with
	 */
	public void draw(Graphics2D g) {
		assert image != null;
		draw(0, 0, g);
	}

	/**
	 * Draws the Level.
	 * @param xShift the horizontal translation
	 * @param yShift the vertical translation
	 * @param g the Graphics component to draw with
	 */
	public void draw(int xShift, int yShift, Graphics2D g) {
		assert image != null;
		g.drawImage(image, xShift + xMin - extraX, yShift + yMin - extraY, null);
	}

	/**
	 * Returns the Ball for this level.
	 * @return the Ball
	 */
	public Ball getBall() {
		return ball;
	}

	/**
	 * Returns the ArrayList of Bodies.
	 * @return the ArrayList of Bodies.
	 */
	public ArrayList<Body> getBodies() {
		return bodies;
	}

	/**
	 * Returns a Body that intersects the parameter, or null if there are none.
	 * Ignores reflector bodies.
	 * @param body a Body
	 * @return a intersecting Body or null
	 */
	public Body getIntersectingBody() {
		for (Body b : bodies) {
			if (!b.isReflector() && ball.intersects(b)) {
				return b;
			}
			for (Moon m : b.getMoons()) {
				if (ball.intersects(m)) {
					return m;
				}
			}
		}
		return null;

	}

	/**
	 * Returns the ArrayList of Blockages.
	 * @return the ArrayList of Blockages.
	 */
	public ArrayList<Blockage> getBlockages() {
		return blockages;
	}

	/**
	 * Returns a blockage a shape is intersecting with, or null if there are
	 * none.
	 * @param shape a parameter
	 * @return a blockage a shape is intersecting with, or null if there are
	 *         none
	 */
	public Blockage getBlockageIntersection(CircularShape shape) {
		for (Blockage b : blockages) {
			if (b.intersects(shape.getCenter())) {
				return b;
			}
		}
		return null;
	}

	/**
	 * Returns the follow factor.
	 * @return the follow factor
	 */
	public double getFollowFactor() {
		return followFactor;
	}

	/**
	 * Returns the ArrayList of GoalPosts.
	 * @return the ArrayList of GoalPosts
	 */
	public ArrayList<GoalPost> getGoalPosts() {
		return goals;
	}

	/**
	 * Returns the gravitational constant for this level.
	 * @return the gravitational constant for this level
	 */
	public double getGravityStrength() {
		return gravityStrength;
	}

	/**
	 * Returns the current horizontal screen shift distance.
	 * @return the current horizontal screen shift distance
	 */
	public double getScreenXShift() {
		return screenXShift;
	}

	/**
	 * Returns the current vertical screen shift distance.
	 * @return the current vertical screen shift distance
	 */
	public double getScreenYShift() {
		return screenYShift;
	}

	/**
	 * Returns the ArrayList of WarpPoints.
	 * @return the ArrayList of WarpPoints
	 */
	public ArrayList<WarpPoint> getWarpPoints() {
		return warps;
	}

	private boolean isOutOfBounds(Point2d p) {
		return p.x + screenXShift < 0 || p.x + screenXShift > GamePanel.Width
				|| p.y + screenYShift < 0
				|| p.y + screenYShift > GamePanel.Height - 20;
	}

	private boolean isOutOfBounds() {
		return isOutOfBounds(ball.getCenter());
	}

	private boolean xOutOfBounds(double x) {
		return x + screenXShift < 0 || x + screenXShift > GamePanel.Width;
	}

	private boolean yOutOfBounds(double y) {
		return y + screenYShift < 0 || y + screenYShift > GamePanel.Height - 20;
	}

	/**
	 * Returns if the ball is in the goal post, i.e. has won.
	 * @return if the user was won this level
	 */
	public boolean inGoalPost() {
		for (GoalPost g : goals) {
			if (ball.intersects(g)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Resets the ball to its starting position.
	 */
	public void reset() {
		ball.resetLocation();
		ball.setLaunched(false);
		screenXShift = (followFactor == 0) ? 0
				: ((500 - ball.getCenter().x) / followFactor);
		screenYShift = (followFactor == 0) ? 0
				: ((350 - ball.getCenter().y) / followFactor);
	}

	/**
	 * Returns if the ball should be reset.
	 * @return if the ball should be reset
	 */
	public boolean timeToReset() {
		return isOutOfBounds() || (getIntersectingBody() != null);
	}

	/**
	 * Updates the level state a single step.
	 */
	public void updateLevel() {
		double sumXForce = 0.0;
		double sumYForce = 0.0;
		// Gravity Vector components:
		double angle; // direction
		double gravitationalForce; // magnitude

		for (Body b : bodies) {
			// Planetary effect on ball
			if (ball.isLaunched()) {
				angle = CalcHelp.getAngle(ball.getCenter(), b.getCenter());
				gravitationalForce = CalcHelp.getAcceleration(ball.getCenter(),
						b.getCenter(), b.getMass(), gravityStrength);
				sumXForce += Math.cos(angle) * gravitationalForce;
				sumYForce -= Math.sin(angle) * gravitationalForce;
			}
			// Moon effect on ball
			// Moon movement
			for (Moon m : b.getMoons()) {
				m.move(gravityStrength);
				if (ball.isLaunched()) {
					angle = CalcHelp.getAngle(ball.getCenter(), m.getCenter());

					gravitationalForce = CalcHelp.getAcceleration(
							ball.getCenter(), m.getCenter(), m.getMass(),
							gravityStrength);
					sumXForce += Math.cos(angle) * gravitationalForce;
					sumYForce -= Math.sin(angle) * gravitationalForce;
				}
			}
			// Reflector collision and resolution
			if (b.isReflector() && b.intersects(ball)) {
				Vector2d v_i = ball.getVelocity();
				Vector2d v_p = v_i.projection(Math.atan((ball.getCenter().y - b
						.getCenter().y)
						/ (ball.getCenter().x - b.getCenter().x)));

				// TODO: projection clean up - what is going on here?

				Vector2d v_f = v_i.subtract(v_p.multiply(2));
				ball.setVelocity(v_f);
				while (ball.intersects(b)) {
					ball.move();
				}
			}
		}

		boolean inAnyWarp = false;
		for (int i = 0; i < warps.size(); i++) {
			WarpPoint wp = warps.get(i);
			boolean intersecting = ball.intersects(wp);
			if (intersecting && !ballInWarp) {
				ballInWarp = true;
				if (warps.size() != 1) {
					WarpPoint nextWarp = new WarpPoint();
					if (i + 1 != warps.size()) {
						nextWarp = warps.get(i + 1);
					} else {
						nextWarp = warps.get(0);
					}
					ball.setCenter(new Point2d(nextWarp.getCenter().x, nextWarp
							.getCenter().y));
					inAnyWarp = true;
					break;
				}
			} else if (intersecting) {
				inAnyWarp = true;
			}
		}

		if (!inAnyWarp) {
			ballInWarp = false;
		}

		boolean thisTime = false;
		for (Rectangle r : blockageRects) {
			if (r.contains(ball.getCenter().getIntegerPoint())) {
				if (!hittingBlockage) {
					double xSpeed = ball.getVelocity().getXComponent();
					double ySpeed = ball.getVelocity().getYComponent();
					boolean sureUpDown = ball.getCenter().x > r.getX() + 3
							&& ball.getCenter().x < r.getX() + r.getWidth() - 3;
					boolean sureLeftRight = ball.getCenter().y > r.getY() + 3
							&& ball.getCenter().y < r.getY() + r.getHeight()
									- 3;
					if (sureLeftRight) {
						xSpeed *= -1;
					} else if (sureUpDown) {
						ySpeed *= -1;
					} else {
						// Else: pick the bigger side: if rect X > rect Y -
						// vertical
						if (r.getWidth() > r.getHeight()) {
							ySpeed *= -1;
						} else {
							xSpeed *= -1;
						}
					}
					hittingBlockage = true;

					if (!timeToReset()) {
						ball.setVelocity(new Vector2d(xSpeed, ySpeed));
						while (getBlockageIntersection(ball) != null) {
							ball.move();
						}
					}
				}
				thisTime = true;
				break;
			}
		}

		if (!thisTime && ball.isLaunched()) {
			hittingBlockage = false;
			ball.accelerate(new Vector2d(sumXForce, sumYForce));
			ball.move();
		}

		screenXShift = (followFactor == 0) ? 0
				: ((500 - ball.getCenter().x) / followFactor);
		screenYShift = (followFactor == 0) ? 0
				: ((350 - ball.getCenter().y) / followFactor);
	}

	/**
	 * Performs the computation for possible input points to win the level.
	 * @param max the maximum length for the inital vector
	 */
	public void calculateSolutionSet(double max) {
		solutions = new ArrayList<java.awt.Point>();
		long t1 = System.currentTimeMillis();
		double sqr = max * max;
		for (int x = (int) (ball.getCenter().x - max); x <= (int) (ball
				.getCenter().x + max); x++) {
			if (xOutOfBounds(x)) {
				continue;
			}
			for (int y = (int) (ball.getCenter().y - max); y <= (int) (ball
					.getCenter().y + max); y++) {
				if (yOutOfBounds(y)) {
					continue;
				}
				Point2d p = new Point2d(x, y);
				if (Math.pow(p.x - ball.getCenter().x, 2)
						+ Math.pow(p.y - ball.getCenter().y, 2) <= sqr) {

					if (possibleWin(p, max)) {
						solutions.add(p.getIntegerPoint());
					}
				}
			}
		}
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("levels/data/level"
					+ (levelIndex + 1) + ".txt"));
		} catch (FileNotFoundException fnf) {
		}
		for (java.awt.Point p : solutions) {
			pw.println(p.x + " " + p.y);
		}
		pw.close();
		System.out.println((System.currentTimeMillis() - t1) / 1000.0 + " s");
	}

	/**
	 * Returns the solution set for this Level, as already defined in
	 * levels/data/levelX.txt
	 * @return the solution set
	 */
	public ArrayList<java.awt.Point> getSolutionSet() {
		if (solutions != null) {
			return solutions;
		}

		solutions = new ArrayList<java.awt.Point>();
		try {
			// TODO: bad dependency on levelIndex here
			Scanner infile = new Scanner(new File("levels/data/level"
					+ (levelIndex + 1) + ".txt"));
			while (infile.hasNext()) {
				solutions.add(new java.awt.Point(infile.nextInt(), infile
						.nextInt()));
			}
			infile.close();
			return solutions;

		} catch (Exception e) {
			return solutions;
		}

	}

	/**
	 * Returns if the user clicking this point would result in a win.
	 * @param clickedPoint the screen point the user clicked
	 * @param max the maximum initial vector length
	 * @return if this point results in a win
	 */
	public boolean possibleWin(java.awt.Point clickedPoint, double max) {
		// for a screen (game) coordinate
		return possibleWin(new Point2d(clickedPoint).translate(-screenXShift,
				-screenYShift), max);
	}

	/**
	 * Returns if the user inputting this point (raw level data - all
	 * translation data stripped) woudl result in a win.
	 * @param translatedPoint the input point
	 * @param max the maximum initial vector length
	 * @return if this point results in a win
	 */
	public boolean possibleWin(Point2d translatedPoint, double max) {
		// for non-translated points
		if (!onScreen(translatedPoint))
			return false;
		double mag = translatedPoint.getDistance(ball.getCenter());
		if (mag > max) {
			mag = max;
		}
		double ang = CalcHelp.getAngle(ball.getCenter(), translatedPoint);
		Level cloneLevel = new Level(new Ball((int) ball.getCenter().x,
				(int) ball.getCenter().y, ball.getRadius(), ball.getColor()),
				bodies, warps, goals, blockages, followFactor, gravityStrength);
		double xLength = Math.cos(ang) * mag;
		double yLength = -Math.sin(ang) * mag;
		cloneLevel.getBall().setVelocity(
				new Vector2d(xLength / 200, yLength / 200));
		cloneLevel.getBall().setLaunched(true);
		long t = System.currentTimeMillis();
		while (true) {
			cloneLevel.updateLevel();
			if (cloneLevel.inGoalPost()) {
				return true;
			}
			if (cloneLevel.timeToReset()) {
				return false;
			}
			if (System.currentTimeMillis() - t > 10) {
				return false;
			}
		}
	}

	private boolean onScreen(Point2d p) {
		return p.x + screenXShift > 0 && p.x + screenXShift < 1000
				&& p.y + screenYShift > 0 && p.y + screenYShift < 700;
	}

	/**
	 * Returns the String representation of this Level.
	 */
	public String toString() {
		String str = ball.toString() + "\n";
		for (Body b : bodies) {
			str += b.toString() + "\n";
		}
		for (WarpPoint w : warps) {
			str += w.toString() + "\n";
		}
		for (Blockage b : blockages) {
			str += b.toString() + "\n";
		}
		for (GoalPost g : goals) {
			str += g.toString() + "\n";
		}
		str += "level(" + followFactor + ", " + gravityStrength + ")\n";
		return str;
	}

	/**
	 * Returns the hash value for the level.
	 */
	public int hashCode() {
		long sum = 0L;
		int count = 0;		
		
		sum += ball.hashCode();
		count++;

		for (Body b : bodies) {
			sum += b.hashCode();
			count++;
		}
		for (WarpPoint w : warps) {
			sum += w.hashCode();
			count++;
		}
		for (Blockage b : blockages) {
			sum += b.hashCode();
			count++;
		}
		for (GoalPost g : goals) {
			sum += g.hashCode();
			count++;
		}
		sum += followFactor * gravityStrength;

		return (int) (sum / count);
	}
}