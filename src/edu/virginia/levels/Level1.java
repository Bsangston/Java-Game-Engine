package edu.virginia.levels;

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
 * Created by BrandonSangston on 4/22/17.
 * Matthew Leon
 * Cole Schafer
 */
public class Level1 extends DisplayObjectContainer {

    Game g;

    //Platform mechanics parameters
    int speed = 4;
    int frameClock = 0;
    int timeRemaining = 60, prevTime = 0;
    int jmpHeight = 10, jmp = 0;
    boolean jumping = false;

    //Stimulus mechanics parameters
    boolean shadow = false;
    double shadowClock = 180; //frames
    double shadowClockMax = 180;
    int soundModeBarLength = 200;
    int soundModeBarWidth = 30;
    double rechargeRate = 0.5;

    //Util
    ArrayList<Integer> lastKeyPressed = new ArrayList<>();
    boolean shadowToggled = false;

    QuestManager questManager = new QuestManager();
    DisplayObjectContainer GameWorld = new DisplayObjectContainer("GameWorld");

    //Animated Sprites
    AnimatedShadowSprite player = new AnimatedShadowSprite("player", "bigmario_sprites.png",
            "shadow_mario_mag_b.png", 4, 2);

    AnimatedShadowSprite coin = new AnimatedShadowSprite("Coin", "coin.png",
            "shadow_coin_mag.png", 10, 1);

    AnimatedShadowSprite pickup = new AnimatedShadowSprite("Pickup", "pickupAnim.jpg", "pickupAnim.jpg", 8, 8);

    //Sprites
    DisplayObjectContainer Platforms = new DisplayObjectContainer("Platforms");
    ShadowSprite platform1 = new ShadowSprite("Platform1", "basic_platform.png", "shadow_basic_platform.png");
    ShadowSprite platform2 = new ShadowSprite("Platform2", "shadow_basic_platform.png");
    ShadowSprite platform3 = new ShadowSprite("Platform3", "basic_platform.png", "shadow_basic_platform.png");
    ShadowSprite platform5 = new ShadowSprite("Platform5", "shadow_basic_platform.png");
    ShadowSprite platform4 = new ShadowSprite("Platform4", "basic_platform.png", "shadow_basic_platform.png");


    //Backgrounds
    ShadowSprite background = new ShadowSprite("Background", "background1.png", "shadow_background1_mag.png");

    ArrayList<DisplayObject> children = getChildren();

    Vec2 spawnPoint;

    SoundManager soundManager = new SoundManager();

    boolean jumpReady;
    boolean landed;

    public Level1(Game wrapper) {
        super("Stimulus Beta");

        g = wrapper;

        spawnPoint = new Vec2(g.centerX/4, g.centerY);

        background.setPosition(g.center);

        //Initialize player parameters
        player.addNewAnimation(AnimatedSprite.IDLE, new int[] {0});
        player.addNewAnimation(AnimatedSprite.RUN, new int[] {1,2,3,4});
        player.addNewAnimation(AnimatedSprite.JUMP, new int[] {4});
        player.centerPivot();
        player.setScale(0.2f);
        player.setPosition(225, g.centerY - 150);
        player.setAnim(AnimatedSprite.IDLE);
        player.setAnimSpeed(speed);
        player.playAnim();
        player.addRigidBody2D();

        //spawnPoint = new Vec2(100, centerY + 150);

        //Initialize game object parameters
        platform1.setPosition(225, g.centerY);

        platform2.setPosition(player.getPosX()+225, g.centerY);
        platform2.setOnlyShadow(true);

        platform3.setPosition(platform2.getPosX()+225, g.centerY);

        platform5.setPosition(platform3.getPosX()+225, g.centerY);
        platform5.setOnlyShadow(true);

        platform4.setPosition(platform5.getPosX()+225, g.centerY);

        coin.addNewAnimation("spin", new int[] {0,1,2,3,4,5,6,7,8,9});
        coin.centerPivot();
        coin.setPosition(platform4.getPosX(), platform4.getPosY() - 50);
        coin.setScale(0.25);
        coin.setAnim("spin");
        coin.playAnim();
        coin.setAnimSpeed(6);


        //Construct Display Tree
        addChild(background);
        addChild(coin);
        addChild(player);
        addChild(Platforms);
        Platforms.addChild(platform1);
        Platforms.addChild(platform2);
        Platforms.addChild(platform3);
        Platforms.addChild(platform4);
        Platforms.addChild(platform5);

        for (DisplayObject platform : Platforms.getChildren()) {
            platform.setScale(0.175f); //original 0.135
            platform.addRigidBody2D();
            platform.getRigidBody().toggleGravity(false);
        }

        moveDown(50);

        //Objectives
        Quest q1 = new Quest("Learning the Ropes", "Use your state-switching ability to collect the coin (F or Square).");
        questManager.setActiveQuest(q1);

        //Event Listeners
        coin.addEventListener(questManager, Event.COIN_PICKED_UP);
        player.addEventListener(this, Event.COLLISION);

        //Music
        //soundManager.loadMusic("player theme", "player_theme.wav");

        /*JavaSoundThread audioThread = new JavaSoundThread(44100, 2, 16);
        try {
            int patch = PdBase.openPatch("resources/AUTOMATONISM/main.pd");
            //int music_patch = PdBase.openPatch("resources/AUTOMATONISM/GenerativeMusicPatches/_main.pd");
            //System.out.println(patch);

        } catch (java.io.IOException e) {
            System.err.print("IO Exception w/ patch!");
        }
        audioThread.start();

        PdBase.sendBang("toggle-music");
        PdBase.sendBang("shadow-on");*/

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
            if ((pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_D)) && inBoundsRight(player)) { //move right

                player.moveRight(speed);
                //parallaxScrolling(1, player);
            }
            if ((pressedKeys.contains(KeyEvent.VK_LEFT) || pressedKeys.contains(KeyEvent.VK_A)) && inBoundsLeft(player)) { //move left

                player.moveLeft(speed);
                //parallaxScrolling(1, player);

            }

            if (pressedKeys.contains(KeyEvent.VK_SPACE)) {
                if (player != null && jmp <= jmpHeight && !jumping && jumpReady) {

                    landed = false;
                    if(jmp == jmpHeight-1)
                        jumpReady = false;

                    player.jump();
                    ++jmp;

                    if (jmp == jmpHeight) {
                        jumping = true;
                    }
                }
            }
        }

        if(shadow && !pressedKeys.contains(KeyEvent.VK_F) && lastKeyPressed.contains(KeyEvent.VK_F)){
            toggleShadows();
            lastKeyPressed.remove(new Integer(KeyEvent.VK_F));
        }

        if(shadowClock > shadowClockMax / 4){
            if (!shadow && !pressedKeys.contains(KeyEvent.VK_F) && lastKeyPressed != null && lastKeyPressed.contains(KeyEvent.VK_F)) {
                toggleShadows();
            }
        }

        //Controller input
        if (!gamePads.isEmpty()) {
            GamePad controller = gamePads.get(0);

            //Movement
            if (controller.isButtonPressed(GamePad.DPAD_RIGHT) || controller.getLeftStickXAxis() > 0.5
                    && inBoundsRight(player)) { //move right

                player.moveRight(speed);
            }
            if (controller.isButtonPressed(GamePad.DPAD_LEFT) || controller.getLeftStickXAxis() < -0.5
                    && inBoundsLeft(player)) { //move left

                player.moveLeft(speed);

            }

            if (controller.isButtonPressed(GamePad.BUTTON_CROSS)) {
                if (player != null && jmp <= jmpHeight && !jumping && jumpReady) {

                    landed = false;
                    if(jmp == jmpHeight-1)
                        jumpReady = false;

                    player.jump();
                    ++jmp;

                    if (jmp == jmpHeight) {
                        jumping = true;
                    }
                }
            }

            if (isMoving() && !player.getCurrentAnim().equals(AnimatedSprite.JUMP)) {
                player.setAnim(AnimatedSprite.RUN);

            }

//            if (shadow && controller.isButtonPressed(GamePad.BUTTON_SQUARE)) {
//                toggleShadows();
//            } else if (shadowClock > shadowClockMax / 4){
//                if (!shadow && controller.isButtonPressed(GamePad.BUTTON_SQUARE)) {
//                    toggleShadows();
//                }
//            }

            if (controller.isButtonPressed(GamePad.BUTTON_SQUARE)) {
                if (!shadow) {
                    if (shadowClock > shadowClockMax/4) {
                        toggleShadows();
                    }
                } else if (shadow) {
                    toggleShadows();
                }
            }

        }

        //Idle animation
        if (player != null && pressedKeys.isEmpty() && !isMoving()) {
            player.setAnim(AnimatedSprite.IDLE);
        }

        if (player != null && coin != null && player.onTriggerEnter(coin)) {
            if (coin.isVisible()) {
                soundManager.loadSoundEffect("coin", "coin.wav");
            }
            coin.dispatchEvent(new Event(Event.COIN_PICKED_UP, coin));
            questManager.completeQuest("Learning the Ropes");

            if(shadow)
                toggleShadows();

            this.dispatchEvent(new Event("end_level_1", this));

        }

        //Shadow duration
        if (shadowClock <= 0 && shadow) {
            toggleShadows();
        }


        //TODO: make more efficient -will get really slow with lots of objects (implement collision grid?)
        if (player != null) {
            for (DisplayObject platform : Platforms.getChildren()) {
                if (player.collidesWith(platform)) {

                    player.dispatchEvent(new Collision(Collision.GROUND, player, platform));

                    if (player.getCurrentAnim().equals(AnimatedSprite.JUMP)) {
                        if (isMoving())
                            player.setAnim(AnimatedSprite.RUN);
                        else
                            player.setAnim(AnimatedSprite.IDLE);
                    }

                    if (player.getPosY() < platform.getPosY()) {
                        jmp = 0;
                        jumping = false;
                        landed = true;
                    }

                }
            }


            //Respawn
            if (player.getPosY() >= 1500) {
                respawn();
            }
        }

        if(landed && !jumpReady && !pressedKeys.contains(KeyEvent.VK_SPACE)){
            jumpReady = true;
        }


        //Exit game
        if (pressedKeys.contains(KeyEvent.VK_ESCAPE)) {
            g.exitGame();
            g.closeGame();
        }

        ++frameClock;

        if(shadow){
            shadowClock = shadowClock - 1;
        } else if(shadowClock != shadowClockMax) {
            shadowClock = shadowClock + rechargeRate;
        }

        lastKeyPressed = new ArrayList<>(pressedKeys);

        g.mouseEvents.clear();

    }

    /**
     * Engine automatically invokes draw() every frame as well. If we want to make sure player gets drawn to
     * the screen, we need to make sure to override this method and call player's draw method.
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
            g.drawString("Level 1: " + questManager.getActiveQuest().getId(), 15, 20); //25
            g.drawString("Objective: " + questManager.getActiveQuest().getObjective(), 15, 40); //50

            if (questManager.getActiveQuest().isComplete()) {
                g.setFont(f);
                g.drawString("Level 1 Completed!", this.g.centerX-120, this.g.centerY-150);
            }

        }

        g.setColor(Color.red);
        g.drawRect(2*this.g.centerX-soundModeBarLength-15, 15, soundModeBarLength,soundModeBarWidth);
        g.fillRect(2*this.g.centerX-soundModeBarLength-15, 15, (int)((shadowClock * 1.0/ shadowClockMax) * soundModeBarLength),soundModeBarWidth);
    }

    private boolean inBounds(Sprite s) {
        return inBoundsRight(s) && inBoundsBottom(s) &&
                inBoundsLeft(s) && inBoundsTop(s);
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

    private void toggleShadows() {

        toggleChildren(this);

        if (g.getScenePanel().getBackground() != Color.BLACK) {
            g.getScenePanel().setBackground(Color.BLACK);
            PdBase.sendBang("shadow_off");
        } else {
            g.getScenePanel().setBackground(Color.WHITE);
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

    private void respawn() {
        player.setPosition(225, g.centerY-150);
    }

}