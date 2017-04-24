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
 * Created by BrandonSangston on 2/17/17.
 * Matthew Leon
 * Cole Schafer
 */
public class Level5 extends DisplayObjectContainer {

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
    boolean enemy2left = true;
    boolean platform6Up = true;
    boolean platform7active = false, platform7left = true;
    boolean enemy3up = false, enemy4up = true, enemy5up = false;
    boolean platform9active = false, platform9up = true;

    //Util
    ArrayList<Integer> lastKeyPressed = new ArrayList<>();
    boolean shadowToggled = false;

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
    AnimatedShadowSoundSprite enemy4 = new AnimatedShadowSoundSprite("Ghost4", "shadow_boo.png",
            "shadow_boo.png", 3, 2);
    AnimatedShadowSoundSprite enemy5 = new AnimatedShadowSoundSprite("Ghost5", "shadow_boo.png",
            "shadow_boo.png", 3, 2);

    AnimatedShadowSprite pickup = new AnimatedShadowSprite("Pickup", "pickupAnim.jpg", "pickupAnim.jpg", 8, 8);

    //Sprites
    DisplayObjectContainer Platforms = new DisplayObjectContainer("Platforms");
    ShadowSprite platform1 = new ShadowSprite("Platform1", "basic_platform.png", "shadow_basic_platform.png");
    ShadowSprite platform2 = new ShadowSprite("Platform2", "basic_platform.png", "shadow_basic_platform.png");
    ShadowSprite platform3 = new ShadowSprite("Platform3", "shadow_basic_platform.png");
    ShadowSprite platform4 = new ShadowSprite("Platform4", "basic_platform.png", "shadow_basic_platform.png");
    ShadowSprite platform5 = new ShadowSprite("Platform5", "shadow_basic_platform.png");
    ShadowSprite platform6 = new ShadowSprite("Platform6", "basic_platform.png", "shadow_basic_platform.png");
    ShadowSprite platform7 = new ShadowSprite("Platform7", "basic_platform.png","shadow_basic_platform.png");
    ShadowSprite platform8 = new ShadowSprite("Platform8",  "basic_platform.png","shadow_basic_platform.png");
    ShadowSprite platform9 = new ShadowSprite("Platform9", "basic_platform.png","shadow_basic_platform.png");
    ShadowSprite platform10 = new ShadowSprite("Platform10", "basic_platform.png","shadow_basic_platform.png");
    ShadowSprite platform11 = new ShadowSprite("Platform11", "basic_platform.png","shadow_basic_platform.png");
    ShadowSprite platform12 = new ShadowSprite("Platform12", "basic_platform.png","shadow_basic_platform.png");
    ShadowSprite platform13 = new ShadowSprite("Platform13", "basic_platform.png","shadow_basic_platform.png");
    ShadowSprite platform14 = new ShadowSprite("Platform14", "shadow_basic_platform.png");
    ShadowSprite platform15 = new ShadowSprite("Platform15", "basic_platform.png","shadow_basic_platform.png");
    ShadowSprite platform16 = new ShadowSprite("Platform16", "shadow_basic_platform.png");
    ShadowSprite platform17 = new ShadowSprite("Platform17", "basic_platform.png","shadow_basic_platform.png");

    //Backgrounds
    ShadowSprite background = new ShadowSprite("Background", "background1.png", "shadow_background1_mag.png");
    ArrayList<DisplayObject> children = getChildren();

    Vec2 spawnPoint;

    SoundManager soundManager = new SoundManager();

    boolean soundSpriteCollision = false;

    boolean jumpReady;
    boolean landed;

    public Level5(Game game) {
        super("Stimulus Beta");

        this.game = game;

        spawnPoint = new Vec2(100, game.centerY + 150);

        background.setPosition(game.center);

        //Initialize player parameters
        player.addNewAnimation(AnimatedSprite.IDLE, new int[] {0});
        player.addNewAnimation(AnimatedSprite.RUN, new int[] {1,2,3,4});
        player.addNewAnimation(AnimatedSprite.JUMP, new int[] {4});
        player.centerPivot();
        player.setScale(0.2f);
        player.setPosition(100, game.centerY + 150);
        player.setAnim(AnimatedSprite.IDLE);
        player.setAnimSpeed(speed);
        player.playAnim();
        player.addRigidBody2D();

        //spawnPoint = new Vec2(100, centerY + 150);

        //Initialize game object parameters
        platform1.setPosition((int)spawnPoint.x, (int)spawnPoint.y + 200);

        platform2.setPosition(player.getPosX()+200, 650);

        platform3.setPosition(platform2.getPosX()+200, 650);
        platform3.setOnlyShadow(true);

        platform4.setPosition(platform3.getPosX()+200, 650);

        platform5.setPosition(platform4.getPosX()+200, 650);
        platform5.setOnlyShadow(true);

        platform8.setPosition(platform5.getPosX()+200, 650);

        platform6.setPosition(platform8.getPosX(), 500);

        platform7.setPosition(platform6.getPosX()-200, game.centerY-100);

        platform9.setPosition(platform1.getPosX(), game.centerY-100);

        platform10.setPosition(platform9.getPosX()+150, 150);

        platform11.setPosition(platform10.getPosX()+150, 100);

        platform11.setPosition(platform10.getPosX()+150, 100);

        platform12.setPosition(platform11.getPosX()+150, 150);

        platform13.setPosition(platform12.getPosX()+150, 100);

        platform14.setPosition(platform13.getPosX()+150, 100);
        platform14.setOnlyShadow(true);

        platform15.setPosition(platform14.getPosX()+150, 100);

        platform16.setPosition(platform15.getPosX()+150, 100);
        platform16.setOnlyShadow(true);

        platform17.setPosition(platform16.getPosX()+150, 100);


        //Invisible enemies
        enemy1.setScale(0.5f);
        enemy1.setPosition(platform8.getPosX(), platform8.getPosY() - 100);
        enemy1.setOnlyShadow(true);
        enemy1.addNewAnimation(AnimatedSprite.IDLE, new int[] {0, 1, 2, 4, 5});
        enemy1.setAnim(AnimatedSprite.IDLE);
        enemy1.playAnim();
        enemy1.setAnimSpeed(24);

        enemy2.setScale(0.4f);
        enemy2.setPosition(game.centerX*2 + enemy2.getScaledWidth(), game.centerY*2 - 150);
        enemy2.setOnlyShadow(true);
        enemy2.addNewAnimation(AnimatedSprite.IDLE, new int[] {0, 1, 2, 4, 5});
        enemy2.setAnim(AnimatedSprite.IDLE);
        enemy2.playAnim();
        enemy2.setAnimSpeed(24);

        enemy3.setScale(0.25f);
        enemy3.setPosition(platform2.getPosX()+50, game.centerY - 400);
        enemy3.setOnlyShadow(true);
        enemy3.addNewAnimation(AnimatedSprite.IDLE, new int[] {0, 1, 2, 4, 5});
        enemy3.setAnim(AnimatedSprite.IDLE);
        enemy3.playAnim();
        enemy3.setAnimSpeed(24);

        enemy4.setScale(0.25f);
        enemy4.setPosition(platform3.getPosX()+50, platform7.getPosY());
        enemy4.setOnlyShadow(true);
        enemy4.addNewAnimation(AnimatedSprite.IDLE, new int[] {0, 1, 2, 4, 5});
        enemy4.setAnim(AnimatedSprite.IDLE);
        enemy4.playAnim();
        enemy4.setAnimSpeed(24);

        enemy5.setScale(0.25f);
        enemy5.setPosition(platform4.getPosX()+50, game.centerY - 400);
        enemy5.setOnlyShadow(true);
        enemy5.addNewAnimation(AnimatedSprite.IDLE, new int[] {0, 1, 2, 4, 5});
        enemy5.setAnim(AnimatedSprite.IDLE);
        enemy5.playAnim();
        enemy5.setAnimSpeed(24);

        coin.addNewAnimation("spin", new int[] {0,1,2,3,4,5,6,7,8,9});
        coin.centerPivot();
        coin.setPosition(platform17.getPosX(), platform17.getPosY() - 50);
        coin.setScale(0.25);
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
        addChild(coin);
        //addChild(pickup);
        addChild(player);
        addChild(Platforms);
        addChild(Enemies);
        Enemies.addChild(enemy1);
        Enemies.addChild(enemy2);
        Enemies.addChild(enemy3);
        Enemies.addChild(enemy4);
        Enemies.addChild(enemy5);
        Platforms.addChild(platform1);
        Platforms.addChild(platform2);
        Platforms.addChild(platform3);
        Platforms.addChild(platform4);
        Platforms.addChild(platform5);
        Platforms.addChild(platform6);
        Platforms.addChild(platform7);
        Platforms.addChild(platform8);
        Platforms.addChild(platform9);
        Platforms.addChild(platform10);
        Platforms.addChild(platform11);
        Platforms.addChild(platform12);
        Platforms.addChild(platform13);
        Platforms.addChild(platform14);
        Platforms.addChild(platform15);
        Platforms.addChild(platform16);
        Platforms.addChild(platform17);

        for (DisplayObject platform : Platforms.getChildren()) {
            platform.setScale(0.175f);
            platform.addRigidBody2D();
            platform.getRigidBody().toggleGravity(false);
        }

        moveDown(50);

        //Objectives
        Quest q1 = new Quest("The Gauntlet", "Collect the coin. Good luck.");
        questManager.setActiveQuest(q1);

        //Event Listeners
        coin.addEventListener(questManager, Event.COIN_PICKED_UP);
        player.addEventListener(this, Event.COLLISION);

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
            questManager.completeQuest("The Gauntlet");

            if(shadow)
                toggleShadows();

            this.dispatchEvent(new Event("end_level_5", this));

        }

        //Shadow duration
        if (shadowClock <= 0 && shadow) {
            toggleShadows();
        }


        //TODO: Write methods for platform and enemy movement (ex: platform.moveWithinRange(Vec2 start, Vec2 stop)
        //Platform movement
        if (platform6 != null) {
            if (platform6Up) {
                if (platform6.getPosY() < game.centerY-100) {
                    platform6Up = false;
                }
                platform6.moveUp(speed/4);
            } else {
                if (platform6.getPosY() - platform6.getScaledHeight() >= game.centerY+50) {
                    platform6Up = true;
                }
                platform6.moveDown(speed/4);
            }
        }

        if (platform7 != null && platform7active) {
            if (platform7left) {
                if (platform7.getPosX() < 250) {
                    platform7left = false;
                }
                platform7.moveRight(-speed / 4);
            } else {
                if (platform7.getPosX() + platform7.getScaledWidth() >= platform6.getPosX() - platform7.halfWidth()) {
                    platform7left = true;
                }
                platform7.moveRight(speed / 4);
            }
        }

        if (platform9 != null && platform9active) {
            if (platform9up) {
                if (platform9.getPosY() < 150) {
                    platform9up = false;
                }
                platform9.moveUp(speed/4);
            } else {
                if (platform9.getPosY() - platform9.getScaledHeight()>= game.centerY-100) {
                    platform9up = true;
                }
                platform9.moveDown(speed/4);
            }
        }

        //Enemy movement
        if (enemy2 != null) {
            if (enemy2left) {
                if (enemy2.getPosX() < platform2.getPosX()) {
                    enemy2left = false;
                }
                enemy2.moveRight(-speed/2);
            } else {
                if (enemy2.getPosX() + enemy2.getScaledWidth() >= 2*game.centerX - enemy2.halfWidth()) {
                    enemy2left = true;
                }
                enemy2.moveLeft(-speed/2);
            }

        }

        if (enemy3 != null) {
            float startPoint = game.centerY - 350;
            if (enemy3up) {
                if (enemy3.getPosY() < startPoint) {
                    enemy3up = false;
                }
                enemy3.moveUp(speed/4);
            } else {
                if (enemy3.getPosY() - enemy3.getScaledHeight() >= startPoint + 300) {
                    enemy3up = true;
                }
                enemy3.moveDown(speed/4);
            }
        }

        if (enemy4 != null) {
            float startPoint = game.centerY - 350;
            if (enemy4up) {
                if (enemy4.getPosY() < startPoint) {
                    enemy4up = false;
                }
                enemy4.moveUp(speed/4);
            } else {
                if (enemy4.getPosY() - enemy4.getScaledHeight() >= startPoint + 300) {
                    enemy4up = true;
                }
                enemy4.moveDown(speed/4);
            }
        }

        if (enemy5 != null) {
            float startPoint = game.centerY - 350;
            if (enemy5up) {
                if (enemy5.getPosY() < startPoint) {
                    enemy5up = false;
                }
                enemy5.moveUp(speed/4);
            } else {
                if (enemy5.getPosY() - enemy5.getScaledHeight() >= startPoint + 300) {
                    enemy5up = true;
                }
                enemy5.moveDown(speed/4);
            }
        }

        //TODO: make more efficient -will get really slow with lots of objects (implement collision grid?)
        if (player != null) {
            for (DisplayObject platform : Platforms.getChildren()) {
                if (player.collidesWith(platform)) {

                    player.dispatchEvent(new Collision(Collision.GROUND, player, platform));

                    if (platform.getId().equalsIgnoreCase("Platform7")) {
                        platform7active = true;
                        if (platform7left) {
                            player.translateLeft(speed/4);
                        } else {
                            player.translateRight(speed/4);
                        }
                    }

                    if (platform.getId().equalsIgnoreCase("Platform9")) {
                        platform9active = true;

                    }


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

            for (DisplayObject enemy : Enemies.getChildren()) {
                if (player.collidesWith(enemy)) {
                    player.dispatchEvent(new Collision(Collision.ENEMY, player, enemy));
                    player.setPosition(100, game.centerY + 150);
                    soundSpriteCollision = true;
                }
            }

            //Respawn
            if (player.getPosY() >= 1500) {
                player.setPosition(100, game.centerY + 150);
                soundSpriteCollision = false;
            }
        }

        if(landed && !jumpReady && !pressedKeys.contains(KeyEvent.VK_SPACE)){
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
            g.drawString("Level 5: " + questManager.getActiveQuest().getId(), 15, 20); //25
            g.drawString("Objective: " + questManager.getActiveQuest().getObjective(), 15, 40); //50

            if (questManager.getActiveQuest().isComplete()) {
                g.setFont(f);
                g.drawString("Level 5 Completed!", game.centerX-120, game.centerY);
            }
        }

        if(soundSpriteCollision){
            g.setFont(f);
            g.drawString("You collided with the deathly sound sprite, restart!", game.centerX-250, game.centerY-300);
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
            //PdBase.sendBang("shadow_off");
        } else {
            game.getScenePanel().setBackground(Color.WHITE);
            //PdBase.sendBang("shadow_on");
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
}