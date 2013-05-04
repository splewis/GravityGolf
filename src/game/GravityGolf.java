package game;

/**
 * Top-level launching for the game.
 * @author Sean Lewis
 */
final class GravityGolf {

	/** Game log tracker. Outputs to logs\gamelog.txt */
	DataHandler DataWriter = new DataHandler();

	/** Launches the game. */
	public static void main(String[] args) {
		new GameFrame();
	}

}