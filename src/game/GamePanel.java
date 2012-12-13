package game;

import structures.Ball;
import structures.Point2d;

import graphics.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
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

	// Game Components
	public static final int MaxInitialMagnitude = 300;
	public static final int ArrowLength = 200; // affects how large drawn
												// vectors are relative to those

	public static final int EffectsNum = 0;
	public static final int VectorsNum = 1;
	public static final int ResultantNum = 2;
	public static final int TrailNum = 3;
	public static final int WarpArrowsNum = 4;
	public static final int[] DEFAULT_SETTINGS = { 1, 0, 0, 1, 0, 3 };
	// User-set settings
	private boolean settings[] = new boolean[5];

	private boolean blinkingBall = true;
	// Game data
	private double launchAngle, launchMagnitude;
	private Point2d initialPoint, terminalPoint; // for the initial velocity
	boolean drawingInitialVelocity, gameStarted, gamePaused, gameWon;

	private GameManager gameManager;
	// Current Level data storage
	boolean errorFound;
	private Level currentLevel;
	private Ball ball;
	private boolean levelComplete;
	private double screenXShift, screenYShift;

	// Graphics Components
	// Main components
	int speed = 2;
	private Thread animator;
	private volatile boolean running;
	private int paints;
	// Special effect values
	static final int TimeBetweenFlashes = 250; // ms

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

	private static final String SAVE_EXTENSION = "sav";
	JMenu controlMenu = new JMenu("Control");
	JMenuItem pauseItem = new JMenuItem("Pause game");
	JMenuItem resetLevelItem = new JMenuItem("Reset level");
	JMenu saveMenu = new JMenu("Save");
	JMenuItem saveItem = new JMenuItem("Save current game");
	JMenu loadMenu = new JMenu("Load");
	JMenuItem loadItem = new JMenuItem("Load game");

	public GamePanel() {
		gameManager = new GameManager();
		int[] importedSettings = DataHandler.getSettings();
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
					if (e.getSource() == settingsBoxes[TrailNum]) {
						TrailEffect.resetPoints();
					}
					for (int i = 0; i < settingsBoxes.length; i++) {
						settings[i] = settingsBoxes[i].getState();
					}
				}
			});
			settingsBoxes[i].setState(settings[i]);
		}
		settingsBoxes[VectorsNum].setMnemonic(KeyEvent.VK_V);
		settingsBoxes[ResultantNum].setMnemonic(KeyEvent.VK_D);
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
				TrailEffect.addTrailPoint(new Point2d(ball.getCenter().x, ball
						.getCenter().y));
			}
			if (currentLevel.timeToReset() && settings[EffectsNum]) {

				if (!CollisionEffect.started()) {
					CollisionEffect.start(currentLevel);
				}
				if (!CollisionEffect.running()) {
					CollisionEffect.kill();
					resetLevel();
				}

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
			} else if (CollisionEffect.running()) {
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
			if (settings[VectorsNum] && !CollisionEffect.running()) {
				GravityVectorsEffect.draw(currentLevel, g);
			}
			if (settings[ResultantNum] && !CollisionEffect.running()) {
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
		CollisionEffect.kill();
		TrailEffect.resetPoints();
		currentLevel.reset();
		drawingInitialVelocity = false;
	}

	private void drawLevel(Graphics2D g) {

		long t = System.currentTimeMillis();
		ball = currentLevel.getBall();
		int xShift = (int) Math.round(screenXShift);
		int yShift = (int) Math.round(screenYShift);

		if (settings[EffectsNum] && CollisionEffect.running()) {
			int[] newShifts = CollisionEffect.update();
			screenXShift = newShifts[0];
			screenYShift = newShifts[1];
		} else {
			if ((CollisionEffect.running() || !(settings[EffectsNum])
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
			TrailEffect.draw(currentLevel, g);
		}
	
		if (!CollisionEffect.running()) { // prevents flicker of ball on reset
											// after
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

		// TODO: needs adjustment with collision positioning
		if (gameManager.getLevelNumber() == 1 && gameStarted
				&& gameManager.getCurrentLevelSwings() == 0) {
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

		if (settings[EffectsNum] && CollisionEffect.running()) {
			CollisionEffect.draw(g);
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
			chooser.setFileFilter(new FileNameExtensionFilter("."
					+ SAVE_EXTENSION + " files", SAVE_EXTENSION));
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				chooser.getSelectedFile().getName();
			}
			try {
				String name = chooser.getSelectedFile().getName();
				if (name.substring(name.length() - 4, name.length()).equals(
						"." + SAVE_EXTENSION)) {
					gameManager.save("saves/" + name);
				} else {
					gameManager.save("saves/" + name + "." + SAVE_EXTENSION);
				}
			} catch (Exception e) {
			}

		} else if (event.getSource() == loadItem) {
			CollisionEffect.kill();
			JFileChooser chooser = new JFileChooser(
					System.getProperty("user.dir") + "/saves");
			chooser.setApproveButtonText("Load");
			chooser.setDialogTitle("Load");
			chooser.setFileFilter(new FileNameExtensionFilter("."
					+ SAVE_EXTENSION + " files", SAVE_EXTENSION));
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				chooser.getSelectedFile().getName();
			}
			try {
				gameStarted = true;
				gamePaused = false;
				gameManager = GameManager.loadSave(chooser.getSelectedFile());
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
				&& !CollisionEffect.running()) {
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
		TrailEffect.resetPoints();
		blinkingBall = false; // the first launch will disable all blinking
								// (reset upon getting to next level)
		if (!gamePaused && !ball.isLaunched() && !CollisionEffect.running()
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

			double xLength = Math.cos(launchAngle) * launchMagnitude;
			double yLength = -Math.sin(launchAngle) * launchMagnitude;
			ball.setVelocity(new Vector2d(xLength / 200, yLength / 200));
			ball.setLaunched(true);
		}

		if (levelComplete) {
			levelComplete = false;
			blinkingBall = true;
			boolean moreLevels = gameManager.nextLevel();
			TrailEffect.resetPoints();
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

	public boolean isPaused() {
		return gamePaused;
	}

	public void switchSetting(int index) {
		settings[index] = !settings[index];
		settingsBoxes[index].setSelected(settings[index]);
		if (index == TrailNum)
			TrailEffect.resetPoints();
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
			DataHandler.printSettings(settings, speed);
		} catch (IOException e) {
		}
		running = false;
	}

	public void mouseClicked(MouseEvent event) {
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}

	public void mouseMoved(MouseEvent event) {
	}

	public void pause() {
		if (!CollisionEffect.running()) {
			gamePaused = !gamePaused;
		}
	}

}
