package graphics;

import java.awt.Color
import java.awt.Font;
import java.awt.Graphics
import java.util.ArrayList
import game._
import structures._

/**
 * Class for the Menu Screen functions.
 * @author Sean Lewis
 */
object MenuScreen {

  private def instructionStrings = Array(
    "P: pause",
    "R: reset",
    "Right arrow: speed up",
    "Left arrow: slow down",
    // break
    "V: show gravity vectors",
    "D: show gravity resultant",
    "T: show ball trail",
    "E: show special effects")
  private var menuLevel: Level = null

  /**
   * Returns the start level that the game uses.
   * @return the menu Level
   */
  def getMenuLevel(): Level = {
    if (menuLevel == null) {
      val b = new Ball(340, 335, 3, Color.red)
      val bod = new ArrayList[Body]()
      bod.add(new Body(495, 335, 100, Color.magenta))
      val ws = new ArrayList[WarpPoint]()
      val gs = new ArrayList[GoalPost]()
      val bs = new ArrayList[Blockage]()
      b.setLaunched(true)
      b.accelerate(new Vector2d(0.0, 1.8))
      menuLevel = new Level(b, bod, ws, gs, bs, 0, 3.5)
      menuLevel.generateLevelData()
    }
    return menuLevel
  }

  /**
   * Draws the menu screen and its information.
   * @param menuLevel the level that is displayed
   * @param settings the current settings in the game
   * @param g the Graphics component to draw with
   */
  def draw(menuLevel: Level, settings: Array[Boolean], g: Graphics) {
    if (settings(GamePanel.VectorsNum))
      GravityVectorsEffect.draw(menuLevel, g)
    if (settings(GamePanel.ResultantNum))
      ResultantDrawer.draw(menuLevel, g)

    g.setColor(Color.blue)
    g.setFont(new Font("Tahoma", Font.ITALIC, 80))
    g.drawString("Gravity Golf", 275, 100)
    
    g.setFont(new Font("Times new Roman", Font.ITALIC, 25))    
    g.setColor(Color.blue)
    for (i <- 0 to 3)
      g.drawString(instructionStrings(i), 50, 60 * i + 235)

    for (i <- 4 to 7)
      g.drawString(instructionStrings(i), 700, 60 * (i - 4) + 235)

    g.setFont(new Font("Times new Roman", Font.ITALIC, 20))
    g.setColor(Color.green)
    g.drawString("Your goal is to give the ball an initial velocity that "
      + "allows it reach the white goal.", 140, 550)

    g.setColor(Color.white)
    g.drawString("Chose an option below to begin", 345, 590)
    g.drawString("1.00", 10, 630)
    g.drawString("1/1/2013", 10, 660)
  }

}