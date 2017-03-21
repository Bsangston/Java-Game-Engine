package edu.virginia.lab6test;

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
 */
public class LabSixGame extends Game {

    int speed = 12;
    int frameClock = 0;
    int timeRemaining = 60, prevTime = 0;
    int jmpCount = 15, jmp = 0;
    GameClock gameClock = new GameClock();
    Timer timer = new Timer();

    QuestManager questManager = new QuestManager();
    DisplayObjectContainer GameWorld = new DisplayObjectContainer("GameWorld");

    AnimatedSprite mario = new AnimatedSprite("Mario", "bigmario_sprites.png", 4, 2);
    PickUp coin = new PickUp("Gold", "coin.png", 10, 1);

    Tween coinTween = new Tween(coin);

    DisplayObjectContainer Platforms = new DisplayObjectContainer("Platforms");
    Sprite platform1 = new Sprite("Platform1", "platform.png");
    Sprite platform2 = new Sprite("Platform2", "platform.png");
    Sprite platform3 = new Sprite("Platform3", "platform.png");
    Sprite platform4 = new Sprite("Platform4", "platform.png");
    Sprite platform5 = new Sprite("Platform5", "platform.png");

    ArrayList<DisplayObject> children = getChildren();

    SoundManager soundManager = new SoundManager();


    public LabSixGame() {
        super("Lab Six Test Game", 1200, 800);

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

        //coinTween = new Tween(coin);
        coinTween.addEventListener(new EventListener(), Event.TWEEN_START);
        coinTween.addEventListener(new EventListener(), Event.TWEEN_TICK);
        coinTween.addEventListener(new EventListener(), Event.TWEEN_END);

        coinTween.animate(TweenableParam.ROTATION, 0, 360, 3000);
        //TweenJuggler.add(coinTween);
    }

    /**
     * Engine will automatically call this update method once per frame and pass to us
     * the set of keys (as keycode ints) that are currently being pressed down
     */
    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);

//        TweenJuggler.tweens.element().update();
//
//        if (TweenJuggler.tweens.element().isComplete()) {
//            TweenJuggler.tweens.remove();
//        }

        if (coinTween != null) {
            coinTween.update();
        }


        if (pressedKeys.size() > 0) {

            //Movement TODO: fix rotation hitboxes
            if (pressedKeys.contains(KeyEvent.VK_RIGHT) && inBoundsRight(mario)) { //move right
                if (!mario.isFacingRight()) {
                    mario.flip();
                }
                mario.setPosX(mario.getPosX() + speed);
            }
            if (pressedKeys.contains(KeyEvent.VK_LEFT) && inBoundsLeft(mario)) { //move left
                if (mario.isFacingRight()) {
                    mario.flip();
                }
                mario.setPosX(mario.getPosX() - speed);
            }
            if (pressedKeys.contains(KeyEvent.VK_DOWN) && inBoundsBottom(mario)) { //move down
                mario.setPosY(mario.getPosY() + speed);

            }

            if (pressedKeys.contains(KeyEvent.VK_SPACE)) {
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
        }

        //Idle animation
        if (mario != null && pressedKeys.isEmpty()) {
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

        g.drawString("Current Quest: "+questManager.getActiveQuest().getId(), 25, 25);
        g.drawString("Objective: "+questManager.getActiveQuest().getObjective(), 25, 50);

        if (questManager.getActiveQuest().isComplete()) {
            g.setFont(f);
            g.drawString("\""+questManager.getActiveQuest().getId()+"\" Completed!", centerX-135, centerY);
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
        return (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_LEFT)
                || pressedKeys.contains(KeyEvent.VK_UP) || pressedKeys.contains(KeyEvent.VK_DOWN));

    }

    /**
     * Quick main class that simply creates an instance of our game and starts the timer
     * that calls update() and draw() every frame
     */
    public static void main(String[] args) {
        LabSixGame game = new LabSixGame();
        game.start();

    }

}
