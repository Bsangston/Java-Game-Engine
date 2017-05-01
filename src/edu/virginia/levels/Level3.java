package edu.virginia.levels;

import edu.virginia.engine.cole_tween.TweenJuggler;
import edu.virginia.engine.controller.GamePad;
import edu.virginia.engine.display.*;
import edu.virginia.engine.events.Collision;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.Quest;
import edu.virginia.engine.events.QuestManager;
import edu.virginia.engine.sound.JavaSoundThread;
import edu.virginia.engine.sound.SoundManager;
import org.puredata.core.PdBase;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by BrandonSangston on 4/23/17.
 * Matthew Leon
 * Cole Schafer
 */
public class Level3 extends DisplayObjectContainer {

    Game game;

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

    //Platform/Enemy Logic
    boolean platform6Up = true;
    boolean platform3Up = false;
    boolean enemy1up = true;
    boolean enemy2up = false;

    //Util
    ArrayList<Integer> lastKeyPressed = new ArrayList<>();
    ArrayList<String> lastButtonPressed = new ArrayList<>();

    QuestManager questManager = new QuestManager();
    DisplayObjectContainer GameWorld = new DisplayObjectContainer("GameWorld");
    DisplayObjectContainer Enemies = new DisplayObjectContainer("Enemies");

    //Animated Sprites
    AnimatedShadowSprite player = new AnimatedShadowSprite("player", "bigmario_sprites.png",
            "shadow_mario_mag_b.png", 4, 2);

    AnimatedShadowSprite coin = new AnimatedShadowSprite("Coin", "coin.png",
            "shadow_coin_mag.png", 10, 1);
    AnimatedShadowSoundSprite enemy1 = new AnimatedShadowSoundSprite("Ghost1", "shadow_boo.png",
            "shadow_boo.png", 3, 2);
    AnimatedShadowSoundSprite enemy2 = new AnimatedShadowSoundSprite("Ghost1", "shadow_boo.png",
            "shadow_boo.png", 3, 2);

    AnimatedShadowSprite pickup = new AnimatedShadowSprite("Pickup", "pickupAnim.jpg", "pickupAnim.jpg", 8, 8);

    //Sprites
    DisplayObjectContainer Platforms = new DisplayObjectContainer("Platforms");
    ShadowSprite platform1 = new ShadowSprite("Platform1", "basic_platform.png", "shadow_basic_platform.png");
    ShadowSprite platform2 = new ShadowSprite("Platform2", "shadow_basic_platform.png");
    ShadowSprite platform3 = new ShadowSprite("Platform3", "basic_platform.png", "shadow_basic_platform.png");
    ShadowSprite platform5 = new ShadowSprite("Platform5", "shadow_basic_platform.png");
    ShadowSprite platform4 = new ShadowSprite("Platform4", "basic_platform.png", "shadow_basic_platform.png");
    ShadowSprite platform6 = new ShadowSprite("Platform6", "basic_platform.png", "shadow_basic_platform.png");
    ShadowSprite platform7 = new ShadowSprite("Platform7", "basic_platform.png", "shadow_basic_platform.png");

    //Backgrounds
    ShadowSprite background = new ShadowSprite("Background", "background1.png", "shadow_background1_mag.png");

    ArrayList<DisplayObject> children = getChildren();

    Vec2 spawnPoint;

    SoundManager soundManager = new SoundManager();

    boolean jumpReady;
    boolean landed;
    boolean soundSpriteCollision = false;

    public Level3(Game game) {
        super("Stimulus Beta");

        this.game = game;

        spawnPoint = new Vec2(game.centerX/4, game.centerY);

        background.setPosition(game.center);

        //Initialize player parameters
        player.addNewAnimation(AnimatedSprite.IDLE, new int[] {0});
        player.addNewAnimation(AnimatedSprite.RUN, new int[] {1,2,3,4});
        player.addNewAnimation(AnimatedSprite.JUMP, new int[] {4});
        player.centerPivot();
        player.setScale(0.2f);
        player.setPosition(112, game.centerY - 150);
        player.setAnim(AnimatedSprite.IDLE);
        player.setAnimSpeed(speed);
        player.playAnim();
        player.addRigidBody2D();

        //spawnPoint = new Vec2(100, centerY + 150);

        //Initialize game object parameters
        platform1.setPosition(112, game.centerY);
        platform7.setPosition(2*game.centerX-112, game.centerY);

        platform4.setPosition(game.centerX, game.centerY); //moves
        platform3.setPosition(platform4.getPosX()-225, game.centerY); //moves
        platform6.setPosition(platform4.getPosX()+225, game.centerY); //moves

        platform2.setPosition(Math.abs((platform1.getPosX()-platform3.getPosX())/2)+platform1.getPosX(), game.centerY);
        platform2.setOnlyShadow(true);

        platform5.setPosition(Math.abs((platform6.getPosX()-platform7.getPosX())/2)+platform6.getPosX(), game.centerY);
        platform5.setOnlyShadow(true);

        coin.addNewAnimation("spin", new int[] {0,1,2,3,4,5,6,7,8,9});
        coin.centerPivot();
        coin.setPosition(platform7.getPosX(), platform7.getPosY() - 50);

        coin.setScale(0.25);
        coin.setAnim("spin");
        coin.playAnim();
        coin.setAnimSpeed(6);

        //Invisible enemies
        enemy1.setScale(0.25f);
        enemy1.setPosition(platform3.getPosX()+112, game.centerY - 100);

        enemy2.setScale(0.25f);
        enemy2.setPosition(platform4.getPosX()+112, game.centerY + 100);

        //Construct Display Tree
        addChild(background);
        addChild(coin);
        addChild(player);
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
        Platforms.addChild(platform7);

        for (DisplayObject platform : Platforms.getChildren()) {
            platform.setScale(0.175f); //original 0.135
            platform.addRigidBody2D();
            platform.getRigidBody().toggleGravity(false);
        }

        for (DisplayObject enemy : Enemies.getChildren()) {
            if (enemy instanceof AnimatedShadowSoundSprite) {
                AnimatedShadowSoundSprite enemy_ = (AnimatedShadowSoundSprite)(enemy);
                enemy_.setOnlyShadow(true);
                enemy_.addNewAnimation(AnimatedSprite.IDLE, new int[]{0, 1, 2, 4, 5});
                enemy_.setAnim(AnimatedSprite.IDLE);
                enemy_.playAnim();
                enemy_.setAnimSpeed(24);
            }
        }

        platform2.setScaleX(0.125f);
        platform5.setScaleX(0.125f);

        moveDown(50);

        //Objectives
        Quest q1 = new Quest("On the Move", "Collect the coin. Be wary of enemies...");
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

        } catch (IOException e) {
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
                if (!lastButtonPressed.contains(GamePad.BUTTON_CROSS)) lastButtonPressed.add(GamePad.BUTTON_CROSS);
            }

            if (isMoving() && !player.getCurrentAnim().equals(AnimatedSprite.JUMP)) {
                player.setAnim(AnimatedSprite.RUN);

            }


            if (controller.isButtonPressed(GamePad.BUTTON_SQUARE) && !lastButtonPressed.contains(GamePad.BUTTON_SQUARE)) {
                if (!shadow) {
                    if (shadowClock > shadowClockMax/4) {
                        toggleShadows();
                    }
                } else if (shadow) {
                    toggleShadows();
                }
                if (!lastButtonPressed.contains(GamePad.BUTTON_SQUARE)) lastButtonPressed.add(GamePad.BUTTON_SQUARE);
            }

            if (!controller.isButtonPressed(GamePad.BUTTON_SQUARE) && lastButtonPressed.contains(GamePad.BUTTON_SQUARE)) {
                lastButtonPressed.remove(GamePad.BUTTON_SQUARE);
            }

            if (!controller.isButtonPressed(GamePad.BUTTON_CROSS) && lastButtonPressed.contains(GamePad.BUTTON_CROSS)) {
                lastButtonPressed.remove(GamePad.BUTTON_CROSS);
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
            //coin.dispatchEvent(new Event(Event.COIN_PICKED_UP, coin));
            //questManager.completeQuest("On the Move");

            if(shadow)
                toggleShadows();

            player.setPosition(112, game.centerY - 150);
            this.dispatchEvent(new Event("end_level_3", this));

        }

        //Shadow duration
        if (shadowClock <= 0 && shadow) {
            toggleShadows();
        }

        //TODO: Write methods for platform and enemy movement (ex: platform.moveWithinRange(Vec2 start, Vec2 stop)
        //Platform movement
        if (platform3 != null) {
            if (platform3Up) {
                if (platform3.getPosY() < game.centerY-200) {
                    platform3Up = false;
                }
                platform3.moveUp(speed/4);
            } else {
                if (platform3.getPosY() >= game.centerY+200) {
                    platform3Up = true;
                }
                platform3.moveDown(speed/4);
            }
        }

        if (platform6 != null) {
            if (platform6Up) {
                if (platform6.getPosY() < game.centerY-200) {
                    platform6Up = false;
                }
                platform6.moveUp(speed/4);
            } else {
                if (platform6.getPosY() >= game.centerY+200) {
                    platform6Up = true;
                }
                platform6.moveDown(speed/4);
            }
        }

        //Enemy movement
        if (enemy1 != null) {
            float startPoint = game.centerY - 200;
            if (enemy1up) {
                if (enemy1.getPosY() < startPoint) {
                    enemy1up = false;
                }
                enemy1.moveUp(speed/2);
            } else {
                if (enemy1.getPosY() - enemy1.getScaledHeight() >= startPoint + 400) {
                    enemy1up = true;
                }
                enemy1.moveDown(speed/2);
            }
        }
        if (enemy2 != null) {
            float startPoint = game.centerY - 200;
            if (enemy2up) {
                if (enemy2.getPosY() < startPoint) {
                    enemy2up = false;
                }
                enemy2.moveUp(speed/2);
            } else {
                if (enemy2.getPosY() - enemy2.getScaledHeight() >= startPoint + 400) {
                    enemy2up = true;
                }
                enemy2.moveDown(speed/2);
            }
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

            ArrayList<Float> distList = new ArrayList<>();

            for (DisplayObject enemy : Enemies.getChildren()) {

                float dist = calcDistance(player.getPosition(), enemy.getPosition());
                distList.add(dist);

                if (player.collidesWith(enemy)) {
                    player.dispatchEvent(new Collision(Collision.ENEMY, player, enemy));
                    respawn();
                    soundSpriteCollision = true;
                }

                float closestEnemyDistance = 600;

                for (int i = 0; i < distList.size(); i++) {
                    if (distList.get(i) < closestEnemyDistance) {
                        closestEnemyDistance = distList.get(i);
                    }
                }
                PdBase.sendFloat("enemy_distance", closestEnemyDistance);

            }


            //Respawn
            if (player.getPosY() >= 1500) {
                respawn();
                soundSpriteCollision = false;
            }
        }

        if(landed && !jumpReady && !pressedKeys.contains(KeyEvent.VK_SPACE) && !lastButtonPressed.contains(GamePad.BUTTON_CROSS)){
            jumpReady = true;
        }


        //Exit game
        if (pressedKeys.contains(KeyEvent.VK_ESCAPE)) {
            game.exitGame();
            game.closeGame();
        }

        ++frameClock;

        if(shadow){
            shadowClock = shadowClock - 1;
        } else if(shadowClock != shadowClockMax) {
            shadowClock = shadowClock + rechargeRate;
        }

        lastKeyPressed = new ArrayList<>(pressedKeys);

        game.mouseEvents.clear();

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
            g.drawString("Level 3: " + questManager.getActiveQuest().getId(), 15, 20); //25
            g.drawString("Objective: " + questManager.getActiveQuest().getObjective(), 15, 40); //50

            if (questManager.getActiveQuest().isComplete()) {
                g.setFont(f);
                g.drawString("Level 3 Completed!", game.centerX-120, game.centerY-250);
            }

        }

        if(soundSpriteCollision){
            g.setFont(f);
            g.drawString("You collided with the deathly sound sprite, restart!", game.centerX-250, game.centerY-250);
        }

        g.setColor(Color.red);

        g.drawRect(2*game.centerX-soundModeBarLength-15, 15, soundModeBarLength,soundModeBarWidth);
        g.fillRect(2*game.centerX-soundModeBarLength-15, 15, (int)((shadowClock * 1.0/ shadowClockMax) * soundModeBarLength),soundModeBarWidth);
    }

    private boolean inBounds(Sprite s) {
        return inBoundsRight(s) && inBoundsBottom(s) &&
                inBoundsLeft(s) && inBoundsTop(s);
    }

    private boolean inBoundsRight(Sprite s) {
        return s.getPosX() <= game.getMainFrame().getWidth() - s.getScaledWidth()/2;
    }

    private boolean inBoundsLeft(Sprite s) {
        return (!s.isFacingRight() && s.getPosX() >= s.getScaledWidth()/2) ||
                (s.isFacingRight() && s.getPosX() >= s.getScaledWidth()/2);
    }

    private boolean inBoundsTop(Sprite s) {
        return s.getPosY() >= s.getScaledHeight()/2;
    }

    private boolean inBoundsBottom(Sprite s) {
        return s.getPosY() <= game.getMainFrame().getHeight() - s.getScaledHeight()/2;
    }


    private boolean isMoving() {

        boolean controllerInput = false;
        if (game.controllers != null && !game.controllers.isEmpty()) {
            GamePad controller = game.controllers.get(0);
            controllerInput = controller.getLeftStickXAxis() > 0.5 ||
                    controller.getLeftStickXAxis() < -0.5 ||
                    controller.getLeftStickYAxis() > 0.5 ||
                    controller.getLeftStickYAxis() < -0.5;
        }

        return game.pressedKeys.contains(KeyEvent.VK_RIGHT) || game.pressedKeys.contains(KeyEvent.VK_LEFT)
                || game.pressedKeys.contains(KeyEvent.VK_UP) || game.pressedKeys.contains(KeyEvent.VK_DOWN)
                || game.pressedKeys.contains(KeyEvent.VK_D) || game.pressedKeys.contains(KeyEvent.VK_A)
                || game.pressedKeys.contains(KeyEvent.VK_W) || game.pressedKeys.contains(KeyEvent.VK_S) || controllerInput;


    }

    private void toggleShadows() {

        toggleChildren(this);

        if (game.getScenePanel().getBackground() != Color.BLACK) {
            game.getScenePanel().setBackground(Color.BLACK);
            PdBase.sendBang("shadow_off");
        } else {
            game.getScenePanel().setBackground(Color.WHITE);
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
        player.setPosition(112, game.centerY-200);
    }

    private float calcDistance(Vec2 a, Vec2 b) {
        return (float)(Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y)));
    }

}