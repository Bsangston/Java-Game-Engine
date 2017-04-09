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
 * Matthew Leon
 * Cole Schafer
 */
public class StimulusPrototype extends Game {

    //Platform mechanics parameters
    int speed = 4;
    int frameClock = 0;
    int timeRemaining = 60, prevTime = 0;
    int jmpHeight = 10, jmp = 0;
    boolean jumping = false;

    //Stimulus mechanics parameters
    boolean shadow = false;
    int shadowRecharge = 180; //frames
    int shadowClock = 180; //frames
    int shadowDuration = 5000; //ms
    double start = 0;
    GameClock gameClock = new GameClock();

    //Platform Logic
    boolean platform6Up = true;

    //Util
    ArrayList<Integer> lastKeyPressed = new ArrayList<>();

    QuestManager questManager = new QuestManager();
    DisplayObjectContainer GameWorld = new DisplayObjectContainer("GameWorld");
    DisplayObjectContainer Enemies = new DisplayObjectContainer("Enemies");

    //Animated Sprites
    AnimatedShadowSprite mario = new AnimatedShadowSprite("Mario", "bigmario_sprites.png",
            "shadow_mario_mag_b.png", 4, 2);

    AnimatedShadowSprite coin = new AnimatedShadowSprite("Coin", "coin.png",
            "shadow_coin_mag.png", 10, 1);
    AnimatedShadowSoundSprite enemy1 = new AnimatedShadowSoundSprite("Ghost1", "shadow_boo.png",
            "shadow_boo.png", 3, 2);
    AnimatedShadowSoundSprite enemy2 = new AnimatedShadowSoundSprite("Ghost2", "shadow_boo.png",
            "shadow_boo.png", 3, 2);

    AnimatedShadowSprite pickup = new AnimatedShadowSprite("Pickup", "pickupAnim.jpg", "pickupAnim.jpg", 8, 8);

    //Sprites
    DisplayObjectContainer Platforms = new DisplayObjectContainer("Platforms");
    ShadowSprite platform1 = new ShadowSprite("Platform1", "platform.png", "shadow_platform_mag_b.png");
    ShadowSprite platform2 = new ShadowSprite("Platform2", "platform.png", "shadow_platform_mag_b.png");
    ShadowSprite platform3 = new ShadowSprite("Platform3", "shadow_platform_mag_b.png");
    ShadowSprite platform4 = new ShadowSprite("Platform4", "platform.png", "shadow_platform_mag_b.png");
    ShadowSprite platform5 = new ShadowSprite("Platform5", "shadow_platform_mag_b.png");
    ShadowSprite platform6 = new ShadowSprite("Platform6", "platform.png", "shadow_platform_mag_b.png");
    ShadowSprite platform7 = new ShadowSprite("Platform7", "platform.png","shadow_platform_mag_b.png");
    ShadowSprite platform8 = new ShadowSprite("Platform9", "platform.png", "shadow_platform_mag_b.png");
    ShadowSprite platform9 = new ShadowSprite("Platform0", "platform.png","shadow_platform_mag_b.png");


    //Backgrounds
    ShadowSprite background = new ShadowSprite("Background", "background1.png", "shadow_background1_mag.png");
    Sprite sky = new Sprite("Sky", "sky.png");
    DisplayObject mountains1 = new DisplayObject("Mountains1", "mountains1.png");
    DisplayObject mountains2 = new DisplayObject("Mountains2", "mountains2.png");
    DisplayObject mountains3 = new DisplayObject("Mountains3", "mountains3.png");


    ArrayList<DisplayObject> children = getChildren();

    Vec2 spawnPoint = new Vec2(100, centerY + 150);

    SoundManager soundManager = new SoundManager();

    boolean soundSpriteCollision = false;

    boolean jumpReady;
    boolean landed;

    public StimulusPrototype() {
        super("Stimulus Prototype v1.0", 1400, 800);

        background.setPosition(center);
//        addBackgroundLayer(sky);
//        addBackgroundLayer(mountains1);
//        addBackgroundLayer(mountains2);
//        addBackgroundLayer(mountains3);
//
//        for (DisplayObject d : backgroundLayers) {
//            d.setPosition(center);
//        }

        mountains2.setPosX(mountains2.getPosX()-500);
        mountains2.setPosY(mountains2.getPosY()+100);
        mountains3.setPosX(mountains2.getPosX()+250);
        mountains3.setPosY(mountains2.getPosY()+250);
        sky.setScale(2);

        //Initialize player parameters
        mario.addNewAnimation(AnimatedSprite.IDLE, new int[] {0});
        mario.addNewAnimation(AnimatedSprite.RUN, new int[] {1,2,3,4});
        mario.addNewAnimation(AnimatedSprite.JUMP, new int[] {4});
        mario.centerPivot();
        mario.setScale(0.2);
        mario.setPosition(100, centerY + 150);
        mario.setAnim(AnimatedSprite.IDLE);
        mario.playAnim();
        mario.setAnimSpeed(speed);
        mario.addRigidBody2D();

        //spawnPoint = new Vec2(100, centerY + 150);

        //Initialize game object parameters
        platform1.setPosition((int)spawnPoint.x, (int)spawnPoint.y + 200);
        platform1.setScale(0.15);
        platform1.addRigidBody2D();
        platform1.getRigidBody().toggleGravity(false);
        platform1.setHitbox(platform1.getLocalHitbox().x, platform1.getLocalHitbox().y+platform1.getLocalHitbox().height/50,
                platform1.getLocalHitbox().width, platform1.getLocalHitbox().height - platform1.getLocalHitbox().height/25
                        - 300);

        platform2.setPosition(mario.getPosX()+200, 650);
        platform2.setScale(0.15);
        platform2.addRigidBody2D();
        platform2.getRigidBody().toggleGravity(false);
        platform2.setHitbox(platform2.getLocalHitbox().x, platform2.getLocalHitbox().y+platform2.getLocalHitbox().height/50,
                platform2.getLocalHitbox().width, platform2.getLocalHitbox().height - platform2.getLocalHitbox().height/25
                        - 300);

        //Invisible platform
        platform3.setPosition(platform2.getPosX()+200, 650);
        platform3.setScale(0.15);
        platform3.addRigidBody2D();
        platform3.getRigidBody().toggleGravity(false);
        platform3.setHitbox(platform3.getLocalHitbox().x, platform3.getLocalHitbox().y+platform3.getLocalHitbox().height/50,
                platform3.getLocalHitbox().width, platform3.getLocalHitbox().height - platform3.getLocalHitbox().height/25
                        - 300);
        platform3.setOnlyShadow(true);

        platform4.setPosition(platform3.getPosX()+200, 650);
        platform4.setScale(0.15);
        platform4.addRigidBody2D();
        platform4.getRigidBody().toggleGravity(false);
        platform4.setHitbox(platform4.getLocalHitbox().x, platform4.getLocalHitbox().y+platform4.getLocalHitbox().height/50,
                platform4.getLocalHitbox().width, platform4.getLocalHitbox().height - platform4.getLocalHitbox().height/25 - 300);


        platform5.setPosition(platform4.getPosX()+200, 650);
        platform5.setScale(0.15);
        platform5.addRigidBody2D();
        platform5.getRigidBody().toggleGravity(false);
        platform5.setHitbox(platform5.getLocalHitbox().x, platform5.getLocalHitbox().y+platform5.getLocalHitbox().height/50,
                platform5.getLocalHitbox().width, platform5.getLocalHitbox().height - platform5.getLocalHitbox().height/25
                        - 300);
        platform5.setOnlyShadow(true);

        platform6.setPosition(platform5.getPosX(), 500);
        platform6.setScale(0.15);
        platform6.addRigidBody2D();
        platform6.getRigidBody().toggleGravity(false);
        platform6.setHitbox(platform6.getLocalHitbox().x, platform6.getLocalHitbox().y+platform6.getLocalHitbox().height/50,
                platform6.getLocalHitbox().width, platform6.getLocalHitbox().height - platform6.getLocalHitbox().height/25
                        - 300);

        //Invisible enemy
        enemy1.setScale(0.5f);
        enemy1.setPosition(platform5.getPosX(), platform5.getPosY() - 100);
        enemy1.setOnlyShadow(true);
        enemy1.addNewAnimation(AnimatedSprite.IDLE, new int[] {0, 1, 2, 4, 5});
        enemy1.setAnim(AnimatedSprite.IDLE);
        enemy1.playAnim();
        enemy1.setAnimSpeed(24);

        enemy2.setScale(0.5f);
        enemy2.setPosition(centerX*2 + enemy2.getScaledWidth(), centerY*2 - 100);
        enemy2.setOnlyShadow(true);
        enemy2.addNewAnimation(AnimatedSprite.IDLE, new int[] {0, 1, 2, 4, 5});
        enemy2.setAnim(AnimatedSprite.IDLE);
        enemy2.playAnim();
        enemy2.setAnimSpeed(24);

        coin.addNewAnimation("spin", new int[] {0,1,2,3,4,5,6,7,8,9});
        coin.centerPivot();
        coin.setPosition(platform5.getPosX(), platform5.getPosY() - 100);
        coin.setScale(0.5);
        coin.setAnim("spin");
        coin.playAnim();
        coin.setAnimSpeed(6);

//        pickup.addNewAnimation(AnimatedSprite.IDLE, new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,
//                21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,
//                54,55,56,57,58,59,60,61,62,63});
//        pickup.centerPivot();
//        pickup.setPosition(platform3.getPosX(), platform3.getPosY() - 100);
//        pickup.setScale(0.5);
//        pickup.setAnim(AnimatedSprite.IDLE);
//        pickup.setAnimSpeed(speed/4);
//        pickup.playAnim();
//        pickup.setOnlyShadow(true);
//

        //Construct Display Tree
        addChild(background);
        //addChild(coin);
        //addChild(pickup);
        addChild(mario);
        addChild(Platforms);
        addChild(Enemies);
        Enemies.addChild(enemy1);
        Enemies.addChild(enemy2);
        Platforms.addChild(platform1);
        Platforms.addChild(platform2);
        Platforms.addChild(platform3);
        Platforms.addChild(platform4);
        Platforms.addChild(platform5);
        Platforms.addChild(platform6);
//        Platforms.addChild(platform7);
//        Platforms.addChild(platform8);
//        Platforms.addChild(platform9);

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
            //int patch = PdBase.openPatch("resources/AUTOMATONISM/main.pd");
            int music_patch = PdBase.openPatch("resources/AUTOMATONISM/GenerativeMusicPatches/_main.pd");
            //System.out.println(patch);

        } catch (java.io.IOException e) {
            System.err.print("IO Exception w/ patch!");
        }
        audioThread.start();

        PdBase.sendBang("shadow_on");

        jumpReady = true;
        landed = true;

    }

    /**
     * Engine will automatically call this update method once per frame and pass to us
     * the set of keys (as keycode ints) that are currently being pressed down
     */
    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<GamePad> gamePads) {
        super.update(pressedKeys, gamePads);

        TweenJuggler.nextFrame();

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
//                    if (frameClock >= 10) soundManager.loadSoundEffect("jump", "jump.wav");
//                    frameClock = 0;
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
                if (mario != null && jmp <= jmpHeight && !jumping && jumpReady) {

                    landed = false;
                    if(jmp == jmpHeight-1)
                        jumpReady = false;

                    mario.jump();
                    ++jmp;
//                    if (frameClock >= 10) soundManager.loadSoundEffect("jump", "jump.wav");
//                    frameClock = 0;
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

        //Platform movement
        if (platform6 != null) {
            System.out.println(platform6.getPosY());
            if (platform6Up) {
                if (platform6.getPosY() < centerY-200) {
                    platform6Up = false;
                }
                platform6.moveUp(speed/4);
            } else {
                if (platform6.getPosY() - platform6.getScaledHeight() >= centerY) {
                    platform6Up = true;
                }
                platform6.moveDown(speed/4);
            }


        }

        //Enemy movement
        if (enemy2 != null) {
           float startPoint = centerX*2 + enemy2.getScaledWidth();
           enemy2.moveRight(-speed/2);
           if (enemy2.getPosX() < -200) {
               enemy2.setPosX((int)startPoint);
           }



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

                    if (mario.getPosY() < platform.getPosY()) {
                        jmp = 0;
                        jumping = false;
                        landed = true;
                    }
                }
            }

            for (DisplayObject enemy : Enemies.getChildren()) {
                if (mario.collidesWith(enemy)) {
                    mario.dispatchEvent(new Collision(Collision.ENEMY, mario, enemy));
                    mario.setPosition(100, centerY + 150);
                    soundSpriteCollision = true;
                }
            }

            //Respawn
            if (mario.getPosY() >= 3000) {
                mario.setPosition(100, centerY + 150);
            }
        }

        if(landed && !jumpReady && !pressedKeys.contains(KeyEvent.VK_SPACE) && !controllers.get(0).isButtonPressed(GamePad.BUTTON_CROSS)){
            jumpReady = true;
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

        if(enemy1 != null){
            enemy1.draw(g);
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

        return pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_LEFT)
                || pressedKeys.contains(KeyEvent.VK_UP) || pressedKeys.contains(KeyEvent.VK_DOWN)
                || pressedKeys.contains(KeyEvent.VK_D) || pressedKeys.contains(KeyEvent.VK_A)
                || pressedKeys.contains(KeyEvent.VK_W) || pressedKeys.contains(KeyEvent.VK_S) || controllerInput;

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
                d.setPosX(d.getPosX() - speed*backgroundLayers.indexOf(d)) ;
            } else {
                d.setPosX(d.getPosX() + speed * backgroundLayers.indexOf(d));
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
