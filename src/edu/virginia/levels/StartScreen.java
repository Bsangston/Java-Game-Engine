package edu.virginia.levels;

import edu.virginia.engine.controller.GamePad;
import edu.virginia.engine.display.*;
import edu.virginia.engine.events.*;
import edu.virginia.engine.events.Event;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by BrandonSangston on 2/17/17.
 * Matthew Leon
 * Cole Schafer
 */
public class StartScreen extends DisplayObjectContainer {

    Game g;
    int currentLevel;
    boolean gameWon = false;

    //SHARED VARIABLES
    boolean startScreen = true;

    int speed = 4;
    int jmpHeight = 10;
    int jmp = 0;
    boolean jumping = false;
    boolean jumpReady;
    boolean landed;

    //Animated Sprites
    AnimatedSprite mario = new AnimatedSprite("Mario", "bigmario_sprites.png", 4, 2);
    AnimatedSprite coin1 = new AnimatedSprite("Coin1", "coin.png", 10, 1);

    DisplayObjectContainer platforms = new DisplayObjectContainer("Platforms");
    Sprite platform1 = new Sprite("Platform1", "basic_platform.png");
    Sprite platform2 = new Sprite("Platform2", "basic_platform.png");

    Font f1 = new Font("GUI", Font.BOLD, 55);
    Font f2 = new Font("GUI", Font.BOLD, 20);

    public StartScreen(Game wrapper, int currentLevel) {
        super("Startscreen");

        g = wrapper;
        this.currentLevel = currentLevel;

        mario.addEventListener(this, Event.COLLISION);

        //Initialize player parameters
        mario.addNewAnimation(AnimatedSprite.IDLE, new int[] {0});
        mario.addNewAnimation(AnimatedSprite.RUN, new int[] {1,2,3,4});
        mario.addNewAnimation(AnimatedSprite.JUMP, new int[] {4});
        mario.centerPivot();
        mario.setScale(0.2);
        mario.setPosition(700, 0);
        mario.setAnim(AnimatedSprite.IDLE);
        mario.playAnim();
        mario.setAnimSpeed(speed);
        mario.addRigidBody2D();

        platform1.setPosition(700, 300);
        platform2.setPosition(700, 600);

        coin1.addNewAnimation("spin", new int[] {0,1,2,3,4,5,6,7,8,9});
        coin1.centerPivot();
        coin1.setPosition(platform2.getPosX(), platform2.getPosY() - 50);
        coin1.setScale(0.25);
        coin1.setAnim("spin");
        coin1.playAnim();
        coin1.setAnimSpeed(6);

        addChild(mario);
        addChild(platforms);
        addChild(coin1);
        platforms.addChild(platform1);
        platforms.addChild(platform2);

        for (DisplayObject platform : platforms.getChildren()) {
            platform.setScale(0.175f); //original 0.135
            platform.addRigidBody2D();
            platform.getRigidBody().toggleGravity(false);
        }


        platform2.setScaleX(.5);
        mario.addEventListener(this, edu.virginia.engine.events.Event.COLLISION);
    }

    /**
     * Engine will automatically call this update method once per frame and pass to us
     * the set of keys (as keycode ints) that are currently being pressed down
     */
    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<GamePad> gamePads) {

            super.update(pressedKeys, gamePads);

            //Keyboard input
            if (!pressedKeys.isEmpty()) {

                //Movement
                if ((pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_D)) && inBoundsRight(mario)) { //move right

                    mario.moveRight(speed);
                    //parallaxScrolling(1, mario);
                }
                if ((pressedKeys.contains(KeyEvent.VK_LEFT) || pressedKeys.contains(KeyEvent.VK_A)) && inBoundsLeft(mario)) { //move left

                    mario.moveLeft(speed);
                    //parallaxScrolling(1, mario);

                }

                if (pressedKeys.contains(KeyEvent.VK_SPACE)) {
                    if (mario != null && jmp <= jmpHeight && !jumping && jumpReady) {

                        landed = false;
                        if(jmp == jmpHeight-1)
                            jumpReady = false;

                        mario.jump();
                        ++jmp;

                        if (jmp == jmpHeight) {
                            jumping = true;
                        }
                    }
                }

                if (isMoving() && !mario.getCurrentAnim().equals(AnimatedSprite.JUMP)) {
                    mario.setAnim(AnimatedSprite.RUN);

                }


            }

            //Controller input
            if (!gamePads.isEmpty()) {
                GamePad controller = gamePads.get(0);

                //Movement
                if (controller.isButtonPressed(GamePad.DPAD_RIGHT) || controller.getLeftStickXAxis() > 0.5
                        && inBoundsRight(mario)) { //move right

                    mario.moveRight(speed);
                }
                if (controller.isButtonPressed(GamePad.DPAD_LEFT) || controller.getLeftStickXAxis() < -0.5
                        && inBoundsLeft(mario)) { //move left

                    mario.moveLeft(speed);

                }

                if (controller.isButtonPressed(GamePad.BUTTON_CROSS)) {
                    if (mario != null && jmp <= jmpHeight && !jumping && jumpReady) {

                        landed = false;
                        if(jmp == jmpHeight-1)
                            jumpReady = false;

                        mario.jump();
                        ++jmp;

                        if (jmp == jmpHeight) {
                            jumping = true;
                        }
                    }
                }

//                if (isMoving() && !mario.getCurrentAnim().equals(AnimatedSprite.JUMP)) {
//                    mario.setAnim(AnimatedSprite.RUN);
//
//                }
            }

            //TODO: make more efficient -will get really slow with lots of objects (implement collision grid?)
            if (mario != null) {
                for (DisplayObject platform : platforms.getChildren()) {
                    if (mario.collidesWith(platform)) {

                        mario.dispatchEvent(new Collision(Collision.GROUND, mario, platform));


                        if (mario.getCurrentAnim().equals(AnimatedSprite.JUMP)) {
                            if (isMoving())
                                mario.setAnim(AnimatedSprite.RUN);
                            else
                                mario.setAnim(AnimatedSprite.IDLE);
                        }

                        if (mario.getPosY() < platform.getPosY()) {
                            jmp = 0;
                            jumping = false;
                            landed = true;
                        }

                    }
                }

                //Respawn
                if (mario.getPosY() >= 1500) {
                    mario.setPosition(700, 0);
                }
            }

            //Idle animation
            if (mario != null && pressedKeys.isEmpty() && !isMoving()) {
                mario.setAnim(AnimatedSprite.IDLE);
            }

            if(landed && !jumpReady && !pressedKeys.contains(KeyEvent.VK_SPACE) ||
                    (g.controllers.size() >= 1 && !g.controllers.get(0).isButtonPressed(GamePad.BUTTON_CROSS))){
                jumpReady = true;
            }

            //Exit game
            if (pressedKeys.contains(KeyEvent.VK_ESCAPE)) {
                g.exitGame();
                g.closeGame();
            }

            if(mario.collidesWith(coin1)){
                mario.setPosition(g.centerX, 0);
                this.dispatchEvent(new Event("start_level_" + currentLevel, this));
            }

            if(gameWon){
                coin1.setVisible(false);
                platform2.setVisible(false);
            }

            g.mouseEvents.clear();
    }

    /**
     * Engine automatically invokes draw() every frame as well. If we want to make sure mario gets drawn to
     * the screen, we need to make sure to override this method and call mario's draw method.
     */
    @Override
    public void draw(Graphics g) {

         super.draw(g);

        g.setFont(f1);
        g.drawString("Stimulus", 580, 100);

        g.setFont(f2);
        g.drawString("Level " + currentLevel, coin1.getPosX() - 40, coin1.getPosY() - 25);

        if(gameWon){
            g.setFont(f1);
            g.drawString("Congrats, you've beaten the game!", 225, 425);
        }

    }

    private boolean isMoving() {

        boolean controllerInput = false;
        if (g.controllers != null && !g.controllers.isEmpty()) {
            GamePad controller = g.controllers.get(0);
            controllerInput = controller.getLeftStickXAxis() > 0.5 ||
                    controller.getLeftStickXAxis() < -0.5 ||
                    controller.getLeftStickYAxis() > 0.5 ||
                    controller.getLeftStickYAxis() < -0.5;
        }

        return g.pressedKeys.contains(KeyEvent.VK_RIGHT) || g.pressedKeys.contains(KeyEvent.VK_LEFT)
                || g.pressedKeys.contains(KeyEvent.VK_UP) || g.pressedKeys.contains(KeyEvent.VK_DOWN)
                || g.pressedKeys.contains(KeyEvent.VK_D) || g.pressedKeys.contains(KeyEvent.VK_A)
                || g.pressedKeys.contains(KeyEvent.VK_W) || g.pressedKeys.contains(KeyEvent.VK_S) || controllerInput;

    }

    private boolean inBoundsRight(Sprite s) {
        return s.getPosX() <= g.getMainFrame().getWidth() - s.getScaledWidth()/2;
    }

    private boolean inBoundsLeft(Sprite s) {
        return (!s.isFacingRight() && s.getPosX() >= s.getScaledWidth()/2) ||
                (s.isFacingRight() && s.getPosX() >= s.getScaledWidth()/2);
    }

    private boolean inBoundsTop(Sprite s) {
        return s.getPosY() >= s.getScaledHeight()/2;
    }

    private boolean inBoundsBottom(Sprite s) {
        return s.getPosY() <= g.getMainFrame().getHeight() - s.getScaledHeight()/2;
    }


    /**
     * Quick main class that simply creates an instance of our game and starts the timer
     * that calls update() and draw() every frame
     */
    /*public static void main(String[] args) {
        StartScreen game = new StartScreen();
        game.start();
    }*/

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

}
