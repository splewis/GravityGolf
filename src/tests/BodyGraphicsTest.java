package tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import structures.*;

public class BodyGraphicsTest extends JFrame {

	private JPanel panel = new JPanel();
	private int numBodies;
	private int largestSize;
	private int smallestSize;
	private List<Body> bodies;
	private boolean drawOutline = false;

	public static void main(String[] args) {
		new BodyGraphicsTest(10, 100, 10);
	}

	public BodyGraphicsTest(int numBodies, int largestSize, int smallestSize) {
		this.numBodies = numBodies;
		this.largestSize = largestSize;
		this.smallestSize = smallestSize;
		setSize(1200, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("BodyGraphicsTest - press any key to draw outlines");
		panel.setSize(getWidth(), getHeight());
		setVisible(true);
		makeBodies();
		setFocusable(true);
		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				drawOutline = !drawOutline;
				repaint();
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});
	}

	public void makeBodies() {
		bodies = new ArrayList<Body>();
		final int centerY = getHeight() / 2;
		int centerX = 3 * smallestSize;
		int deltaSize = (largestSize - smallestSize) / numBodies;
		for (int i = 0; i < numBodies; i++) {
			Color c = CalcHelp.getShiftedColor(new Color(127, 127, 127), 126);
			int r = deltaSize * i + smallestSize;
			Body b = new Body(centerX, centerY, r, c);
			bodies.add(b);
			centerX += 2 * deltaSize * (i + 1) + smallestSize + 10;
		}
	}

	public void paint(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_NORMALIZE);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		for (Body b : bodies) {
			b.draw(g);
			if (drawOutline) {
				g.setColor(Color.red);
				g.drawOval((int) b.getCenter().x - b.getRadius(),
						(int) b.getCenter().y - b.getRadius(), b.getDiameter(),
						b.getDiameter());
			}
		}
	}

}