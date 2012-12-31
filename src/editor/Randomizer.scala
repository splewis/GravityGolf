package editor

import structures._;
import game.GamePanel;
import scala.util.Random;
import java.util.ArrayList;
import java.awt.Color;

/**
 * A tool for calling for the generation of a random level.
 */
object Randomizer {

  /**
   * Returns a random Level. It is guaranteed that this level will be possible 
   * to solve.
   */
  def randomLevel(): Level = {
    var ball = randBall()
    var bodies = randBodies(ball)
    var warps = new ArrayList[WarpPoint]()
    var blockages = new ArrayList[Blockage]()
    var followFactor = CalcHelp.gaussianDoubleInRange(4.0, 2.0, 0.0, 10.0)
 
    var gravityStrength = CalcHelp.gaussianDoubleInRange(1.5, 0.5, 0.75, 2.0)

    var goals = new ArrayList[GoalPost]()
    goals.add(placeGoal(ball, bodies, followFactor, gravityStrength))
    
    return new Level(ball, bodies, warps, goals, blockages, followFactor,
      gravityStrength)
  }

  private def placeGoal(ball: Ball, bodies: ArrayList[Body],
    ff: Double, g: Double): GoalPost = {
    // we pick 1000 random points (possibly with duplicates)
    val toCheck = 1000
    for (i <- 1 to toCheck) {
      val centerX = ball.getCenter().x
      val centerY = ball.getCenter().y
      val r = GamePanel.MaxInitialMagnitude

      // generate random x, random y in launch circle
      val randX = CalcHelp.randomDouble(centerX - r, centerX + r)
      //    (x-h)^2 + (y-k)^2 = r^2
      // => y = k +/- sqrt ( r^2 - (x-h)^2 )
      val xMinusH = randX - centerX
      val yRange = scala.math.sqrt(r * r - xMinusH * xMinusH) + centerY
      val randY = CalcHelp.randomDouble(centerY - yRange, centerY + yRange)

      var tempLevel = new Level(ball, bodies, null, null, null, ff, g)
      
      // TODO get data for launch point, launch!
      
      ball.setLaunched(true)
      
//      launchMagnitude = initialPoint.getDistance(terminalPoint);
//			launchAngle = CalcHelp.getAngle(initialPoint, terminalPoint);
//			if (launchAngle < 0)
//				launchAngle += (2 * Math.PI); // so the display only shows the
//												// positive coterminal angle
//												// (300 instead of -60)
//			if (launchMagnitude > MaxInitialMagnitude)
//				launchMagnitude = MaxInitialMagnitude;
//
//			double xLength = Math.cos(launchAngle) * launchMagnitude;
//			double yLength = -Math.sin(launchAngle) * launchMagnitude;
//			ball.setVelocity(new Vector2d(xLength / 200, yLength / 200));
//			ball.setLaunched(true);
      
      
    }
    
    
    return new GoalPost(1, 2, 3)
  }

  /** Returns a random Ball. */
  private def randBall(): Ball = {
    var xMean = 0.0
    var yMean = 0.0
    var rand = 0.0
    // set ball's coordinates
    // 60% of time near the left
    // 20% near the middle
    // 20% near the right

    rand = Random.nextDouble()
    if (rand < .6)
      xMean = 75
    else if (rand < .8)
      xMean = 500
    else
      xMean = 800

    // 60% of time near the top
    // 20% near the middle
    // 20% near the bottom
    rand = Random.nextDouble()
    if (rand < .6)
      yMean = 50
    else if (rand < .8)
      yMean = 300
    else
      yMean = 500
    
    // assign actual coordinates based on a normal distribution
    var x:Int = CalcHelp.gaussianIntegerInRange(xMean, 15, 10, 990)
    var y:Int = CalcHelp.gaussianIntegerInRange(yMean, 15, 10, 580) 
    return new Ball(x, y, 3)
  }

  /** Generates the random list of Bodies for the Level. */
  private def randBodies(ball: Ball): ArrayList[Body] = {
    var bodies = new ArrayList[Body]()

    // we set a fixed amount of the screen area that must be filled by planets:
    // randomly set on normal distribution with mean 0.3 and std dev 0.2
    var fillPercentage = CalcHelp.gaussianDoubleInRange(.3, .2, .1, .5)

    val mustBeFilled = fillPercentage * 1000 * 700
    var isFilled = 0.0
    while (isFilled < mustBeFilled) {
      var currentBody = randBody()
      // recompute until no overlap
      while (bodyBallOverlap(ball, bodies, currentBody)) {
        currentBody = randBody()
      }
      bodies.add(currentBody);
      isFilled += scala.math.Pi * currentBody.getRadiusSquared()
    }

    return bodies
  }

  /** Returns a random Body. */
  private def randBody(): Body = {
    var x = CalcHelp.randomInteger(-100, 1100)
    var y = CalcHelp.randomInteger(-100, 800)
    var r = CalcHelp.gaussianInteger(120, 90)
    if (r < 10)
      r = 10
    return new Body(x, y, r, randColor())
  }

  /** Returns if any overlap occurs. */
  private def bodyBallOverlap(ball: Ball, bodies: ArrayList[Body],
    currentBody: Body): Boolean = {
    if (ball.intersects(currentBody))
      return true
    for (i <- 0 to (bodies.size() - 1))
      if (bodies.get(i).intersects(currentBody))
        return true
    return false
  }

  /** Returns a randomly generated color. */
  private def randColor(): Color = {
    val r = CalcHelp.randomInteger(0, 255)
    val g = CalcHelp.randomInteger(0, 255)
    val b = CalcHelp.randomInteger(0, 255)
    return new Color(r, g, b)
  }

}