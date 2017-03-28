package edu.virginia.engine.display;

import edu.virginia.engine.events.*;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.util.GameClock;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;

import edu.virginia.engine.controller.GamePad;
import net.java.games.input.*;


/**
 * Highest level class for creating a game in Java.
 * 
 * */
public class Game extends DisplayObjectContainer implements ActionListener, KeyListener, IEventListener {

	/* Frames per second this game runs at */
	private int FRAMES_PER_SEC = 60;

	/* The main JFrame that holds this game */
	private JFrame mainFrame;

	/* Timer that this game runs on */
	private Timer gameTimer;
	private GameClock gameClock;
	private long timePassed = 0;
	private long curTime = 0;
	private long lastTime = 0;
	private double timeFraction = 0.0;
	
	/* The JPanel for this game */
	private GameScenePanel scenePanel;

	/* Controller support */
	protected ArrayList<GamePad> controllers;

	/* Mouse functionality */
	protected ArrayList<MouseEvent> mouseEvents = new ArrayList<MouseEvent>();
	protected double mouse_x, mouse_y;

	/* Center point of game scene for convenience */
	protected Vector2D center;
	protected int centerX;
	protected int centerY;

	/* Physics stuff */
	public static double GRAVITY = 1.1;
	public static double DRAG = 0.2;
	public static double STICKY_THRESHOLD = 0.0004;

	public Game(String gameId, int width, int height) {
		super(gameId);
		
		setUpMainFrame(gameId, width, height);
		
		setScenePanel(new GameScenePanel(this));
		
		/* Use an absolute layout */
		scenePanel.setLayout(null);

		center = new Vector2D(getMainFrame().getWidth()/2, getMainFrame().getHeight()/2);
		centerX = (int)center.getX();
		centerY = (int)center.getY();

		gameClock = new GameClock();

		setUpControllers();

	}
	
	
	public void setFramesPerSecond(int fps){
		if(fps > 0) this.FRAMES_PER_SEC = fps;
	}

	public void setUpMainFrame(String gameId, int width, int height) {
		this.mainFrame = new JFrame();
		getMainFrame().setTitle(gameId);
		getMainFrame().setResizable(false);
		getMainFrame().setVisible(true);
		getMainFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getMainFrame().setBounds(0, 0, width, height);
		getMainFrame().addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		getMainFrame().addKeyListener(this);
		getMainFrame().addMouseListener(new MouseAdapter() {


			@Override
			public void mousePressed(MouseEvent e) {
				if (!mouseEvents.contains(e)) {
					mouseEvents.add(e);
					mouse_x = e.getX();
					mouse_y = e.getY();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (mouseEvents.contains(e)) {
					mouseEvents.remove(e);
				}
			}
		});
	}


	private void setUpControllers() {
		controllers = new ArrayList<GamePad>();
		ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
		Controller[] cs = ce.getControllers();
		for (int i = 0; i < cs.length; i++) {
			Controller controller = cs[i];
			if (
					controller.getType() == Controller.Type.STICK ||
							controller.getType() == Controller.Type.GAMEPAD ||
							controller.getType() == Controller.Type.WHEEL ||
							controller.getType() == Controller.Type.FINGERSTICK
					)
			{
				System.out.println("Found Controller: " + controller.getName() + ", " + controller.getType() );
				controllers.add(new GamePad(controller));
			}
		}
	}

	/**
	 * Starts the game
	 */
	public void start() {
		if (gameTimer == null) {
			gameTimer = new Timer(1000 / FRAMES_PER_SEC, this);
			gameTimer.start();
			gameClock.resetGameClock();
		} else {
			gameTimer.start();
			gameClock.resetGameClock();
		}
	}

	/**
	 * Stops the animation.
	 */
	public void stop() {
		pause();
		gameTimer = null;
	}

	public void pause() {
		if (gameTimer != null) {
			gameTimer.stop();
		}
	}
	
	public void exitGame(){
		stop();
		this.mainFrame.setVisible(false);
		this.mainFrame.dispose();
	}
	
	/**
	 * Close the window
	 * */
	public void closeGame(){
		this.stop();
		if(this.getMainFrame() != null){
			this.getMainFrame().setVisible(false);
			this.getMainFrame().dispose();
		}
	}


	/**
	 * Called once per frame. updates the game, redraws the screen, etc. May
	 * need to optimize this if games get too slow.
	 * */
	@Override
	public void actionPerformed(ActionEvent e) {
		repaintGame();
	}
	
	/**
	 * Forces a repaint
	 * */
	public void repaint(){repaintGame();}
	public void repaintGame(){
		if(getScenePanel() != null){
			getScenePanel().validate();
			getScenePanel().repaint();
		}
	}

	protected void nextFrame(Graphics g) {

		try {
			/* Update all objects on the stage */
			pollControllers();
			this.update(pressedKeys, controllers);
			//this.physicsUpdate();
			/* Draw everything on the screen */
			this.draw(g);
		} catch (Exception e) {
			System.out
					.println("Exception in nextFrame of game. Stopping game (no frames will be drawn anymore");
			stop();
			e.printStackTrace();
		}
	}

	/**
	 * Searches all known controllers (ps3, etc.) and adds any pressed buttons to pressed keys
	 * */
	private void pollControllers(){
		for(GamePad controller : controllers){
			controller.update();
		}
	}

	@Override
	public void draw(Graphics g){
		/* Start with no transparency */
		((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));
		
		super.draw(g);
	}


	public JFrame getMainFrame() {
		return this.mainFrame;
	}
	
	public void setScenePanel(GameScenePanel scenePanel) {
		this.scenePanel = scenePanel;
		this.getMainFrame().add(this.scenePanel);
		getMainFrame().setFocusable(true);
		getMainFrame().requestFocusInWindow();
	}

	public GameScenePanel getScenePanel() {
		return scenePanel;
	}

	protected ArrayList<Integer> pressedKeys = new ArrayList<>();

	@Override
	public void keyPressed(KeyEvent e) {
		if(!pressedKeys.contains(e.getKeyCode()))
			pressedKeys.add(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(pressedKeys.contains(e.getKeyCode()))
			pressedKeys.remove((Integer)e.getKeyCode());
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	//TODO: expand collision resolution capabilities
	public void resolveCollision(DisplayObject s, DisplayObject other) {
		double dx = (s.getPosX() - other.getPosX()) / (double)other.halfWidth();
		double dy = (s.getPosY() - other.getPosY()) / (double)other.halfHeight();
		double absDX = Math.abs(dx);
		double absDY = Math.abs(dy);

		if (Math.abs(absDX - absDY) < 0.1) {
			if (dx < 0) {
				s.setPosX(other.getLeft() - s.halfWidth());
			} else {
				s.setPosX(other.getRight() + s.halfWidth());
			}

			if (dy < 0) {
				s.setPosY(other.getTop() - s.halfHeight());
			} else {
				s.setPosY(other.getBottom() + s.halfHeight());
			}

		}
		else if (absDY > absDX) {
			if (dy < 0) {
				s.setPosY(other.getTop() - s.halfHeight());
			} else {
				s.setPosY(other.getBottom() + s.halfHeight());
			}

		}
		else {
			if (dx < 0) {
				s.setPosX(other.getLeft() - s.halfWidth());
			} else {
				s.setPosX(other.getRight() + s.halfWidth());
			}
		}

	}

	public boolean detectCollisions(DisplayObject collider, DisplayObject collidee) {
		return collider.collidesWith(collidee);
	}

	@Override
	public void handleEvent(Event event) {
		if (event instanceof Collision) {
			Collision collision = (Collision)event;
			resolveCollision((DisplayObject)collision.getSource(), collision.getCollidee());
		}
	}

	private void physicsUpdate() {
		curTime = System.currentTimeMillis();
		updateTime();
		constantForces();
		sumForces();
		moveObjects();
	}

	private void sumForces() {
		for (DisplayObject child : children) {
			if (child.hasRigidBody()) {
				double vx = child.rb2d.velocity.x + (child.rb2d.acceleration.x * timeFraction);
				double vy = child.rb2d.velocity.y + (child.rb2d.acceleration.y * timeFraction);

				child.rb2d.updateVelocity(new Vector2D(vx, vy));

				child.rb2d.applyDrag(1.0 - (timeFraction * Game.DRAG));
			}

		}
	}

	private void constantForces() {
		for (DisplayObject child : children) {
			if (child.hasRigidBody()) {
				child.rb2d.applyConstantForces();
			}
		}
	}

	private void moveObjects() {
		for (DisplayObject child : children) {
			if (child.hasRigidBody()) {
				double prevX = child.getPosX();
				double prevY = child.getPosY();

				double newX = prevX + (child.rb2d.velocity.x * timeFraction);
				double newY = prevY + (child.rb2d.velocity.y * timeFraction);
				child.updatePosition(new Vector2D(newX, newY));
			}
		}
	}

	private void updateTime() {
		lastTime = curTime;
		curTime = System.currentTimeMillis();
		timePassed = (curTime - lastTime);
		timeFraction = (timePassed / 1000.0);
	}
}
