package editor;

import structures.*;
import game.GamePanel;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

/**
 * A tool for calling for the generation of a random level.
 */
public final class Randomizer {

	/**
	 * Returns a random Level. It is guaranteed that this level will be possible
	 * to solve.
	 */
	public static Level randomLevel() {
		Ball ball = randBall();
		ArrayList<Body> bodies = randBodies(ball);
		ArrayList<WarpPoint> warps = new ArrayList<WarpPoint>();
		ArrayList<Blockage> blockages = new ArrayList<Blockage>();
		double followFactor = CalcHelp.gaussianDoubleInRange(5.0, 1.0, 4.0,
				10.0);
		double gravityStrength = CalcHelp.gaussianDoubleInRange(1.5, 0.5, 0.75,
				2.0);

		ArrayList<GoalPost> goals = new ArrayList<GoalPost>();
		GoalPost goal = placeGoal(ball, bodies, followFactor, gravityStrength);
		if (goal == null) // no good spot for goal, try again
			return randomLevel();

		goals.add(goal);
		return new Level(ball, bodies, warps, goals, blockages, followFactor,
				gravityStrength);
	}

	/** Returns an intelligent place for the goal to go */
	private static GoalPost placeGoal(Ball ball, ArrayList<Body> bodies, double ff,
			double g) {

		final int Radius = 15;
		/*
		 * Algorithm - Pick 1000 random points in the ball's launch circle -
		 * Simulate the launching and track the ball - If a point the ball is at
		 * is far enough, add it to a master list - Once enough level ticks have
		 * been done or the ball crashes, stop - After all points tested, pick a
		 * random point from the list
		 */

		List<Point2d> possibleGoalPoints = new ArrayList<Point2d>();

		int toCheck = 1000;
		for (int i = 0; i < toCheck; i++) {
			int r = GamePanel.MaxInitialMagnitude;
			Level tempLevel = new Level(ball, bodies, null, null, null, ff, g);

			Point2d launchPoint = null;
			do {
				launchPoint = randPointInCircle(ball.getCenter(), r);
			} while (!tempLevel.onScreen(launchPoint));

			// compute launch parameters
			Point2d ballStart = new Point2d(ball.getCenter().x,
					ball.getCenter().y);
			double launchMagnitude = ball.getCenter().getDistance(launchPoint);
			double launchAngle = CalcHelp.getAngle(ball.getCenter(),
					launchPoint);
			if (launchMagnitude > GamePanel.MaxInitialMagnitude)
				launchMagnitude = GamePanel.MaxInitialMagnitude;

			double xLength = Math.cos(launchAngle) * launchMagnitude;
			double yLength = -Math.sin(launchAngle) * launchMagnitude;
			ball.setVelocity(new Vector2d(xLength / 200, yLength / 200));
			ball.setLaunched(true);

			// simulate game-play
			int ticks = 0;
			while (ticks < 10000 && !tempLevel.timeToReset()) {

				tempLevel.updateLevel();

				// check distance from start:
				double minDist = Math.hypot(0.5 * GamePanel.Width,
						0.5 * GamePanel.Height);
				boolean tooClose = ball.getCenter().withinDistance(ballStart,
						minDist);
				if (!tooClose) {

					// check if on screen:
					Point2d curPoint = new Point2d(ball.getCenter().x,
							ball.getCenter().y);
					if (validGoalLocation(curPoint, tempLevel)) {

						// check if overlapping a planet:
						boolean planetOverlap = false;
						for (Body b : bodies) {
							if (b.getCenter().withinDistance(curPoint,
									Radius + 10)) {
								planetOverlap = true;
							}
						}
						// If all good- add the point to the master list
						if (!planetOverlap)
							possibleGoalPoints.add(curPoint);
					}
				}

				ticks += 1;
			} // end simulation loop

			tempLevel.reset();
		} // end potential point loop

		int length = possibleGoalPoints.size();
		if (length == 0) // i.e. no good points found, bad level design!
			return null;

		int randIndex = CalcHelp.randomInteger(0, possibleGoalPoints.size());
		Point2d pick = possibleGoalPoints.get(randIndex);
		return new GoalPost((int) pick.x, (int) pick.y, Radius);
	}

	private static Point2d randPointInCircle(Point2d center, double radius) {
		double randX = CalcHelp.randomDouble(center.x - radius, center.x
				+ radius);
		// (x-h)^2 + (y-k)^2 = r^2
		// => y = k +/- sqrt ( r^2 - (x-h)^2 )
		double xMinusH = randX - center.x;
		double yRange = Math.sqrt(radius * radius - xMinusH * xMinusH)
				+ center.y;
		double randY = CalcHelp.randomDouble(center.y - yRange, center.x
				+ yRange);
		return new Point2d(randX, randY);
	}

	private static boolean validGoalLocation(Point2d p, Level level) {
		int edgeDist = 100;
		boolean xInBounds = p.x >= edgeDist
				&& p.x <= GamePanel.Width - edgeDist;
		boolean yInBounds = p.y >= edgeDist
				&& p.y <= GamePanel.Height - edgeDist;
		return xInBounds && yInBounds && level.onScreen(p);
	}

	/** Returns a random Ball. */
	private static Ball randBall() {
		double xMean = 0.0;
		double yMean = 0.0;
		double rand = 0.0;
		// set ball's coordinates
		// 60% of time near the left
		// 20% near the middle
		// 20% near the right

		rand = Math.random();
		if (rand < .6)
			xMean = 75;
		else if (rand < .8)
			xMean = 500;
		else
			xMean = 800;

		// 60% of time near the top
		// 20% near the middle
		// 20% near the bottom
		rand = Math.random();
		if (rand < .6)
			yMean = 50;
		else if (rand < .8)
			yMean = 300;
		else
			yMean = 500;

		// assign actual coordinates based on a normal distribution
		int x = CalcHelp.gaussianIntegerInRange(xMean, 15, 10, 990);
		int y = CalcHelp.gaussianIntegerInRange(yMean, 15, 10, 580);
		return new Ball(x, y, 3);
	}

	/** Generates the random list of Bodies for the Level. */
	private static ArrayList<Body> randBodies(Ball ball) {
		ArrayList<Body> bodies = new ArrayList<Body>();

		// we set a fixed amount of the screen area that must be filled by
		// planets:
		// randomly set on normal distribution with mean 0.25 and std dev 0.15
		double fillPercentage = CalcHelp.gaussianDoubleInRange(.2, .1, .1, .35);

		double mustBeFilled = fillPercentage * 1000 * 700;
		double isFilled = 0.0;
		while (isFilled < mustBeFilled && bodies.size() < 10) {
			Body currentBody = randBody();
			// recompute until no overlap
			while (bodyBallOverlap(ball, bodies, currentBody)) {
				currentBody = randBody();
			}
			bodies.add(currentBody);
			isFilled += Math.PI * currentBody.getRadiusSquared();
		}

		return bodies;
	}

	/** Returns a random Body. */
	private static Body randBody() {
		int x = CalcHelp.randomInteger(-100, 1100);
		int y = CalcHelp.randomInteger(-100, 800);
		int r = CalcHelp.gaussianInteger(70, 70);
		if (r < 20)
			r = 20;
		return new Body(x, y, r, randColor());
	}

	/** Returns if any overlap occurs. */
	private static boolean bodyBallOverlap(Ball ball, List<Body> bodies,
			Body currentBody) {
		if (ball.intersects(currentBody))
			return true;
		for (Body b : bodies)
			if (b.intersects(currentBody))
				return true;
		return false;
	}

	/** Returns a randomly generated color. */
	private static Color randColor() {
		int r = CalcHelp.randomInteger(0, 255);
		int g = CalcHelp.randomInteger(0, 255);
		int b = CalcHelp.randomInteger(0, 255);
		return new Color(r, g, b);
	}

}