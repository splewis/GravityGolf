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
    var followFactor = CalcHelp.gaussianDoubleInRange(5.0, 1.0, 4.0, 10.0)
    var gravityStrength = CalcHelp.gaussianDoubleInRange(1.5, 0.5, 0.75, 2.0)

    var goals = new ArrayList[GoalPost]()
    var goal = placeGoal(ball, bodies, followFactor, gravityStrength)
    if(goal == null) // no good spot for goal, try again
      return randomLevel()      
      
    goals.add(goal)
    return new Level(ball, bodies, warps, goals, blockages, followFactor,
      gravityStrength)
  }

  /** Returns an intelligent place for the goal to go */
  private def placeGoal(ball: Ball, bodies: ArrayList[Body],
    ff: Double, g: Double): GoalPost = {
    
    val Radius = 15
    
    /*
     * Algorithm:
     *  - Pick 1000 random points in the ball's launch circle
     *    - Simulate the launching and track the ball
     *    - If a point the ball is at is far enough, add it to a master list
     *    - Once enough level ticks have been done or the ball crashes, stop
     *  - After all points tested, pick a random point from the list
     */
    
    var possibleGoalPoints = List[Point2d]()
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
      val launchPoint = new Point2d(randX, randY)
      var tempLevel = new Level(ball, bodies, null, null, null, ff, g)

      // compute launch parameters
      val ballStart = new Point2d(ball.getCenter().x, ball.getCenter().y)
      var launchMagnitude = ball.getCenter().getDistance(launchPoint)
      var launchAngle = CalcHelp.getAngle(ball.getCenter(), launchPoint)
      if (launchMagnitude > GamePanel.MaxInitialMagnitude)
        launchMagnitude = GamePanel.MaxInitialMagnitude

      var xLength =  math.cos(launchAngle) * launchMagnitude
      var yLength = -math.sin(launchAngle) * launchMagnitude
      ball.setVelocity(new Vector2d(xLength / 200, yLength / 200))
      ball.setLaunched(true)

      // simulate game-play
      var ticks = 0
      while (ticks < 10000 && !tempLevel.timeToReset()) {
        
        tempLevel.updateLevel()
        
        // check distance from start:
        val minDist = math.hypot(0.5*GamePanel.Width, 0.5*GamePanel.Height)
        val tooClose = ball.getCenter().withinDistance(ballStart, minDist)
        if (!tooClose) {
          
          // check if on screen:
          val curPoint = new Point2d(ball.getCenter().x, ball.getCenter().y)
          val xInBounds = curPoint.x >= 10 && curPoint.x <= GamePanel.Width
          val yInBounds = curPoint.y >= 10 && curPoint.y <= GamePanel.Height
          if (xInBounds && yInBounds) {
            
            // check if overlapping a planet:
            var planetOverlap = false
            for (i <- 0 to (bodies.size() - 1)) {
              if (bodies.get(i).getCenter().withinDistance(curPoint, Radius)) {
                planetOverlap = true
              }
            }            
            // If all good- add the point to the master list
            if (!planetOverlap)
              possibleGoalPoints ::= curPoint     
          }
        } 
        
        ticks += 1
      } // end simulation loop
      
      tempLevel.reset()     
    } // end potential point loop
    
    
    val length:Int = possibleGoalPoints.length  
    if(length == 0) // i.e. no good points found, bad level design!
      return null
      
  	val randIndex:Int = (new scala.util.Random).nextInt(length)    
    val pick:Point2d = possibleGoalPoints(randIndex)    
    return new GoalPost(pick.x.toInt, pick.y.toInt, Radius)
  }

  private def validGoalLocation(p: Point2d): Boolean = {
    val edgeDist = 100
    val xInBounds = p.x >= edgeDist && p.x <= GamePanel.Width - edgeDist
    val yInBounds = p.y >= edgeDist && p.y <= GamePanel.Height- edgeDist
    return xInBounds && yInBounds    
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
    var x: Int = CalcHelp.gaussianIntegerInRange(xMean, 15, 10, 990)
    var y: Int = CalcHelp.gaussianIntegerInRange(yMean, 15, 10, 580)
    return new Ball(x, y, 3)
  }

  /** Generates the random list of Bodies for the Level. */
  private def randBodies(ball: Ball): ArrayList[Body] = {
    var bodies = new ArrayList[Body]()

    // we set a fixed amount of the screen area that must be filled by planets:
    // randomly set on normal distribution with mean 0.25 and std dev 0.15
    var fillPercentage = CalcHelp.gaussianDoubleInRange(.2, .1, .1, .35)

    val mustBeFilled = fillPercentage * 1000 * 700
    var isFilled = 0.0
    while (isFilled < mustBeFilled && bodies.size() < 10) {
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
    var r = CalcHelp.gaussianInteger(70, 70)
    if (r < 20)
      r = 20
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