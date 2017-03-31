package edu.virginia.prototype;

import edu.virginia.engine.cole_tween.TweenJuggler;
import edu.virginia.engine.controller.GamePad;
import edu.virginia.engine.display.*;
import edu.virginia.engine.events.*;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.puredata.core.*;
import edu.virginia.engine.puredata.core.PdReceiver;
import edu.virginia.engine.puredata.core.utils.PdDispatcher;
import edu.virginia.engine.sound.JavaSoundThread;
import edu.virginia.engine.sound.SoundManager;
import edu.virginia.engine.util.GameClock;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;


import org.puredata.core.*;
import org.puredata.core.PdBase;

/**
 * Created by BrandonSangston on 2/17/17.
 * TODO: Fix physics, tweening, stimulus duration/recharge, libpd, sound visualization,
 * TODO: optimization, sound "collision", side-scrolling, optimization
 */
public class StimulusPrototype extends Game {

    //Platform mechanics parameters
    int speed = 12;
    int frameClock = 0;
    int timeRemaining = 60, prevTime = 0;
    int jmpHeight = 15, jmp = 0;
    boolean jumping = false;

    //Stimulus mechanics parameters
    boolean shadow = false;
    int shadowRecharge = 180; //frames
    int shadowClock = 180; //frames
    int shadowDuration = 5000; //ms
    double start = 0;
    GameClock gameClock = new GameClock();

    //Util
    ArrayList<Integer> lastKeyPressed = new ArrayList<>();

    QuestManager questManager = new QuestManager();
    DisplayObjectContainer GameWorld = new DisplayObjectContainer("GameWorld");

    //Animated Sprites
    AnimatedShadowSprite mario = new AnimatedShadowSprite("Mario", "bigmario_sprites.png",
            "shadow_mario_mag_b.png", 4, 2);
    AnimatedShadowSprite coin = new AnimatedShadowSprite("Coin", "coin.png",
            "shadow_coin_mag.png", 10, 1);
    AnimatedShadowSoundSprite enemy = new AnimatedShadowSoundSprite("Ghost", "shadow_boo.png",
            "shadow_boo.png", 3, 2);

    //Sprites
    DisplayObjectContainer Platforms = new DisplayObjectContainer("Platforms");
    ShadowSprite platform1 = new ShadowSprite("Platform1", "platform.png", "shadow_platform_mag_b.png");
    ShadowSprite platform2 = new ShadowSprite("Platform2", "platform.png", "shadow_platform_mag_b.png");
    ShadowSprite platform3 = new ShadowSprite("Platform3", "platform.png", "shadow_platform_mag_b.png");
    ShadowSprite platform4 = new ShadowSprite("Platform4", "platform.png", "shadow_platform_mag_b.png");
    ShadowSprite platform5 = new ShadowSprite("Platform5", "platform.png","shadow_platform_mag_b.png");

    //Backgrounds
    ShadowSprite background = new ShadowSprite("Background", "background1.png", "shadow_background1_mag.png");
    Sprite sky = new Sprite("Sky", "sky.png");
    DisplayObject mountains1 = new DisplayObject("Mountains1", "mountains1.png");
    DisplayObject mountains2 = new DisplayObject("Mountains2", "mountains2.png");
    DisplayObject mountains3 = new DisplayObject("Mountains3", "mountains3.png");


    ArrayList<DisplayObject> children = getChildren();

    SoundManager soundManager = new SoundManager();

    boolean soundSpriteCollision = false;

    public StimulusPrototype() {
        super("Stimulus Prototype v1.0", 1200, 800);

        background.setPosition(center);
//        addBackgroundLayer(sky);
//        addBackgroundLayer(mountains1);
//        addBackgroundLayer(mountains2);
//        addBackgroundLayer(mountains3);
//
//        for (DisplayObject d : backgroundLayers) {
//            d.setPosition(center);
//        }
//
//        mountains2.setPosX(mountains2.getPosX()-500);
//        mountains2.setPosY(mountains2.getPosY()+100);
//        mountains3.setPosX(mountains2.getPosX()+250);
//        mountains3.setPosY(mountains2.getPosY()+250);
//        sky.setScale(2);

        //Initialize player parameters
        mario.addNewAnimation(AnimatedSprite.IDLE, new int[] {0});
        mario.addNewAnimation(AnimatedSprite.RUN, new int[] {1,2,3,4});
        mario.addNewAnimation(AnimatedSprite.JUMP, new int[] {4});
        mario.centerPivot();
        mario.setPosition(mario.halfWidth(), 800 - mario.halfHeight());
        mario.setScale(0.5);
        mario.setAnim(AnimatedSprite.IDLE);
        mario.playAnim();
        mario.setAnimSpeed(speed/2);
        mario.addRigidBody2D();

        //Initialize game object parameters
        platform1.setPosition(mario.getPosX(), mario.getPosY()+225);
        platform1.setScale(0.25);
        platform1.addRigidBody2D();
        platform1.getRigidBody().toggleGravity(false);
        platform1.setHitbox(platform1.getLocalHitbox().x, platform1.getLocalHitbox().y+platform1.getLocalHitbox().height/25,
                platform1.getLocalHitbox().width, platform1.getLocalHitbox().height - platform1.getLocalHitbox().height/25
                        - 300);

        //Invisible platform
        platform2.setPosition(mario.getPosX()+300, mario.getPosY());
        platform2.setScale(0.2);
        platform2.addRigidBody2D();
        platform2.getRigidBody().toggleGravity(false);
        platform2.setHitbox(platform2.getLocalHitbox().x, platform2.getLocalHitbox().y+platform2.getLocalHitbox().height/25,
                platform2.getLocalHitbox().width, platform2.getLocalHitbox().height - platform2.getLocalHitbox().height/25
                        - 300);
        platform2.setOnlyShadow(true);

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

        //Invisible enemy
        enemy.setPosition(1005, 320);
        enemy.setOnlyShadow(true);
        enemy.addNewAnimation(AnimatedSprite.IDLE, new int[] {0, 1, 2, 4, 5});
        enemy.setAnim(AnimatedSprite.IDLE);
        enemy.playAnim();
        enemy.setAnimSpeed(24);

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

        //Construct Display Tree
        addChild(background);
        addChild(coin);
        addChild(mario);
        addChild(Platforms);
        addChild(enemy);
        Platforms.addChild(platform1);
        Platforms.addChild(platform2);
        Platforms.addChild(platform3);
        Platforms.addChild(platform4);
        Platforms.addChild(platform5);

        //Objectives
        Quest q1 = new Quest("Learning the Ropes", "Find a way to collect the coin");
        questManager.setActiveQuest(q1);

        //Event Listeners
        coin.addEventListener(questManager, Event.COIN_PICKED_UP);
        mario.addEventListener(this, Event.COLLISION);

        //Music
       //soundManager.loadMusic("mario theme", "mario_theme.wav");

        JavaSoundThread audioThread = new JavaSoundThread(44100, 2, 16);
        try {
            int patch = PdBase.openPatch("resources/AUTOMATONISM/main.pd");
            //int patch = PdBase.openPatch("resources/test-patch.pd");
            System.out.println(patch);

        } catch (java.io.IOException e) {
            System.err.print("IO Exception w/ patch!");
        }
        audioThread.start();

        PdBase.sendBang("shadow_on");

    }

    /**
     * Engine will automatically call this update method once per frame and pass to us
     * the set of keys (as keycode ints) that are currently being pressed down
     */
    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<GamePad> gamePads) {
        super.update(pressedKeys, gamePads);

        TweenJuggler.nextFrame();

        if(enemy != null)
            enemy.update(pressedKeys, gamePads);

        //Keyboard input
        if (!pressedKeys.isEmpty()) {

            //Movement
            if (pressedKeys.contains(KeyEvent.VK_RIGHT) && inBoundsRight(mario)) { //move right

                mario.moveRight(speed);
                //parallaxScrolling(speed, mario);
            }
            if (pressedKeys.contains(KeyEvent.VK_LEFT) && inBoundsLeft(mario)) { //move left

                mario.moveLeft(speed);
                //parallaxScrolling(speed, mario);

            }

            if (pressedKeys.contains(KeyEvent.VK_SPACE)) {
                if (mario != null && jmp <= jmpHeight && !jumping) {
                    mario.jump();
                    ++jmp;
                    if (frameClock >= 10) soundManager.loadSoundEffect("jump", "jump.wav");
                    frameClock = 0;
                    if (mario.getPosY() <= 0) {
                        mario.setPosY(mario.halfHeight());
                    }
                    if (jmp == jmpHeight) {
                        jumping = true;
                    }
                }
            }

            if (isMoving() && !mario.getCurrentAnim().equals(AnimatedSprite.JUMP)) {
                mario.setAnim(AnimatedSprite.RUN);

            }

            //Toggle shadows
            if (shadowClock >= shadowRecharge) {
				if (pressedKeys.contains(KeyEvent.VK_F)) {
					toggleShadows();

                    if (shadow) {
                        start = gameClock.getElapsedTime();

                    }
                    shadowClock = 0;
				}
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
                if (mario != null && jmp <= jmpHeight && !jumping) {
                    mario.jump();
                    ++jmp;
                    if (frameClock >= 10) soundManager.loadSoundEffect("jump", "jump.wav");
                    frameClock = 0;
                    if (mario.getPosY() <= 0) {
                        mario.setPosY(mario.halfHeight());
                    }
                    if (jmp == jmpHeight) jumping = true;
                }
            }

            if (isMoving() && !mario.getCurrentAnim().equals(AnimatedSprite.JUMP)) {
                mario.setAnim(AnimatedSprite.RUN);

            }

            //Toggle shadows
            if (shadowClock >= shadowRecharge) {
                if (controller.isButtonPressed(GamePad.BUTTON_SQUARE)) {
                    toggleShadows();

                    if (shadow) {
                        start = gameClock.getElapsedTime();
                        //System.out.println(start);
                    }

                    shadowClock = 0;
                }
            }
        }

        //Idle animation
        if (mario != null && pressedKeys.isEmpty() && !isMoving()) {
            mario.setAnim(AnimatedSprite.IDLE);
        }

        if (mario != null && coin != null && mario.onTriggerEnter(coin)) {
            if (coin.isVisible()) {
                soundManager.loadSoundEffect("coin", "coin.wav");
            }
            coin.dispatchEvent(new Event(Event.COIN_PICKED_UP, coin));
            questManager.completeQuest("Learning the Ropes");

        }

        //Shadow duration
        if (gameClock != null && gameClock.getElapsedTime() - start >= shadowDuration && shadow) {
            toggleShadows();
            shadowClock = 0;
        }

        //collision with soundsprite (sun)
        if(mario != null && enemy != null && enemy.collidesWith(mario)){
            mario.setPosition(mario.halfWidth(), 800 - mario.halfHeight());
            soundSpriteCollision = true;
        }

        //TODO: make more efficient -will get really slow with lots of objects (implement collision grid?)
        if (mario != null) {
            for (DisplayObject platform : Platforms.getChildren()) {
                if (mario.collidesWith(platform)) {
                    mario.dispatchEvent(new Collision(Collision.GROUND, mario, platform));
                    if (mario.getCurrentAnim().equals(AnimatedSprite.JUMP)) {
                        if (isMoving())
                            mario.setAnim(AnimatedSprite.RUN);
                        else
                            mario.setAnim(AnimatedSprite.IDLE);
                    }
                    jmp = 0;
                    jumping = false;
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

        if(enemy != null){
            enemy.draw(g);
        }

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

        if(soundSpriteCollision){
            g.setFont(f);
            g.drawString("You collided with the deathly sound sprite, restart!", centerX-225, centerY-250);
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

        boolean controllerInput = false;
        if (controllers != null && !controllers.isEmpty()) {
            GamePad controller = controllers.get(0);
            controllerInput = controller.getLeftStickXAxis() > 0.5 ||
                    controller.getLeftStickXAxis() < -0.5 ||
                    controller.getLeftStickYAxis() > 0.5 ||
                    controller.getLeftStickYAxis() < -0.5;
        }

        return (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_LEFT)
                || pressedKeys.contains(KeyEvent.VK_UP) || pressedKeys.contains(KeyEvent.VK_DOWN) || controllerInput);

    }

    private void toggleShadows() {

        toggleChildren(this);

        if (getScenePanel().getBackground() != Color.BLACK) {
            getScenePanel().setBackground(Color.BLACK);
            PdBase.sendBang("shadow_off");
        } else {
            getScenePanel().setBackground(Color.WHITE);
            PdBase.sendBang("shadow_on");
        }

        shadow = !shadow;
    }


    private void toggleChildren(DisplayObjectContainer t) {
        if (hasChildren()) {
            for (DisplayObject d : t.getChildren()) {
                if (d instanceof ShadowSprite) {
                    ((ShadowSprite) d).toggleShadow(!((ShadowSprite) d).isShadow());
                    if (((ShadowSprite) d).isOnlyShadow()) {
                        d.setVisible(!d.isVisible());
                    }
                } else if (d instanceof AnimatedShadowSprite) {
                    ((AnimatedShadowSprite) d).toggleShadow(!((AnimatedShadowSprite) d).isShadow());
                    if (((AnimatedShadowSprite) d).isOnlyShadow()) {
                        d.setVisible(!d.isVisible());
                    }
                } else if (d instanceof DisplayObjectContainer) {
                    toggleChildren((DisplayObjectContainer)d);
                }
            }
        }
    }

    //TODO: fix (not working)
    private void parallaxScrolling(int speed, DisplayObject player) {
        for (DisplayObject d : backgroundLayers) {
            if (player.isFacingRight()) {
                d.setPosX(d.getPosX() - speed*backgroundLayers.indexOf(d));
            } else {
                d.setPosX(d.getPosX() + speed*backgroundLayers.indexOf(d));
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
