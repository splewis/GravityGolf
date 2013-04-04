package structures

import java.awt.Color;
import java.awt.Graphics;

/**
 * A Star is a visual component that is simply a colored circle that is drawn in
 * the background.
 * @author Sean Lewis
 */
class Star(val x: Int, val y: Int) {

  def this(p: java.awt.Point) = this(p.x, p.y)
  
  private val point = new java.awt.Point(x, y)
  private val size = CalcHelp.bound(CalcHelp.gaussianInteger(1.5, 1.05), 1, 10)

  val r = CalcHelp.gaussianInteger(240, 30)
  val g = CalcHelp.gaussianInteger(240, 30)
  val b = CalcHelp.gaussianInteger(240, 40)
  val color = CalcHelp.correctColor(r, g, b)

  def draw(dx: Int, dy: Int, g: Graphics): Unit = {
    g.setColor(color)
    g.fillOval(point.x + dx, point.y + dy, size, size)
  }

  /**
   * Draws the star.
   * @param g the Graphics component to draw with
   */
  def draw(g: Graphics): Unit = draw(0, 0, g)  
  
}