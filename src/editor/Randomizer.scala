package editor

import structures._;
import scala.util.Random;
import java.util.ArrayList;
import java.awt.Color;

object Randomizer {

  def randomLevel(): Level = {
    var ball = randBall();
    var bodies = randBodies(ball);
    var goals = new ArrayList[GoalPost]();
    var warps = new ArrayList[WarpPoint]();
    var blockages = new ArrayList[Blockage]();
    var followFactor = 5.0;
    var gravityStrength = 1.0;
    return new Level(ball, bodies, warps, goals, blockages, followFactor,
      gravityStrength);

  }
  private def randBall(): Ball = {
    var xMean = 0.0
    var yMean = 0.0
    var rand = 0.0

    rand = Random.nextDouble()
    if (rand < .6)
      xMean = 75
    else if (rand < .8)
      xMean = 500;
    else
      xMean = 800;

    rand = Random.nextDouble()
    if (rand < .6)
      yMean = 50
    else if (rand < .8)
      yMean = 300;
    else
      yMean = 500;

    var x = CalcHelp.gaussianInteger(xMean, 15)
    var y = CalcHelp.gaussianInteger(yMean, 15)
    if (x < 10)
      x = 10
    if (y < 10)
      y = 10;
    if (x > 990)
      x = 990
    if (y > 580)
      y = 580

    return new Ball(x, y, 3)
  }

  private def randBodies(ball: Ball): ArrayList[Body] = {
    var bodies = new ArrayList[Body]()

    var fillPercentage = CalcHelp.gaussianDouble(.3, .2)
    if (fillPercentage < .1)
      fillPercentage = .1
    if (fillPercentage > .5)
      fillPercentage = .5

    val mustBeFilled = fillPercentage * 1000 * 700;
    var isFilled = 0.0;
    while (isFilled < mustBeFilled) {
      var x = CalcHelp.randomInteger(-100, 1100);
      var y = CalcHelp.randomInteger(-100, 800);
      var r = CalcHelp.gaussianInteger(120, 90);
      var currentBody = new Body(x, y, r, randColor());

      while (bodyBallOverlap(ball, bodies, currentBody)) {
        x = CalcHelp.randomInteger(-100, 1100);
        y = CalcHelp.randomInteger(-100, 800);
        r = CalcHelp.gaussianInteger(120, 90);
        currentBody = new Body(x, y, r, randColor());
      }

      bodies.add(currentBody);
      isFilled += scala.math.Pi * r * r;
    }

    return bodies;
  }

  private def bodyBallOverlap(ball: Ball, bodies: ArrayList[Body],
    currentBody: Body): Boolean = {
    if (ball.intersects(currentBody))
      return true;
    for (i <- 0 to (bodies.size() - 1))
      if (bodies.get(i).intersects(currentBody))
        return true;
    return false;
  }

  private def randColor(): Color = {
    val r = CalcHelp.randomInteger(0, 255);
    val g = CalcHelp.randomInteger(0, 255);
    val b = CalcHelp.randomInteger(0, 255);
    return new Color(r, g, b);
  }

}