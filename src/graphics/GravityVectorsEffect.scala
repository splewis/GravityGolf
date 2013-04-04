package graphics

import java.awt.Color
import java.awt.Graphics
import java.util.List
import structures._

/**
 * Holder class for gravitational vectors effect.
 * @author Sean Lewis
 */
object GravityVectorsEffect {

  /**
   * Draws the vectors from the ball.
   * @param level the current Level in the game
   * @param g the Graphics component to draw with
   */
  def draw(level: Level, g: Graphics) = {
    g.setColor(Color.white);
    val ball = level.getBall()
    val ballCent = new Point2d(ball.getCenter.x, ball.getCenter.y)
    val bodies = level.getBodies()
    val shift = level.getShift()
    val ballPt = ballCent + shift

    def drawVector(b: Body) = {
      val angle = CalcHelp.getAngle(ballCent, b.getCenter());
      val length = 5 + level.getGravityStrength() * ArrowLength *
        b.getRadius() / ballCent.distance(b.getCenter)
      val dx = shift.x + length *  math.cos(angle)
      val dy = shift.y + length * -math.sin(angle)
      val p2 = ballCent.translate(dx, dy)
      drawArrow(ballPt, p2, 4, 12, g)
    }

    for (i <- 0 until bodies.size()) {
      val b = bodies.get(i)
      drawVector(b)
      for (i <- 0 until b.getMoons().size()) {
        val m = b.getMoons().get(i)
        drawVector(m)
      }
    }

  }

}