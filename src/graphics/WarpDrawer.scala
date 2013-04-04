package graphics

import java.awt.Color
import java.awt.Graphics
import java.util.List
import structures._

/**
 * Holder class for the Warp Drawing effect.
 * @author Sean Lewis
 */
object WarpDrawer {

  /**
   * Draws the warp arrows effect.
   * @param level the current Level
   * @param g the Graphics component to draw with
   */
  def draw(level: Level, g: Graphics): Unit = {
    g.setColor(Color.white)
    val warps = level.getWarpPoints()
    val shift = level.getShift
    
    for (i <- 1 until warps.size()) {
      val p1 = warps.get(i - 1).getCenter + shift
      val p2 = warps.get(i)    .getCenter + shift
      drawArrow(p1, p2, 0, 25, g)
    }
  }

}