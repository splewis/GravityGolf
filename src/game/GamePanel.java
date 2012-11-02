package game;

import structures.Point2d;

import graphics.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import structures.*;

/**
 * @author Sean Lewis
 */
public class GamePanel extends JPanel implements ActionListener, MouseListener,
		MouseMotionListener, ItemListener, Runnable {

	public static final int Width = 1000;
	public static final int Height = 700;

	boolean showSolution = false;
	ArrayList<java.awt.Point> points = new ArrayList<java.awt.Point>();

	// Game Components
	public static final int MaxInitialMagnitude = 300;
	public static final int ArrowLength = 200; // affects how large drawn
												// vectors are relative to those
												// vectors' magnitude
	public static final int VectorsNum = 0;
	public static final int ResultantNum = 1;
	public static final int TrailNum = 2;
	public static final int EffectsNum = 3;
	public static final int WarpArrowsNum = 4;
	private PrintWriter gameWriter;
	// User-set settings
	private boolean settings[] = new boolean[5];
	ArrayList<Point2d> trailPoints = new ArrayList<Point2d>(); // stores all
																// past values
																// in the ball's
																// trail
	ArrayList<Star> stars = new ArrayList<Star>();
	boolean drawingEffects;
	boolean blinkingBall = true;
	long effectStartTime; // stores time of collision for effects
	ArrayList<Particle> particles;
	// Game data
	private double launchAngle, launchMagnitude;
	private Point2d initialPoint, terminalPoint; // for the initial velocity
	private int[] swingData; // keeps track of swings for each level
	private int numSwingsTaken; // keeps total sum of all swings
	boolean drawingInitialVelocity, gameStarted, gamePaused, gameWon;

	// Current Level data storage
	boolean errorFound;
	private ArrayList<Level> levels;
	private Level currentLevel;
	Ball ball;
	ArrayList<Body> bodies;
	ArrayList<WarpPoint> warps;
	ArrayList<Blockage> blockages;
	ArrayList<GoalPost> goals;
	double followFactor;
	double dXShift, dYShift;
	// values for default shift values for levels - used for preventing ball
	// flicker on resets
	private int currentLevelN;
	boolean levelComplete;
	double screenXShift, screenYShift;

	// Graphics Components
	// Main components
	int speed = 2;
	private Thread animator;
	private volatile boolean running;
	private int paints;
	private boolean gameOver;
	// If ArrowAngle is changed, the drawArrow() method must be updated - see
	// its comments for details.
	// Special effect values
	static final int SpecialEffectTime = 1000; // ms
	double[] shakeValues; // random values for screen shake, set on each
							// collision
	static final int TimeBetweenFlashes = 250; // ms

	// Fonts n' shit
	private static final DecimalFormat DecimalFormatter = new DecimalFormat("#0.00"); // for
																				// angle/magnitude
																				// display
																				// (2
																				// decimals)
	public static final Font TitleFont = new Font("Tahoma", Font.ITALIC, 80);
	public static final Font AuthorFont = new Font("Tahoma", Font.ITALIC, 30);
	public static final Font MediumFont = new Font("Times new Roman", Font.ITALIC, 25);
	public static final Font SmallFont = new Font("Times new Roman", Font.ITALIC, 20);
	public static final Font InfoFont = new Font("Times new Roman", Font.PLAIN, 12); // angle/magnitude
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
			new JCheckBoxMenuItem("Gravity vectors"),
			new JCheckBoxMenuItem("Gravity resultant"),
			new JCheckBoxMenuItem("Ball trail"),
			new JCheckBoxMenuItem("Collision effects"),
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

	// Menu Level Data
	Level menuLevel;

	public GamePanel() throws IOException {
		importData();
		gameWriter = new PrintWriter(new File("logs/gamelog.txt"));
		initializeMenu();
		setLevelUp(menuLevel);
		ball.setLaunched(true);
		ball.accelerate(new Vector2d(0.0, 1.8));
		initialPoint = ball.getCenter();
		addMouseListener(this);
		addMouseMotionListener(this);
		setDoubleBuffered(true);
		setBackground(Color.black);
	}

	private void initializeMenu() {
		menuLevel = MenuScreen.getMenuLevel();

		for (int i = 0; i < settingsBoxes.length; i++) {
			settingsMenu.add(settingsBoxes[i]);
			settingsBoxes[i].addItemListener(this);
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

	}

	private void importData() throws IOException {
		DataReader reader = new DataReader();
		levels = reader.getLevelData("levels/levels.txt");
		int[] importedSettings = reader.getSettings();
		for (int i = 0; i < settings.length; i++) {
			settings[i] = (importedSettings[i] == 1);
		}
		int speed = importedSettings[settings.length];
		speedButtons[speed].setSelected(true);
		currentLevelN = 0;
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
			// System.out.println(dt);
			if (dt < period) {
				if (dt < period)
					sleep(period - dt);
			} else if (dt > period) {
				// System.out.println("Skip");
			}
		}
		gameWriter.close();
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
		if (!gameOver) {
			if (gameStarted && currentLevelN >= levels.size()) {
				gameWon = true;
				gamePaused = true;
			}
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

				double shakeFactor = 3 * speed / followFactor;
				if (followFactor == 0.0)
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
				gameWriter.println("Level "
						+ Integer.toString(currentLevelN + 1) + " complete. "
						+ Integer.toString(swingData[currentLevelN])
						+ " swings.");
				gameWriter.println();
			} else if (currentLevel.timeToReset() && !settings[EffectsNum]) {
				resetLevel();
			}
		}

	}

	private void loadNextLevel() {
		currentLevelN++;
		levelComplete = false;
		blinkingBall = true;
		if (currentLevelN >= levels.size()) {
			gameWriter.println("Game complete. "
					+ Integer.toString(numSwingsTaken) + " swings total.");
			gameWon = true;
			gamePaused = true;
		} else {
			setLevelUp(currentLevelN);
			trailPoints.clear();
			ball.setLaunched(false);
			levels.set(currentLevelN - 1, null);
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
			bodies = currentLevel.getBodies();
			screenXShift = currentLevel.getScreenXShift();
			screenYShift = currentLevel.getScreenYShift();
			initialPoint = new Point2d((int) Math.round(ball.getCenter().x
					+ screenXShift), (int) Math.round(ball.getCenter().y
					+ screenYShift));

			drawLevel(g);
			g.setFont(InfoFont);

			boolean drawData = false;
			if (ball.isLaunched()) {
				g.setColor(Color.green);
				drawData = true;
			} else if (drawingInitialVelocity) {
				g.setColor(Color.white);
				drawData = true;
			} else if (drawingEffects) {
				g.setColor(Color.red);
				drawData = true;
			}

			if (drawData) {
				InfoDisplay.vectorInformation(terminalPoint, launchMagnitude, launchAngle, g);
			}

			g.setColor(Color.WHITE);
			if (!gameWon) {
				InfoDisplay.levelInformation(currentLevelN + 1, levels.size(),
						swingData[currentLevelN], numSwingsTaken, g);
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
				g.setColor(Color.WHITE);
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
				g.setFont(AuthorFont);
				g.setColor(Color.red);
				g.drawString("PAUSED", 422, 50);
			}
		} else {
			screenXShift = 0.0;
			screenYShift = 0.0;
			drawLevel(g);
			MenuScreen.draw(menuLevel, settings, g);
		}
		if (settings[WarpArrowsNum]) {
			WarpDrawer.draw(currentLevel, g);
		}
		if (gameWon) {
			drawWinScreen(g);
		}
		if (levelComplete && currentLevelN == 0) {
			g.setFont(MediumFont);
			g.setColor(Color.WHITE);
			g.drawString("Click to continue to the next level", 320, 60);
		}
	}

	private void setLevelUp(int l) {
		currentLevel = levels.get(l);
		currentLevel.generateLevelData();
		ball = currentLevel.getBall();
		bodies = currentLevel.getBodies();
		goals = currentLevel.getGoalPosts();
		warps = currentLevel.getWarpPoints();
		blockages = currentLevel.getBlockages();
		stars = currentLevel.getStars();
		followFactor = currentLevel.getFollowFactor();
		dXShift = (500.0 - ball.getCenter().x) / followFactor;
		dYShift = (350.0 - ball.getCenter().y) / followFactor;
		if (l != 0) {
			blinkingBall = true;
		}
		drawingInitialVelocity = false;
		levelComplete = false;
		trailPoints.clear();

		if (showSolution)
			points = currentLevel.getSolutionSet();
	}

	private void setLevelUp(Level l) {
		// used only for setting up menu level
		currentLevel = l;
		if (!currentLevel.isInitialized()) {
			currentLevel.generateLevelData();
		}
		ball = currentLevel.getBall();
		bodies = currentLevel.getBodies();
		goals = currentLevel.getGoalPosts();
		warps = currentLevel.getWarpPoints();
		blockages = currentLevel.getBlockages();
		stars = currentLevel.getStars();
		followFactor = currentLevel.getFollowFactor();
		dXShift = (Width - ball.getCenter().x) / followFactor;
		dYShift = (Height - ball.getCenter().y) / followFactor;
		drawingInitialVelocity = false;
		levelComplete = false;
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
					for (Body bod : bodies) {
						if (bod.intersects(b)
								|| bod.getCenter().getDistanceSquared(
										b.getCenter()) <= (bod.getRadius() * .5)
										* (bod.getRadius() * .5) && i != 0) {
							particles.remove(i);
							i--;
						}
					}
					for (Blockage blockage : blockages) {
						if (blockage.intersects(b.getCenter()) && i != 0) {
							particles.remove(i);
							i--;
						}
					}
					for (GoalPost gp : goals) {
						if (b.intersects(gp) && i != 0) {
							particles.remove(i);
							i--;
						}
					}
					b.setVelocity(b.getVelocity().multiply(0.99)); // (rate for
																	// geometric
																	// slow down
																	// of
																	// particles)
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
		for (WarpPoint w : warps) {
			w.draw(xShift, yShift, g);
		}
		for (Body b : bodies) {
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
				if (screenXShift == dXShift && screenYShift == dYShift) {
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
				}
			} else { // moving ball
				ball.draw(screenXShift, screenYShift, g, ball.getColor());
			}
		}

		if (settings[EffectsNum] && dt < SpecialEffectTime && drawingEffects) {
			for (Particle p : particles) {
				p.draw(screenXShift, screenYShift, g);
			}
		}

		if (currentLevelN == 0 && gameStarted) {
			g.setColor(Color.white);
			g.drawString("Aim here!",
					(int) (goals.get(0).getCenter().x - 20 + xShift),
					(int) (goals.get(0).getCenter().y - 70 + yShift));

			Point2d p1 = new Point2d(goals.get(0).getCenter().x - 5 + xShift,
					goals.get(0).getCenter().y - 65 + yShift);
			Point2d p2 = goals.get(0).getCenter()
					.translate(xShift, yShift - goals.get(0).getRadius() - 3);
			GraphicEffect.drawArrow(p1, p2, 0, 5, g);
		}

	}

	private void drawWinScreen(Graphics2D g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Width + 30, Height);
		g.setFont(TitleFont);
		g.setColor(Color.blue);
		g.drawString("You win!", 140, 75);
		g.setFont(MediumFont);
		g.drawString("It took you a total of " + numSwingsTaken + " swings",
				530, 75);
		g.drawString("Level", 78, 150);
		g.drawString("swings", 187, 150);
		int xFactor = 200;
		if (levels.size() > 40)
			xFactor = 175;
		else if (levels.size() > 50)
			xFactor = 160;
		else if (levels.size() > 60)
			xFactor = 145;
		for (int i = 0; i < levels.size(); i++) {
			g.drawString((i + 1) + ": " + swingData[i], xFactor * (i / 10)
					+ 140, 50 * (i % 10) + 150);
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
					saveGame(new File("saves/" + name));
				} else {
					saveGame(new File("saves/" + name + ".txt"));
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
				loadGame(chooser.getSelectedFile());
			} catch (Exception e) {
			}

		}
	}

	public void beginGame() {
		gameWon = false;
		swingData = new int[levels.size()];
		gameStarted = true;
		gamePaused = false;
		setLevelUp(0);
	}

	public void mousePressed(MouseEvent event) {
		mouseDragged(event);
	}

	public void mouseDragged(MouseEvent event) {
		if (!ball.isLaunched()
				&& !gamePaused
				&& initialPoint.getDistance(new Point2d(event.getPoint())) > 2 * GraphicEffect.ArrowDistanceFromBall
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
		blinkingBall = false; // the first launch will disable all blinking
								// (reset upon getting to next level)
		if (!gamePaused && !ball.isLaunched() && !drawingEffects
				&& drawingInitialVelocity) {
			numSwingsTaken++;
			swingData[currentLevelN]++; // since currentLevel 5 corresponds to
										// index 4 in the array
			launchMagnitude = initialPoint.getDistance(terminalPoint);
			launchAngle = CalcHelp.getAngle(initialPoint, terminalPoint);
			if (launchAngle < 0)
				launchAngle += (2 * Math.PI); // so the display only shows the
												// positive coterminal angle
												// (300 instead of -60)
			if (launchMagnitude > MaxInitialMagnitude)
				launchMagnitude = MaxInitialMagnitude;

			gameWriter.println("Ball launched. Point: (" + event.getX() + ", "
					+ event.getY() + "). Magnitude: " + launchMagnitude
					+ ", Angle: " + Math.toDegrees(launchAngle));
			double xLength = Math.cos(launchAngle) * launchMagnitude;
			double yLength = -Math.sin(launchAngle) * launchMagnitude;
			ball.setVelocity(new Vector2d(xLength / 200, yLength / 200)); // 200
																			// is
																			// a
																			// constant
																			// -
																			// only
																			// used
																			// here
			ball.setLaunched(true);
		}

		if (levelComplete) {
			loadNextLevel();
		}

		drawingInitialVelocity = false;
	}

	public void itemStateChanged(ItemEvent event) {
		for (int i = 0; i < settingsBoxes.length; i++) {
			if (event.getSource() == settingsBoxes[i]) {
				settings[i] = settingsBoxes[i].getState();
				break;
			}
		}
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public boolean getSetting(int n) {
		return settings[n];
	}

	public void setSetting(int n, boolean b) {
		settings[n] = b;
		settingsBoxes[n].setState(settings[n]);
	}

	public void switchSetting(int n) {
		settings[n] = !settings[n];
		settingsBoxes[n].setState(settings[n]);
	}

	public void closeGameWriter() {
		gameWriter.close();
	}

	public void printSettings() throws IOException {
		PrintWriter settingsWriter = new PrintWriter("settings.txt");
		settingsWriter.println("vectors = "
				+ (settings[VectorsNum] ? "yes" : "no"));
		settingsWriter.println("resultant = "
				+ (settings[ResultantNum] ? "yes" : "no"));
		settingsWriter
				.println("trail = " + (settings[TrailNum] ? "yes" : "no"));
		settingsWriter.println("effects = "
				+ (settings[EffectsNum] ? "yes" : "no"));
		settingsWriter.println("warpArrows = "
				+ (settings[WarpArrowsNum] ? "yes" : "no"));

		int speed = 3;
		for (int i = 0; i < speedButtons.length; i++) {
			if (speedButtons[i].isSelected()) {
				speed = i;
				break;
			}
		}

		settingsWriter.println("speed = " + speed);
		settingsWriter.close();
	}

	private void saveGame(File file) throws IOException {
		PrintWriter saveWriter = new PrintWriter(file);
		for (int i = 0; i < levels.size(); i++) {
			saveWriter.print(DataWriter.encode(swingData[i], i) + " ");
		}
		saveWriter.print(DataWriter.encode(currentLevelN, levels.size()) + " ");
		saveWriter.print(DataWriter.encode(numSwingsTaken, levels.size() + 1));
		saveWriter.close();
		gameWriter.println("Game saved to " + file.getName() + ".");
	}

	private void loadGame(File file) throws IOException {

		int totalswingimport = 0;
		int level = 0;
		boolean cheatingDetected = false;
		Scanner infile = new Scanner(file);

		int[] swingImport = new int[0];

		try {

			swingImport = new int[levels.size()];
			int swingSum = 0;
			for (int i = 0; i < levels.size(); i++) {
				swingImport[i] = DataReader.decode((long) infile.nextInt(), i);
				if (swingImport[i] < 0) {
					cheatingDetected = true;
					break;
				}
				swingSum += swingImport[i];
			}
			level = DataReader.decode(infile.nextInt(), levels.size());
			if (level < 0 || level > levels.size()) {
				cheatingDetected = true;
			}
			totalswingimport = DataReader.decode(infile.nextInt(),
					levels.size() + 1);
			if (totalswingimport != swingSum) {
				cheatingDetected = true;
			}

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			gameWriter.println(file.getName() + " failed to load.");
			gameWriter.println(e);
		}

		if (!cheatingDetected) {
			currentLevelN = level;
			numSwingsTaken = totalswingimport;
			swingData = swingImport;
			setLevelUp(currentLevelN);
			gameStarted = true;
			gamePaused = false;
			gameWriter.println(file.getName() + " loaded successfully.");

		} else {
			JOptionPane.showMessageDialog(null,
					"Cheating detected; will not load file",
					"Cheating Detected", JOptionPane.ERROR_MESSAGE);
			gameWriter.println(file.getName()
					+ "failed to load - likely cheating.");
		}
		infile.close();
	}

	public void safeQuit() {
		try {
			printSettings();
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
