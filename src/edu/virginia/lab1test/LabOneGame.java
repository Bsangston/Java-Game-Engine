/*
 Brandon Sangston bss3cv
 CS 4730 Lab 1
 1/27/2017
 */

package edu.virginia.lab1test;

import edu.virginia.engine.display.AnimatedSprite;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.Sprite;
import edu.virginia.engine.display.Vector2D;
import edu.virginia.engine.util.GameClock;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Example game that utilizes our engine. We can create a simple prototype game with just a couple lines of code
 * although, for now, it won't be a very fun game :)
 * */
public class LabOneGame extends Game {

	int health = 25;
	int timeRemaining = 60, prevTime = 0;
	int speed = 12;
	int frameClock = 0;
	int flipped = 1;
	boolean player1Win = false, player2Win = false;
	GameClock gameClock = new GameClock();

	Vector2D center = new Vector2D(getMainFrame().getWidth()/2, getMainFrame().getHeight()/2);
	int centerX = (int)center.getX();
	int centerY = (int)center.getY();

	//AnimatedSprite implementation
	AnimatedSprite mario = new AnimatedSprite("Mario", "bigmario_sprites.png", 4, 2);

	/**
	 * Constructor. See constructor in Game.java for details on the parameters given
	 */
	public LabOneGame() {
		super("Lab One Test Game", 1000, 600);

		mario.addNewAnimation("idle", new int[] {0});
		mario.addNewAnimation("run", new int[] {1,2,3,4});
		mario.centerPivot();
		mario.setPosition(center);
		mario.setScaleX(0.5);
		mario.setScaleY(0.5);
		mario.setAnim("idle");
		mario.playAnim();
		mario.setAnimSpeed(speed/2);
	}

	/**
	 * Engine will automatically call this update method once per frame and pass to us
	 * the set of keys (as strings) that are currently being pressed down
	 */
	@Override
	public void update(ArrayList<Integer> pressedKeys) {
		super.update(pressedKeys);

		/* Make sure mario is not null. Sometimes Swing can auto cause an extra frame to go before everything is initialized */
		if (mario != null) {
			mario.update(pressedKeys);


		}
		if (pressedKeys.size() > 0) {

			//Movement TODO: get rid of hard-coded values, and fix rotation hitboxes
			if ((pressedKeys.contains(KeyEvent.VK_RIGHT)) && mario.getPosX() <= getMainFrame().getWidth()-mario.getScaledWidth()) { //move right
				if (!mario.isFacingRight()) {
					mario.flip();
					setFacingRight(true);
					flipped = 1;
				}
				mario.setPosX(mario.getPosX() + speed);
			}
			if ((pressedKeys.contains(KeyEvent.VK_LEFT)) && mario.getPosX() >= 100) { //move left
				if (mario.isFacingRight()) {
					mario.flip();
					setFacingRight(false);
					flipped = -1;
				}
				mario.setPosX(mario.getPosX() - speed);
			}
			if ((pressedKeys.contains(KeyEvent.VK_UP)) && mario.getPosY() >= 0) { //move up
				mario.setPosY(mario.getPosY() - speed);
			}
			if ((pressedKeys.contains(KeyEvent.VK_DOWN)) && mario.getPosY() <= 400) { //move down
				mario.setPosY(mario.getPosY() + speed);

			}

			//Toggle run animation TODO: fix animation frame cycling
			if (isMoving()) {
				mario.setAnim("run");
				mario.increaseAlpha(0.05f);

			}

			//Scale
			if (pressedKeys.contains(KeyEvent.VK_A)) { //scale up
				if (mario.getScaleX() < 1.5 && mario.getScaleY() < 1.25) {
					mario.setScaleX(mario.getScaleX() * 1.01);
					mario.setScaleY(mario.getScaleY() * 1.01);
				}
			}
			if (pressedKeys.contains(KeyEvent.VK_S)) { //scale down

				if (mario.getScaleX() > 0.25 && mario.getScaleY() > 0.25) {
					mario.setScaleX(mario.getScaleX() * 0.99);
					mario.setScaleY(mario.getScaleY() * 0.99);
				}
			}

			//Rotation
			if (pressedKeys.contains(KeyEvent.VK_Q)) { //rotate clockwise
				mario.setRotation(mario.getRotation() + 5);
			}
			if (pressedKeys.contains(KeyEvent.VK_W)) { //rotate counterclockwise
				mario.setRotation(mario.getRotation() - 5);
			}

			//Alpha
			if (pressedKeys.contains(KeyEvent.VK_Z)) { //increase alpha
				mario.increaseAlpha(0.05f);
			}
			if (pressedKeys.contains(KeyEvent.VK_X)) { //decrease alpha
				mario.decreaseAlpha(0.05f);
			}

			//Visibility -disabled for mechanics
			if (frameClock >= 10) {
//				if (pressedKeys.contains(KeyEvent.VK_V)) {
//					mario.setVisible(!mario.isVisible());
//				}
				frameClock = 0;
			}

		}

		//Idle animation
		if (mario != null && pressedKeys.isEmpty()) {
			mario.setAnim("idle");
		}

		//Detect mouse clicks
		if (mouseEvents != null && mouseEvents.size() > 0) {
			if (mouseEvents.get(0).getButton() == MouseEvent.BUTTON1) {
				//Crude hitbox
				if (mario.wasClicked(mouse_x, mouse_y)) {
					health--;
					if (health == 0) {
						player2Win = true;
						stop();
					}
				}
			}
		}

		//Pivot point
		if (pressedKeys.contains(KeyEvent.VK_L)) { //move pivot right
			mario.setPivotX(mario.getPivotX() + speed/2);
			//mario.setPosX(mario.getPosX() - speed);
		}
		if (pressedKeys.contains(KeyEvent.VK_J)) { //move pivot left
			mario.setPivotX(mario.getPivotX() - speed/2);
			//mario.setPosX(mario.getPosX() + speed);
		}
		if (pressedKeys.contains(KeyEvent.VK_I)) { //move pivot up
			mario.setPivotY(mario.getPivotY() - speed/2);
			//mario.setPosY(mario.getPosY() + speed);
		}
		if (pressedKeys.contains(KeyEvent.VK_K)) { //move pivot down
			mario.setPivotY(mario.getPivotY() + speed/2);
			//mario.setPosY(mario.getPosY() - speed);
		}

		//Exit game
		if (pressedKeys.contains(KeyEvent.VK_ESCAPE)) {
			exitGame();
			closeGame();
		}


		//Timer
		if ((int) gameClock.getElapsedTime() / 1000 > prevTime) {
			timeRemaining--;
			if (timeRemaining < 0) {
				timeRemaining = 0;
				player1Win = true;
				stop();
			}

		}

		prevTime = (int) gameClock.getElapsedTime() / 1000;
		mouseEvents.clear();
		++frameClock;
	}

	/**
	 * Engine automatically invokes draw() every frame as well. If we want to make sure mario gets drawn to
	 * the screen, we need to make sure to override this method and call mario's draw method.
	 */
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		g.drawString("Health: " + health, 400, 25);
		g.drawString("Time Remaining " + timeRemaining + "s", 500, 25);

		/* Same, just check for null in case a frame gets thrown in before Mario is initialized */
		if (mario != null) {
			mario.draw(g);
		}

		//Win text TODO: fix win text (not showing)
		if (player1Win) {
			g.drawString("Player 1 Wins!!!", 460, 200);
		}
		if (player2Win) {
			g.drawString("Player 2 Wins!!!", 460, 200);
		}


	}


	private boolean outOfBounds(Sprite s) {
		if (s.getPosX() > getMainFrame().getWidth() - s.getScaledWidth() || s.getPosX() < getMainFrame().getX()
				|| s.getPosY() > getMainFrame().getHeight() || s.getPosY() < getMainFrame().getY()) {
			return true;
		}
		return false;
	}

	private boolean isMoving() {
		if (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_LEFT)
				|| pressedKeys.contains(KeyEvent.VK_UP) || pressedKeys.contains(KeyEvent.VK_DOWN)) {
			return true;
		}
		return false;
	}

	/**
	 * Quick main class that simply creates an instance of our game and starts the timer
	 * that calls update() and draw() every frame
	 */
	public static void main(String[] args) {
		LabOneGame game = new LabOneGame();
		game.start();

	}


}
