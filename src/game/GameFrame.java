package game;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 * Frame level control for the game. Tracks key presses for option changes.
 * @author Sean Lewis
 */

public class GameFrame extends JFrame implements KeyListener {

	private GamePanel gp;

	public GameFrame() {
		gp = new GamePanel();
		if (!gp.errorFound) {
			getContentPane().add(gp);
			this.setTitle("Gravity Golf");
			// arbitrary additive constants to create a real gameplay area size
			// as desired
			this.setSize(GamePanel.Width + 8, GamePanel.Height + 33);
			this.setResizable(false);
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(
					"images/icon.png"));
			setJMenuBar(gp.menuBar);
			this.addKeyListener(this);
			setFocusable(true);
			this.setVisible(true);
		} else {
			this.dispose();
		}
	}

	public void safeQuit() {
		gp.safeQuit();
	}

	public void keyPressed(KeyEvent event) {
		int key = event.getKeyCode();
		switch (key) {

		case KeyEvent.VK_D:
			gp.switchSetting(GamePanel.ResultantNum);
			break;

		case KeyEvent.VK_E:
			gp.switchSetting(GamePanel.EffectsNum);
			break;

		case KeyEvent.VK_P:
			gp.pause();
			break;

		case KeyEvent.VK_R:
			if (gp.isGameStarted())
				gp.resetLevel();
			break;

		case KeyEvent.VK_T:
			gp.switchSetting(GamePanel.TrailNum);
			break;

		case KeyEvent.VK_V:
			gp.switchSetting(GamePanel.VectorsNum);
			break;

		case KeyEvent.VK_RIGHT:
			if (!gp.speedButtons[4].isSelected())
				gp.speedButtons[gp.speed + 1].setSelected(true);

			break;

		case KeyEvent.VK_LEFT:
			if (!gp.speedButtons[0].isSelected())
				gp.speedButtons[gp.speed - 1].setSelected(true);

			break;

		}
	}

	public void keyReleased(KeyEvent event) {
		// do nothing
	}

	public void keyTyped(KeyEvent event) {
		// do nothing
	}

}