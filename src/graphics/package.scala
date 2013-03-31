import java.awt.Graphics
import structures._

package object graphics {

  /**
   * Constant value that multiplies the length of drawn arrows so they are
   * longer.
   */
  val ArrowLength: Int = 200;

  /** Rounding method for Double -> Int */
  def round(x: Double): Int 
    = math.round(x.asInstanceOf[Float])

  /**
   * Draws an arrow between two points.
   * @param p1 the initial point
   * @param p2 the terminal point
   * @param drawingOffset the offset distance from the initial to start
   *        drawing the point
   * @param arrowSize the length of each tail at the terminal point
   * @param g the Graphics component to draw with
   */
  def drawArrow(p1: Point2d, p2: Point2d, drawingOffset: Int,
    arrowSize: Int, g: Graphics) {
    val ang = CalcHelp.getAngle(p1, p2)

    // Shifts away from the center of the ball, so the line starts a little
    // past the edge of the ball
    val xShift =  math.cos(ang) * drawingOffset
    val yShift = -math.sin(ang) * drawingOffset

    g.drawLine(round(p1.x + xShift), round(p1.y + yShift), 
      round(p2.x), round(p2.y));

    val angle  = ang - math.Pi / 4
    val cosine = math.cos(angle)
    val sine   = math.sin(angle)

    g.drawLine(round(p2.x), round(p2.y),
      round(p2.x - arrowSize * cosine),
      round(p2.y + arrowSize * sine));

    g.drawLine(round(p2.x), round(p2.y),
      round(p2.x + arrowSize * sine),
      round(p2.y + arrowSize * cosine));
  }

}