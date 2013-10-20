package game

/**
 * Top-level launching for the game.
 * @author Sean Lewis
 */
object GravityGolf {

  /** Game log tracker. Outputs to logs\gamelog.txt */
  val DataWriter: DataHandler = new DataHandler

  /** Launches the game. */
  def main(args: Array[String]): Unit = {
    new GameFrame()
  }

}
