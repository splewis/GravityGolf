package graphics

import java.awt.Color
import java.awt.Graphics
import java.util.List
import structures._

/**
 * Holder class for drawing the gravitational resultant effect.
 * @author Sean Lewis
 */
object ResultantDrawer {

  /**
   * Draws the gravitational resultant from the ball.
   * @param level the level in the game
   * @param g the Graphics component to draw with
   */
  def draw(level: Level, g: Graphics) = {
    val shift = level.getShift()
    val ball = level.getBall()
    val bodies = level.getBodies()
    val ballCent = new Point2d(ball.getCenter().x, ball.getCenter().y)

    var totalX = 0.0
    var totalY = 0.0

    // finds gravitational forces from the given body
    def addValues(b: Body) = {
      val bodyCent = new Point2d(b.getCenter.x, b.getCenter.y)
      val angle = CalcHelp.getAngle(ballCent, bodyCent)
      val length = level.getGravityStrength * GraphicEffect.ArrowLength *
        b.getRadius / ballCent.distance(bodyCent) + 5
      totalX += length * math.cos(angle)
      totalY -= length * math.sin(angle)
    }

    // sums up all forces
    for (i <- 0 until bodies.size()) {
      addValues(bodies.get(i))
      val moons = bodies.get(i).getMoons()
      for (j <- 0 until moons.size()) {
        addValues(moons.get(j))
      }
    }

    g.setColor(Color.blue);
    val tempPt1 = ball.getCenter().translate(shift)
    val tempPt2 = tempPt1.translate(totalX, totalY)
    GraphicEffect.drawArrow(tempPt1, tempPt2, g) 
  }

}