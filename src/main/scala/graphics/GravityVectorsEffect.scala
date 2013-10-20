package graphics

import java.awt.Color
import java.awt.Graphics
import java.util.List
import structures._

/** Holder class for gravitational vectors effect.
 *  @author Sean Lewis
 */
object GravityVectorsEffect {

  /** Draws the vectors from the ball.
   *  @param level the current Level in the game
   *  @param g the Graphics component to draw with
   */
  def draw(level: Level, g: Graphics) = {
    g.setColor(Color.white);
    val ball = level.getBall()
    val ballCent = new Point2d(ball.getCenter.x, ball.getCenter.y)
    val bodies = level.getBodies()
    val shift = level.getShift()
    val ballPt = ballCent + shift

    def drawVector(b: Body): Unit = {
      val angle = CalcHelp.getAngle(ballCent, b.getCenter());
      val length = 5 + level.getGravityStrength() * GraphicEffect.ArrowLength *
        b.getRadius() / ballCent.distance(b.getCenter)
      val dx = shift.x + length * math.cos(angle)
      val dy = shift.y + length * -math.sin(angle)
      val p2 = ballCent.translate(dx, dy)
      GraphicEffect.drawArrow(ballPt, p2, g)
      CalcHelp.map(drawVector, b.getMoons)
    }

    CalcHelp.map(drawVector, bodies)
  }

}