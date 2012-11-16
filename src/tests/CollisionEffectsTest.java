package tests;

import graphics.CollisionEffect;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import structures.*;

public class CollisionEffectsTest extends JPanel {

	private static Level level;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Collision Effects Test - press any key to start");
		frame.setVisible(true);
		frame.setFocusable(true);
		frame.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				start();
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
		frame.add(new CollisionEffectsTest());

		Ball ball = new Ball(200, 300, 3);
		ArrayList<Body> bodies = new ArrayList<Body>();
		bodies.add(new Body(500, 300, 150, Color.blue));
		level = new Level(ball, bodies, null, null, null, 5.0, 1.0);
		level.generateLevelData();
	}

	private static void start() {
		CollisionEffect.kill();
		level.reset();
		level.getBall().setVelocity(new Vector2d(2.5, 0));
		level.getBall().setLaunched(true);
	}

	public CollisionEffectsTest() {
		setSize(getWidth(), getHeight());
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

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			level.updateLevel();

			double xS = level.getScreenXShift();
			double yS = level.getScreenYShift();

			if (level.timeToReset() && !CollisionEffect.started()) {
				CollisionEffect.start(level);
			}

			if (CollisionEffect.running()) {
				int[] vals = CollisionEffect.update();
				xS += vals[0];
				yS += vals[1];
			} else {
				CollisionEffect.kill();
			}
			level.draw((int) xS, (int) yS, g);
			if (CollisionEffect.running()) {
				CollisionEffect.draw(g);
			} else if(!CollisionEffect.running() && level.timeToReset()) {
				level.reset();
			} else {
				level.getBall().draw(xS, yS, g);
			}
			repaint();
		}

	}

}