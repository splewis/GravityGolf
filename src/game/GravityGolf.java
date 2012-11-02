package game;

import java.io.IOException;
import javax.swing.JFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Sean Lewis
 */

public class GravityGolf {
	public static void main(String[] args) throws IOException {
		final GameFrame gf = new GameFrame();
		gf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				gf.safeQuit(); // ensures gamelog.txt and settings.txt will be
								// written to on close
				gf.dispose();
			}
		});
	}
}