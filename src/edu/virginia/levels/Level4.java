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
public class Level4 extends DisplayObjectContainer {

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
    boolean platform4Up = true, platform4active = false;
    boolean platform2left = false, platform2active = false;
    boolean enemy1up = true;
    boolean enemy2up = false;
    boolean enemy3up = true;

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
    AnimatedShadowSoundSprite enemy2 = new AnimatedShadowSoundSprite("Ghost2", "shadow_boo.png",
            "shadow_boo.png", 3, 2);
    AnimatedShadowSoundSprite enemy3 = new AnimatedShadowSoundSprite("Ghost3", "shadow_boo.png",
            "shadow_boo.png", 3, 2);

    AnimatedShadowSprite pickup = new AnimatedShadowSprite("Pickup", "pickupAnim.jpg", "pickupAnim.jpg", 8, 8);

    //Sprites
    DisplayObjectContainer Platforms = new DisplayObjectContainer("Platforms");
    ShadowSprite platform1 = new ShadowSprite("Platform1", "basic_platform.png", "shadow_basic_platform.png");
    ShadowSprite platform2 = new ShadowSprite("Platform2", "basic_platform.png", "shadow_basic_platform.png");
    ShadowSprite platform3 = new ShadowSprite("Platform3",  "shadow_basic_platform.png");
    ShadowSprite platform4 = new ShadowSprite("Platform4", "basic_platform.png","shadow_basic_platform.png");
    ShadowSprite platform5 = new ShadowSprite("Platform5",  "shadow_basic_platform.png");
    ShadowSprite platform6 = new ShadowSprite("Platform6", "basic_platform.png", "shadow_basic_platform.png");
    ShadowSprite platform7 = new ShadowSprite("Platform7",  "shadow_basic_platform.png");
    ShadowSprite platform8 = new ShadowSprite("Platform8", "basic_platform.png", "shadow_basic_platform.png");
    ShadowSprite platform9 = new ShadowSprite("Platform9", "shadow_basic_platform.png");
    ShadowSprite platform10 = new ShadowSprite("Platform10", "basic_platform.png", "shadow_basic_platform.png");

    //Backgrounds
    ShadowSprite background = new ShadowSprite("Background", "background1.png", "shadow_background1_mag.png");

    ArrayList<DisplayObject> children = getChildren();

    Vec2 spawnPoint;

    SoundManager soundManager = new SoundManager();

    boolean jumpReady;
    boolean landed;
    boolean soundSpriteCollision = false;

    public Level4(Game game) {
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
        platform1.setPosition(112, game.centerY+112);
        platform2.setPosition(platform1.getPosX()+225, platform1.getPosY());
        platform3.setPosition(2*game.centerX-337, platform1.getPosY());
        platform3.setOnlyShadow(true);
        platform4.setPosition(2*game.centerX-112, platform3.getPosY());
        platform5.setPosition(platform3.getPosX(), game.centerY-225);
        platform5.setOnlyShadow(true);
        platform6.setPosition(platform5.getPosX()-225, platform5.getPosY());

        platform9.setPosition(platform2.getPosX(), platform6.getPosY());
        platform9.setOnlyShadow(true);
        platform7.setPosition(Math.abs((platform6.getPosX()-platform9.getPosX())/2)+platform9.getPosX(), platform6.getPosY());
        platform7.setOnlyShadow(true);

        platform10.setPosition(platform1.getPosX(), platform9.getPosY());


        coin.addNewAnimation("spin", new int[] {0,1,2,3,4,5,6,7,8,9});
        coin.centerPivot();
        coin.setPosition(platform10.getPosX(), platform10.getPosY() - 50);
        coin.setScale(0.25);
        coin.setAnim("spin");
        coin.playAnim();
        coin.setAnimSpeed(6);

        //Invisible enemies
        enemy1.setScale(0.25f);
        enemy1.setPosition(Math.abs((platform9.getPosX()-platform7.getPosX())/2)+platform9.getPosX(), game.centerY - 100);

        enemy2.setScale(0.25f);
        enemy2.setPosition(Math.abs((platform7.getPosX()-platform6.getPosX())/2)+platform7.getPosX(), game.centerY + 100);

        enemy3.setScale(0.25f);
        enemy3.setPosition(Math.abs((platform6.getPosX()-platform5.getPosX())/2)+platform6.getPosX(), game.centerY);

        //Construct Display Tree
        addChild(background);
        addChild(coin);
        addChild(player);
        addChild(Platforms);
        addChild(Enemies);
        Enemies.addChild(enemy1);
        Enemies.addChild(enemy2);
        Enemies.addChild(enemy3);
        Platforms.addChild(platform1);
        Platforms.addChild(platform2);
        Platforms.addChild(platform3);
        Platforms.addChild(platform4);
        Platforms.addChild(platform5);
        Platforms.addChild(platform6);
        Platforms.addChild(platform7);
        Platforms.addChild(platform9);
        Platforms.addChild(platform10);


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

        moveDown(50);

        //Objectives
        Quest q1 = new Quest("Patience", "Collect the coin. Timing is everything.");
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
            coin.dispatchEvent(new Event(Event.COIN_PICKED_UP, coin));
            questManager.completeQuest("Patience");

            if(shadow)
                toggleShadows();

            this.dispatchEvent(new Event("end_level_4", this));

        }

        //Shadow duration
        if (shadowClock <= 0 && shadow) {
            toggleShadows();
        }

        //TODO: Write methods for platform and enemy movement (ex: platform.moveWithinRange(Vec2 start, Vec2 stop)
        //Platform movement
        if (platform2 != null && platform2active) {
            if (platform2left) {
                if (platform2.getPosX() < platform1.getPosX()+225) {
                    platform2left = false;
                }
                platform2.moveRight(-speed / 4);
            } else {
                if (platform2.getPosX() + platform2.getScaledWidth() >= platform3.getPosX() - platform2.halfWidth()) {
                    platform2left = true;
                }
                platform2.moveRight(speed / 4);
            }
        }
        if (platform4 != null && platform4active) {
            if (platform4Up) {
                if (platform4.getPosY() < game.centerY-225) {
                    platform4Up = false;
                }
                platform4.moveUp(speed/4);
            } else {
                if (platform4.getPosY() >= platform3.getPosY()) {
                    platform4Up = true;
                }
                platform4.moveDown(speed/4);
            }
        }

        //Enemy movement
        if (enemy1 != null) {
            float startPoint = game.centerY - 350;
            if (enemy1up) {
                if (enemy1.getPosY() < startPoint) {
                    enemy1up = false;
                }
                enemy1.moveUp(speed/3);
            } else {
                if (enemy1.getPosY() - enemy1.getScaledHeight() >= startPoint + 650) {
                    enemy1up = true;
                }
                enemy1.moveDown(speed/3);
            }
        }
        if (enemy2 != null) {
            float startPoint = game.centerY - 350;
            if (enemy2up) {
                if (enemy2.getPosY() < startPoint) {
                    enemy2up = false;
                }
                enemy2.moveUp(speed/3);
            } else {
                if (enemy2.getPosY() - enemy2.getScaledHeight() >= startPoint + 650) {
                    enemy2up = true;
                }
                enemy2.moveDown(speed/3);
            }
        }
        if (enemy3 != null) {
            float startPoint = game.centerY - 350;
            if (enemy3up) {
                if (enemy3.getPosY() < startPoint) {
                    enemy3up = false;
                }
                enemy3.moveUp(speed/3);
            } else {
                if (enemy3.getPosY() - enemy3.getScaledHeight() >= startPoint + 650) {
                    enemy3up = true;
                }
                enemy3.moveDown(speed/3);
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

                    if (platform.getId().equalsIgnoreCase("Platform2")) {
                        platform2active = true;
                        if (platform2left) {
                            player.translateLeft(speed/4);
                        } else {
                            player.translateRight(speed/4);
                        }
                    }

                    if (platform.getId().equalsIgnoreCase("Platform4")) {
                        platform4active = true;

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
            g.drawString("Level 4: " + questManager.getActiveQuest().getId(), 15, 20); //25
            g.drawString("Objective: " + questManager.getActiveQuest().getObjective(), 15, 40); //50

            if (questManager.getActiveQuest().isComplete()) {
                g.setFont(f);
                g.drawString("Level 4 Completed!", game.centerX-120, game.centerY-250);
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