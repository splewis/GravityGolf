package editor;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

/**
 * @author Sean Lewis
 */
class LevelFrame extends JFrame implements KeyListener {

	private LevelPanel lp = new LevelPanel();

	/**
	 * 
	 */
	public LevelFrame() {
		getContentPane().add(lp);
		this.addKeyListener(this);
		this.setTitle("Gravity Golf");
		this.setSize(1010, 730);
		this.setResizable(false);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"images/icon.png"));
		this.setVisible(true);
		setJMenuBar(lp.menuBar);
	}

	public void keyPressed(KeyEvent event) {
		int key = event.getKeyCode();
		switch (key) {
		case KeyEvent.VK_R:
			lp.reset();
			break;
		case KeyEvent.VK_UP:
			lp.dy += 100;
			break;
		case KeyEvent.VK_DOWN:
			lp.dy -= 100;
			break;
		case KeyEvent.VK_LEFT:
			lp.dx += 100;
			break;
		case KeyEvent.VK_RIGHT:
			lp.dx -= 100;
			break;
		}
	}

	public void keyReleased(KeyEvent event) {
	}

	public void keyTyped(KeyEvent event) {
	}

}
