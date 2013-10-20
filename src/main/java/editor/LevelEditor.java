package editor;

import javax.swing.JFrame;

/**
 * Top-level class for launching the level editor.
 * @author Sean Lewis
 */
public class LevelEditor {
	public static void main(String[] args) {
		LevelFrame lf = new LevelFrame();
		lf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}