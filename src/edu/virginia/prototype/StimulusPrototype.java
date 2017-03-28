package edu.virginia.prototype;

import edu.virginia.engine.controller.GamePad;
import edu.virginia.engine.controller.GamePadComponent;
import edu.virginia.engine.display.*;
import edu.virginia.engine.events.*;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.sound.SoundManager;
import edu.virginia.engine.util.GameClock;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by BrandonSangston on 2/17/17.
 * TODO: Fix physics, tweening, stimulus duration/recharge, libpd, sound visualization,
 * TODO: optimization, sound "collision", side-scrolling, optimization
 */
public class StimulusPrototype extends Game {

    //Platform mechanics
    int speed = 12;
    int frameClock = 0;
    int timeRemaining = 60, prevTime = 0;
    int jmpCount = 15, jmp = 0;

    //Stimulus mechanics
    boolean shadow = false;
    int shadowRecharge = 10;
    int shadowClock = 0;
    int shadowDuration = 5;
    double start = 0;
    GameClock gameClock = new GameClock();


    QuestManager questManager = new QuestManager();
    DisplayObjectContainer GameWorld = new DisplayObjectContainer("GameWorld");

    AnimatedShadowSprite mario = new AnimatedShadowSprite("Mario", "bigmario_sprites.png",
            "shadow_mario_mag_b.png", 4, 2);
    AnimatedShadowSprite coin = new AnimatedShadowSprite("Gold", "coin.png",
            "shadow_coin_mag.png", 10, 1);

    DisplayObjectContainer Platforms = new DisplayObjectContainer("Platforms");
    ShadowSprite platform1 = new ShadowSprite("Platform1", "platform.png", "shadow_platform_mag_b.png");
    ShadowSprite platform2 = new ShadowSprite("Platform2", "platform.png", "shadow_platform_mag_b.png");
    ShadowSprite platform3 = new ShadowSprite("Platform3", "platform.png", "shadow_platform_mag_b.png");
    ShadowSprite platform4 = new ShadowSprite("Platform4", "platform.png", "shadow_platform_mag_b.png");
    ShadowSprite platform5 = new ShadowSprite("Platform5", "platform.png", "shadow_platform_mag_b.png");

    ShadowSprite background = new ShadowSprite("Background", "background1.png", "shadow_background1_mag.png");

    ArrayList<DisplayObject> children = getChildren();

    SoundManager soundManager = new SoundManager();


    public StimulusPrototype() {
        super("Stimulus Prototype v1.0", 1200, 800);

        background.setPosition(center);

        mario.addNewAnimation("idle", new int[] {0});
        mario.addNewAnimation("run", new int[] {1,2,3,4});
        mario.addNewAnimation("jump", new int[] {4});
        mario.centerPivot();
        mario.setPosition(mario.halfWidth(), 800 - mario.halfHeight());
        mario.setScale(0.5);
        mario.setAnim("idle");
        mario.playAnim();
        mario.setAnimSpeed(speed/2);

        //Physics
        mario.addRigidBody2D();

        platform1.setPosition(mario.getPosX(), mario.getPosY()+225);
        platform1.setScale(0.25);
        platform1.addRigidBody2D();
        platform1.getRigidBody().toggleGravity(false);
        platform1.setHitbox(platform1.getLocalHitbox().x, platform1.getLocalHitbox().y+platform1.getLocalHitbox().height/25,
                platform1.getLocalHitbox().width, platform1.getLocalHitbox().height - platform1.getLocalHitbox().height/25
                        - 300);

        platform2.setPosition(mario.getPosX()+300, mario.getPosY());
        platform2.setScale(0.2);
        platform2.addRigidBody2D();
        platform2.getRigidBody().toggleGravity(false);
        platform2.setHitbox(platform2.getLocalHitbox().x, platform2.getLocalHitbox().y+platform2.getLocalHitbox().height/25,
                platform2.getLocalHitbox().width, platform2.getLocalHitbox().height - platform2.getLocalHitbox().height/25
                        - 300);

        platform3.setPosition(platform2.getPosX() + 300, platform2.getPosY()+150);
        platform3.setScale(0.2);
        platform3.addRigidBody2D();
        platform3.getRigidBody().toggleGravity(false);
        platform3.setHitbox(platform3.getLocalHitbox().x, platform3.getLocalHitbox().y+platform3.getLocalHitbox().height/25,
                platform3.getLocalHitbox().width, platform3.getLocalHitbox().height - platform3.getLocalHitbox().height/25
                        - 300);

        platform4.setPosition(platform3.getPosX() + 300, platform3.getPosY() - 300);
        platform4.setScale(0.215);
        platform4.addRigidBody2D();
        platform4.getRigidBody().toggleGravity(false);
        platform4.setHitbox(platform4.getLocalHitbox().x, platform4.getLocalHitbox().y+platform4.getLocalHitbox().height/25,
                platform4.getLocalHitbox().width, platform4.getLocalHitbox().height - platform4.getLocalHitbox().height/25
                        - 300);

        platform5.setPosition(centerX, centerY-150);
        platform5.setScale(0.25);
        platform5.addRigidBody2D();
        platform5.getRigidBody().toggleGravity(false);
        platform5.setHitbox(platform5.getLocalHitbox().x, platform5.getLocalHitbox().y+platform5.getLocalHitbox().height/25,
                platform5.getLocalHitbox().width, platform5.getLocalHitbox().height - platform5.getLocalHitbox().height/25
                        - 300);

        coin.addNewAnimation("spin", new int[] {0,1,2,3,4,5,6,7,8,9});
        coin.centerPivot();
        coin.setPosition(platform5.getPosX(), platform5.getPosY() - 100);
        coin.setScale(0.5);
        coin.setAnim("spin");
        coin.playAnim();
        coin.setAnimSpeed(speed/2);


        addChild(background);
        addChild(coin);
        addChild(mario);
        addChild(Platforms);
        Platforms.addChild(platform1);
        Platforms.addChild(platform2);
        Platforms.addChild(platform3);
        Platforms.addChild(platform4);
        Platforms.addChild(platform5);

        Quest q1 = new Quest("Games are Fun", "Collect the coin");
        questManager.setActiveQuest(q1);

        coin.addEventListener(questManager, Event.COIN_PICKED_UP);
        mario.addEventListener(this, Event.COLLISION);

        soundManager.loadMusic("mario theme", "mario_theme.wav");
    }

    /**
     * Engine will automatically call this update method once per frame and pass to us
     * the set of keys (as keycode ints) that are currently being pressed down
     */
    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<GamePad> gamePads) {
        super.update(pressedKeys, gamePads);


        if (pressedKeys.size() > 0 || gamePads.size() > 0) {

            GamePad controller = gamePads.get(0);

            //Movement
            if ((pressedKeys.contains(KeyEvent.VK_RIGHT)
                    || controller.isButtonPressed(GamePad.DPAD_RIGHT)
                    || controller.getLeftStickXAxis() > 0.5)
                    && inBoundsRight(mario)) { //move right

                if (!mario.isFacingRight()) {
                    mario.flip();
                }

                mario.setPosX(mario.getPosX() + speed);
                //if (background != null) background.setPosX(background.getPosX() - 1);
            }
            if ((pressedKeys.contains(KeyEvent.VK_LEFT)
                    || controller.isButtonPressed(GamePad.DPAD_LEFT)
                    || controller.getLeftStickXAxis() < -0.5)
                    && inBoundsLeft(mario)) { //move left

                if (mario.isFacingRight()) {
                    mario.flip();
                }


                mario.setPosX(mario.getPosX() - speed);
                //if (background != null) background.setPosX(background.getPosX() + 1);

            }
            if (pressedKeys.contains(KeyEvent.VK_DOWN) && inBoundsBottom(mario)) { //move down
                mario.setPosY(mario.getPosY() + speed);

            }

            if (pressedKeys.contains(KeyEvent.VK_SPACE) || controller.isButtonPressed(GamePad.BUTTON_CROSS)) {
                if (mario != null && jmp <= jmpCount) {
                    mario.setAnim("jump");
                    mario.jump();
                    ++jmp;
                    if (frameClock >= 10) soundManager.loadSoundEffect("jump", "jump.wav");
                    frameClock = 0;
                    if (mario.getPosY() <= 0) {
                        mario.setPosY(mario.halfHeight());
                    }
                }
            }

            if (isMoving() && !mario.getCurrentAnim().equals("jump")) {
                mario.setAnim("run");

            }

            //Toggle shadows
            if (shadowClock >= shadowRecharge) {
				if (pressedKeys.contains(KeyEvent.VK_F) || controller.isButtonPressed(GamePad.BUTTON_SQUARE)) {
					toggleShadows();

                    if (shadow) {
                        start = gameClock.getElapsedTime();
                        //System.out.println(start);
                    }

				}
                shadowClock = 0;
            }

//            //TODO: Shadow duration
//            if (gameClock.getElapsedTime() - start >= shadowDuration && shadow) {
//                System.out.println(gameClock.getElapsedTime() - start);
//                toggleShadows();
//            }

        }

        //Idle animation
        if (mario != null && pressedKeys.isEmpty() && !isMoving()) {
            mario.setAnim("idle");
        }

        if (mario != null && coin != null && mario.onTriggerEnter(coin)) {
            if (coin.isVisible()) {
                soundManager.loadSoundEffect("coin", "coin.wav");
            }
            coin.dispatchEvent(new Event(Event.COIN_PICKED_UP, coin));
            questManager.completeQuest("Games are Fun");

        }


        //TODO: make more efficient -will get really slow with lots of objects
        if (mario != null) {
            for (DisplayObject platform : Platforms.getChildren()) {
                if (mario.collidesWith(platform)) {
                    mario.dispatchEvent(new Collision(Collision.GROUND, mario, platform));
                    if (mario.getCurrentAnim().equals("jump")) {
                        if (isMoving())
                            mario.setAnim("run");
                        else
                            mario.setAnim("idle");
                    }
                    jmp = 0;
                }
            }

            //Respawn
            if (mario.getPosY() >= 3000) {
                mario.setPosition(mario.halfWidth(), 800 - mario.halfHeight());
            }

        }

        //Exit game
        if (pressedKeys.contains(KeyEvent.VK_ESCAPE)) {
            exitGame();
            closeGame();
        }

        ++frameClock;
        ++shadowClock;

        mouseEvents.clear();
    }

    /**
     * Engine automatically invokes draw() every frame as well. If we want to make sure mario gets drawn to
     * the screen, we need to make sure to override this method and call mario's draw method.
     */
    @Override
    public void draw(Graphics g) {
        super.draw(g);

        Font f = new Font("GUI", Font.ITALIC, 20);

        if (shadow) {
            g.setColor(Color.white);
        } else {
            g.setColor(Color.black);
        }

        if (questManager != null && questManager.getActiveQuest() != null) {
            g.drawString("Current Quest: " + questManager.getActiveQuest().getId(), 25, 25);
            g.drawString("Objective: " + questManager.getActiveQuest().getObjective(), 25, 50);

            if (questManager.getActiveQuest().isComplete()) {
                g.setFont(f);
                g.drawString("\""+questManager.getActiveQuest().getId()+"\" Completed!", centerX-135, centerY);
            }
        }

    }

    private boolean inBounds(Sprite s) {
        return inBoundsRight(s) && inBoundsBottom(s) &&
                inBoundsLeft(s) && inBoundsTop(s);
    }

    private boolean inBoundsRight(Sprite s) {
        return s.getPosX() <= getMainFrame().getWidth() - s.getScaledWidth()/2;
    }

    private boolean inBoundsLeft(Sprite s) {
        return (!s.isFacingRight() && s.getPosX() >= s.getScaledWidth()/2) ||
                (s.isFacingRight() && s.getPosX() >= s.getScaledWidth()/2);
    }

    private boolean inBoundsTop(Sprite s) {
        return s.getPosY() >= s.getScaledHeight()/2;
    }

    private boolean inBoundsBottom(Sprite s) {
        return s.getPosY() <= getMainFrame().getHeight() - s.getScaledHeight()/2;
    }


    private boolean isMoving() {

        GamePad controller = controllers.get(0);

        return (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_LEFT)
                || pressedKeys.contains(KeyEvent.VK_UP) || pressedKeys.contains(KeyEvent.VK_DOWN)
                || controller.getLeftStickXAxis() > 0.5 || controller.getLeftStickXAxis() < -0.5
                || controller.getLeftStickYAxis() > 0.5 || controller.getLeftStickYAxis() < -0.5);
    }

    private void toggleShadows() {

        toggleChildren(this);

        if (getScenePanel().getBackground() != Color.BLACK) {
            getScenePanel().setBackground(Color.BLACK);
        } else {
            getScenePanel().setBackground(Color.WHITE);
        }

        shadow = !shadow;
    }


    private void toggleChildren(DisplayObjectContainer t) {
        if (hasChildren()) {
            for (DisplayObject d : t.getChildren()) {
                if (d instanceof ShadowSprite) {
                    ((ShadowSprite) d).toggleShadow(!((ShadowSprite) d).isShadow());
                } else if (d instanceof AnimatedShadowSprite) {
                    ((AnimatedShadowSprite) d).toggleShadow(!((AnimatedShadowSprite) d).isShadow());
                } else if (d instanceof DisplayObjectContainer) {
                    toggleChildren((DisplayObjectContainer)d);
                }
            }
        }
    }

    /**
     * Quick main class that simply creates an instance of our game and starts the timer
     * that calls update() and draw() every frame
     */
    public static void main(String[] args) {
        StimulusPrototype game = new StimulusPrototype();
        game.start();

    }

}
