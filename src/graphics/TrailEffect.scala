package graphics

import java.awt.Color
import java.awt.Graphics
import java.util.ArrayList
import structures._

/**
 * Holder object for the trail drawing effect
 * @author Sean Lewis
 */
object TrailEffect {
  
  /** Internal point storage */
  private var trailPoints: ArrayList[Point2d] 
    = new ArrayList[Point2d](100);

  /** Adds a point the to current trail. */
  def addTrailPoint(point: Point2d): Unit 
    = trailPoints.add(point)

  /** Clears all points from the saved trail. */
  def resetPoints(): Unit 
    = trailPoints.clear()

  /** Draws the trail effect.
   * @param trailPoints the list of points in the trail
   * @param level the current Level
   * @param g the Graphics component to draw with
   */
  def draw(level: Level, g: Graphics) = {
    val shift = level.getShift
    // n = number of points to draw
    val n = trailPoints.size() - 2
    g.setColor(Color.green)
    for (i <- 0 until n) {
      val from = (trailPoints.get(i)     + shift).getIntegerPoint
      val to   = (trailPoints.get(i + 1) + shift).getIntegerPoint
      g.drawLine(from.x, from.y, to.x, to.y)
    }
  }	
	

}