package tests;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

import structures.Level;
import structures.Randomizer;

public class RandomLevelTest extends JPanel {

	private static Level level = null;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(1200, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("RandomLevelTest - press any key to get a new level");
		frame.setVisible(true);
		frame.setFocusable(true);
		frame.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				newLevel();
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		frame.add(new RandomLevelTest());
	}

	public RandomLevelTest() {
		setSize(getWidth(), getHeight());
	}
	
	private static void newLevel() {
		level = Randomizer.randomLevel();
		level.generateLevelData();
		System.out.println(level);
	//	System.out.println(level.estimateDifficulty());
	}

	@Override
	public void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		Graphics2D g = (Graphics2D) gr;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_NORMALIZE);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		
		if (level != null) {
			level.draw(g);
			level.getBall().draw(g);
		}
		repaint();
	}

}