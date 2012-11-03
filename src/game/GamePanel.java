package game;

import structures.Ball;
import structures.Point2d;

import graphics.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import structures.*;

/**
 * @author Sean Lewis
 */
public class GamePanel extends JPanel implements ActionListener, MouseListener,
		MouseMotionListener, Runnable {

	public static final int Width = 1000;
	public static final int Height = 700;

	boolean showSolution = false;
	ArrayList<java.awt.Point> points = new ArrayList<java.awt.Point>();

	// Game Components
	public static final int MaxInitialMagnitude = 300;
	public static final int ArrowLength = 200; // affects how large drawn
												// vectors are relative to those

	public static final int EffectsNum = 0;		
	public static final int VectorsNum = 1;
	public static final int ResultantNum = 2;
	public static final int TrailNum = 3;
	public static final int WarpArrowsNum = 4;
	public static final int[] DEFAULT_SETTINGS = {
		1, 0, 0, 1, 0, 3
	};
	// User-set settings
	private boolean settings[] = new boolean[5];
	ArrayList<Point2d> trailPoints = new ArrayList<Point2d>(); 
	
	boolean drawingEffects;
	boolean blinkingBall = true;
	long effectStartTime; // stores time of collision for effects
	ArrayList<Particle> particles;
	// Game data
	private double launchAngle, launchMagnitude;
	private Point2d initialPoint, terminalPoint; // for the initial velocity
	boolean drawingInitialVelocity, gameStarted, gamePaused, gameWon;

	private GameManager gameManager;
	// Current Level data storage
	boolean errorFound;
	private Level currentLevel;
	private Ball ball;
	boolean levelComplete;
	double screenXShift, screenYShift;

	// Graphics Components
	// Main components
	int speed = 2;
	private Thread animator;
	private volatile boolean running;
	private int paints;
	// Special effect values
	static final int SpecialEffectTime = 1000; // ms
	double[] shakeValues; // random values for screen shake, set on each
							// collision
	static final int TimeBetweenFlashes = 250; // ms

	// display
	static final String[] instructionStrings = { "H: Help", "P: pause",
			"R: reset", "Right arrow: speed up",
			"Left arrow: slow down",
			// break
			"S: hide stars", "V: show gravity vectors",
			"D: show gravity resultant", "T: show ball trail",
			"E: show special effects" };
	// Menu Components
	JMenuBar menuBar = new JMenuBar();
	JMenu settingsMenu = new JMenu("Settings");
	JCheckBoxMenuItem[] settingsBoxes = {
			new JCheckBoxMenuItem("Collision Effects"),
			new JCheckBoxMenuItem("Gravity Vectors"),
			new JCheckBoxMenuItem("Gravity Resultant"),
			new JCheckBoxMenuItem("Baill Trail"),
			new JCheckBoxMenuItem("Warp direction arrows"), };
	JMenu speedMenu = new JMenu("Speed");

	JRadioButtonMenuItem[] speedButtons = {
			new JRadioButtonMenuItem("Very Slow"),
			new JRadioButtonMenuItem("Slow"),
			new JRadioButtonMenuItem("Medium"),
			new JRadioButtonMenuItem("Fast"),
			new JRadioButtonMenuItem("Very Fast"),
			new JRadioButtonMenuItem("Light speed") };
	ButtonGroup speedButtonGroup = new ButtonGroup();

	JMenu controlMenu = new JMenu("Control");
	JMenuItem pauseItem = new JMenuItem("Pause game");
	JMenuItem resetLevelItem = new JMenuItem("Reset level");
	JMenu saveMenu = new JMenu("Save");
	JMenuItem saveItem = new JMenuItem("Save current game");
	JMenu loadMenu = new JMenu("Load");
	JMenuItem loadItem = new JMenuItem("Load game");
	
	
	

	public GamePanel() throws IOException {
		gameManager = new GameManager();
		int[] importedSettings = DataReader.getSettings();
		for (int i = 0; i < settings.length; i++) {
			settings[i] = (importedSettings[i] == 1);
		}
		speed = importedSettings[settings.length];
		currentLevel = gameManager.getCurrentLevel();
		initializeMenu();
		initialPoint = currentLevel.getBall().getCenter();
		addMouseListener(this);
		addMouseMotionListener(this);
		setDoubleBuffered(true);
		setBackground(Color.black);

	}

	private void initializeMenu() {
		for (int i = 0; i < settingsBoxes.length; i++) {
			settingsMenu.add(settingsBoxes[i]);
			settingsBoxes[i].addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(e.getSource() == settingsBoxes[TrailNum]) {
						trailPoints.clear();
					}
					for (int i = 0; i < settingsBoxes.length; i++) {
						settings[i] = settingsBoxes[i].getState();
					}
				}
			});
			settingsBoxes[i].setState(settings[i]);
		}
		settingsBoxes[VectorsNum].setMnemonic(KeyEvent.VK_V);
		settingsBoxes[ResultantNum].setMnemonic(KeyEvent.VK_R);
		settingsBoxes[TrailNum].setMnemonic(KeyEvent.VK_T);
		settingsBoxes[EffectsNum].setMnemonic(KeyEvent.VK_E);
		menuBar.add(settingsMenu);

		for (int i = 0; i < speedButtons.length; i++) {
			speedMenu.add(speedButtons[i]);
			speedButtonGroup.add(speedButtons[i]);

		}

		menuBar.add(speedMenu);

		controlMenu.add(pauseItem);
		controlMenu.add(resetLevelItem);
		pauseItem.addActionListener(this);
		resetLevelItem.addActionListener(this);
		pauseItem.setMnemonic(KeyEvent.VK_P);
		resetLevelItem.setMnemonic(KeyEvent.VK_R);
		menuBar.add(controlMenu);

		saveMenu.add(saveItem);
		saveItem.addActionListener(this);
		menuBar.add(saveMenu);

		loadMenu.add(loadItem);
		loadItem.addActionListener(this);
		menuBar.add(loadMenu);

		speedButtons[speed].setSelected(true);

	}

	public void addNotify() {
		super.addNotify();
		startGame();
	}

	public void startGame() {
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	}

	public void run() {
		int period = 7;
		running = true;
		long t = System.currentTimeMillis();
		while (running) {
			t = System.currentTimeMillis();

			if (!levelComplete) {
				speed = 2;
				for (int i = 0; i < speedButtons.length; i++) {
					if (speedButtons[i].isSelected()) {
						speed = i;
						break;
					}
				}
				switch (speed) {
				case 0:
					gameUpdate(1);
					period = 9;
					break;
				case 1:
					gameUpdate(1);
					period = 7;
					break;
				case 2:
					gameUpdate(2);
					period = 7;
					break;
				case 3:
					gameUpdate(4);
					period = 7;
					break;
				case 4:
					gameUpdate(6);
					period = 7;
					break;
				case 5:
					gameUpdate(10);
					period = 5;
					break;
				}

			}

			repaint();

			long dt = System.currentTimeMillis() - t;
			if (dt < period) {
				if (dt < period)
					sleep(period - dt);
			} else if (dt > period) {
				// System.out.println("Skip");
			}
		}
		GravityGolf.DataWriter.close();
		System.exit(0);
	}

	private void gameUpdate(int n) {
		for (int i = 0; i < n; i++) {
			gameUpdate();
		}
	}

	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	/*
	 * Handles extra game logic - Sets initial special effect collision time -
	 * Updates the level if current level is completed
	 */
	private void gameUpdate() {

		if (!gameManager.isGameOver()) {
			currentLevel = gameManager.getCurrentLevel();
			ball = currentLevel.getBall();
			if (!gamePaused || !gameStarted) {
				currentLevel.updateLevel();
			}
			if (settings[TrailNum] && ball.isLaunched() && paints > 2) {
				trailPoints.add(new Point2d(ball.getCenter().x, ball
						.getCenter().y));
			}
			if (currentLevel.timeToReset() && !drawingEffects
					&& settings[EffectsNum]) {
				Body intersected = currentLevel.getIntersectingBody();
				particles = ball.generateParticles(intersected);
				shakeValues = new double[6];
				double speed = ball.getVelocity().getLength();
				
				if (speed < .25)
					speed = .25;
				if (speed > 2.5)
					speed = 2.5;

				double shakeFactor = 3 * speed / currentLevel.getFollowFactor();
				if (currentLevel.getFollowFactor() == 0.0)
					shakeFactor = 25 * speed;
				// 1st value is multiplicative factor
				// TODO: make based of rate at which screen shifts
				int sign1 = -1;
				if (ball.getCenter().x + screenXShift < 0) {
					sign1 = 1;
				} else if (ball.getCenter().x + screenXShift > 0) {
					sign1 = CalcHelp.randomSign();
				}
				shakeValues[0] = CalcHelp.randomDouble(35, 40) * shakeFactor
						* sign1;

				int sign2 = -1;
				if (ball.getCenter().y + screenYShift < 0) {
					sign2 = 1;
				} else if (ball.getCenter().y + screenYShift > 0) {
					sign2 = CalcHelp.randomSign();
				}
				shakeValues[3] = CalcHelp.randomDouble(35, 40) * shakeFactor
						* sign2;

				// 2nd value is sinusoidal factor
				shakeValues[1] = CalcHelp.randomDouble(45, 50);
				shakeValues[4] = CalcHelp.randomDouble(45, 50);

				// 3rd value is exponential factor
				shakeValues[2] = CalcHelp.randomDouble(-.0035, -.0045);
				shakeValues[5] = CalcHelp.randomDouble(-.0035, -.0045);

				ball.setLaunched(false);
				effectStartTime = System.currentTimeMillis();
				drawingEffects = true;
			} else if (!levelComplete && currentLevel.inGoalPost() && !gameWon) {
				levelComplete = true;
			} else if (currentLevel.timeToReset() && !settings[EffectsNum]) {
				resetLevel();
			}
		}

	}

	public void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		super.paintComponent(g);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_NORMALIZE);
		paints++;
		if (gameStarted && !gameWon) {
			ball = currentLevel.getBall();
			screenXShift = currentLevel.getScreenXShift();
			screenYShift = currentLevel.getScreenYShift();
			initialPoint = new Point2d((int) Math.round(ball.getCenter().x
					+ screenXShift), (int) Math.round(ball.getCenter().y
					+ screenYShift));

			drawLevel(g);

			Color textColor = null;
			if (ball.isLaunched()) {
				textColor = Color.green;
			} else if (drawingInitialVelocity) {
				textColor = Color.white;
			} else if (drawingEffects) {
				textColor = Color.red;
			}
			if (textColor != null && terminalPoint != null) {
				InfoDisplay.vectorInformation(terminalPoint, launchMagnitude,
						launchAngle, textColor, g);
			}

			if (!gameWon && gameStarted) {
				InfoDisplay.levelInformation(gameManager, g);
			}
			if (levelComplete) {
				g.setColor(Color.GREEN);
				g.drawString("Level Complete", 10, 60);
				g.drawString("Click to continue", 10, 80);
			}
			if (!ball.isLaunched() && drawingInitialVelocity) {
				initialPoint = new Point2d((int) Math.round(ball.getCenter().x
						+ screenXShift), (int) Math.round(ball.getCenter().y
						+ screenYShift));
				double angle = CalcHelp.getAngle(initialPoint, terminalPoint);
				g.setColor(Color.white);
				if (initialPoint.getDistance(terminalPoint) <= MaxInitialMagnitude) {
					GraphicEffect.drawArrow(initialPoint, terminalPoint, g);
				} else {
					double xSide = MaxInitialMagnitude * Math.cos(angle);
					double ySide = MaxInitialMagnitude * -Math.sin(angle);
					Point2d tempTerminalPoint = new Point2d(
							(initialPoint.x + xSide), (initialPoint.y + ySide));
					GraphicEffect.drawArrow(initialPoint, tempTerminalPoint, g);
				}
			}
			if (settings[VectorsNum] && !drawingEffects) {
				GravityVectorsEffect.draw(currentLevel, g);
			}
			if (settings[ResultantNum] && !drawingEffects) {
				ResultantDrawer.draw(currentLevel, g);
			}
			if (gamePaused) {
				InfoDisplay.drawPaused(g);
			}
		} else {
			screenXShift = 0.0;
			screenYShift = 0.0;
			drawLevel(g);
			MenuScreen.draw(currentLevel, settings, g);
		}
		if (settings[WarpArrowsNum]) {
			WarpDrawer.draw(currentLevel, g);
		}
		if (gameWon) {
			InfoDisplay.drawWinScreen(gameManager, g);
		}
		if (levelComplete) {
			InfoDisplay.drawNextLevelMessage(g);
		}
	}

	public void resetLevel() {
		levelComplete = false;
		drawingEffects = false;
		trailPoints.clear();
		currentLevel.reset();
		drawingInitialVelocity = false;
	}

	private void drawLevel(Graphics2D g) {

		long t = System.currentTimeMillis();
		ball = currentLevel.getBall();
		int xShift = (int) Math.round(screenXShift);
		int yShift = (int) Math.round(screenYShift);

		int dt = (int) (t - effectStartTime);
		if (gamePaused) {
			effectStartTime = t;
		}
		if (settings[EffectsNum] && dt < SpecialEffectTime && drawingEffects) {
			// Dampened harmonic motion on collision
			if (!gamePaused) {
				screenXShift += (shakeValues[0] * Math.sin(dt / shakeValues[1]) * Math
						.exp(shakeValues[2] * dt));
				xShift = (int) Math.round(screenXShift);
				screenYShift += (shakeValues[3] * Math.sin(dt / shakeValues[4]) * Math
						.exp(shakeValues[5] * dt));
				yShift = (int) Math.round(screenYShift);
			}

			for (int i = 0; i < particles.size(); i++) {
				Particle b = particles.get(i);

				if (!gamePaused) {
					for (Body bod : currentLevel.getBodies()) {
						if (bod.intersects(b)
								|| bod.getCenter().getDistanceSquared(
										b.getCenter()) <= (bod.getRadius() * .5)
										* (bod.getRadius() * .5) && i != 0) {
							particles.remove(i);
							i--;
						}
					}
					for (Blockage blockage : currentLevel.getBlockages()) {
						if (blockage.intersects(b.getCenter()) && i != 0) {
							particles.remove(i);
							i--;
						}
					}
					for (GoalPost gp : currentLevel.getGoalPosts()) {
						if (b.intersects(gp) && i != 0) {
							particles.remove(i);
							i--;
						}
					}
					b.setVelocity(b.getVelocity().multiply(0.99));
					b.move();
				}
				b.draw(screenXShift, screenYShift, g);
			}

		} else {
			if ((drawingEffects || !(settings[EffectsNum])
					&& currentLevel.timeToReset())) {
				resetLevel();
			}
		}

		currentLevel.draw((int) screenXShift, (int) screenYShift, g);

		// TODO: warp points should not need secondary drawing
		for (WarpPoint w : currentLevel.getWarpPoints()) {
			w.draw(xShift, yShift, g);
		}
		for (Body b : currentLevel.getBodies()) {
			for (Moon m : b.getMoons()) {
				m.draw(screenXShift, screenYShift, g);
			}
		}

		if (settings[TrailNum] && ball.isLaunched()) {
			TrailEffect.draw(trailPoints, currentLevel, g);
		}

		if (showSolution) {
			g.setColor(Color.green);
			for (java.awt.Point p : points) {
				g.fillRect(p.x + xShift, p.y + yShift, 1, 1);
			}
		}

		if (!drawingEffects) { // prevents flicker of ball on reset after
								// effects
			if (!ball.isLaunched()) { // stationary ball
				Color c = ball.getColor();
				if (blinkingBall) {
					if (t % (TimeBetweenFlashes * 2) <= TimeBetweenFlashes) {
						c = Color.white;
					}
					g.setColor(c);
				} else {
					g.setColor(ball.getColor());
				}
				ball.draw(screenXShift, screenYShift, g, c);

			} else { // moving ball
				ball.draw(screenXShift, screenYShift, g, ball.getColor());
			}
		}

		if (settings[EffectsNum] && dt < SpecialEffectTime && drawingEffects) {
			for (Particle p : particles) {
				p.draw(screenXShift, screenYShift, g);
			}
		}

		if (gameManager.getLevelNumber() == 1 && gameStarted) {
			GoalPost goal = currentLevel.getGoalPosts().get(0);
			g.setColor(Color.white);
			g.drawString("Aim here!", (int) (goal.getCenter().x - 20 + xShift),
					(int) (goal.getCenter().y - 70 + yShift));

			Point2d p1 = new Point2d(goal.getCenter().x - 5 + xShift,
					goal.getCenter().y - 65 + yShift);
			Point2d p2 = goal.getCenter().translate(xShift,
					yShift - goal.getRadius() - 3);
			GraphicEffect.drawArrow(p1, p2, 0, 5, g);
		}

	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == pauseItem) {
			gamePaused = !gamePaused;
		} else if (event.getSource() == resetLevelItem) {
			if (gameStarted)
				resetLevel();
		} else if (event.getSource() == saveItem) {
			JFileChooser chooser = new JFileChooser(
					System.getProperty("user.dir") + "/saves");
			chooser.setApproveButtonText("Save");
			chooser.setDialogTitle("Save");
			chooser.setFileFilter(new FileNameExtensionFilter(".txt files",
					"txt"));
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				chooser.getSelectedFile().getName();
			}
			try {
				String name = chooser.getSelectedFile().getName();
				if (name.substring(name.length() - 3, name.length()).equals(
						".txt")) {
					DataWriter.saveGame(gameManager, new File("saves/" + name));
				} else {
					DataWriter.saveGame(gameManager, new File("saves/" + name + ".txt"));
				}
			} catch (Exception e) {
			}

		} else if (event.getSource() == loadItem) {
			killSpecialEffects();
			JFileChooser chooser = new JFileChooser(
					System.getProperty("user.dir") + "/saves");
			chooser.setApproveButtonText("Load");
			chooser.setDialogTitle("Load");
			chooser.setFileFilter(new FileNameExtensionFilter(".txt files",
					"txt"));
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				chooser.getSelectedFile().getName();
			}
			try {
				gameStarted = true;
				gamePaused = false;
				gameManager = new GameManager();
				gameManager.loadSave(chooser.getSelectedFile());
			} catch (Exception e) {
			}

		}
	}

	public void beginGame() throws IOException {
		gameWon = false;
		gameStarted = true;
		gamePaused = false;
		gameManager.nextLevel();
	}

	public void mousePressed(MouseEvent event) {
		mouseDragged(event);
	}

	public void mouseDragged(MouseEvent event) {
		if (!currentLevel.getBall().isLaunched()
				&& !gamePaused
				&& initialPoint.getDistance(new Point2d(event.getPoint())) > 2 * 4
				// 4 is 1 above typical ball radius, so it is used here
				&& !drawingEffects) {
			terminalPoint = new Point2d(event.getPoint().getX(), event
					.getPoint().getY());
			launchMagnitude = initialPoint.getDistance(terminalPoint);
			if (launchMagnitude > MaxInitialMagnitude) {
				launchMagnitude = MaxInitialMagnitude;
			}
			drawingInitialVelocity = true;
			launchAngle = CalcHelp.getAngle(initialPoint, terminalPoint);
			if (launchAngle < 0)
				launchAngle += (2 * Math.PI);
		}
	}

	public void mouseReleased(MouseEvent event) {
		trailPoints.clear();
		blinkingBall = false; // the first launch will disable all blinking
								// (reset upon getting to next level)
		if (!gamePaused && !ball.isLaunched() && !drawingEffects
				&& drawingInitialVelocity) {
			gameManager.swingTaken();
			launchMagnitude = initialPoint.getDistance(terminalPoint);
			launchAngle = CalcHelp.getAngle(initialPoint, terminalPoint);
			if (launchAngle < 0)
				launchAngle += (2 * Math.PI); // so the display only shows the
												// positive coterminal angle
												// (300 instead of -60)
			if (launchMagnitude > MaxInitialMagnitude)
				launchMagnitude = MaxInitialMagnitude;

			GravityGolf.DataWriter.ballLaunched(event.getPoint(),
					launchMagnitude, launchAngle);
			double xLength = Math.cos(launchAngle) * launchMagnitude;
			double yLength = -Math.sin(launchAngle) * launchMagnitude;
			ball.setVelocity(new Vector2d(xLength / 200, yLength / 200));
			ball.setLaunched(true);
		}

		if (levelComplete) {
			levelComplete = false;
			blinkingBall = true;
			boolean moreLevels = gameManager.nextLevel();
			trailPoints.clear();
			if (moreLevels) {
				ball.setLaunched(false);
			} else {
				gameWon = true;
				gamePaused = true;
			}
		}

		drawingInitialVelocity = false;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void switchSetting(int index) {
		settings[index] = !settings[index];
		settingsBoxes[index].setSelected(settings[index]);
	}

	public void safeQuit() {
		try {
			int speed = 3;
			for (int i = 0; i < speedButtons.length; i++) {
				if (speedButtons[i].isSelected()) {
					speed = i;
					break;
				}
			}
			DataWriter.printSettings(settings, speed);
		} catch (IOException e) {
		}
		running = false;
	}

	private void killSpecialEffects() {
		effectStartTime = effectStartTime - SpecialEffectTime - 1;
		drawingEffects = false;
	}

	public void mouseClicked(MouseEvent event) {
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}

	public void mouseMoved(MouseEvent event) {
	}

}
