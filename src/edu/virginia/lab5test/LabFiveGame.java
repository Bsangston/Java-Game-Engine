package edu.virginia.lab5test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.events.*;
import edu.virginia.engine.events.Event;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by BrandonSangston on 2/17/17.
 */
public class LabFiveGame extends Game {

    int speed = 12;

    QuestManager questManager = new QuestManager();

    DisplayObjectContainer GameWorld = new DisplayObjectContainer("GameWorld");

    AnimatedSprite mario = new AnimatedSprite("Mario", "bigmario_sprites.png", 4, 2);
    PickUp coin = new PickUp("Gold", "coin.png", 10, 1);

    DisplayObjectContainer Platforms = new DisplayObjectContainer("Platforms");
    Sprite platform1 = new Sprite("Platform1", "platform.png");
    Sprite platform2 = new Sprite("Platform2", "platform.png");
    Sprite platform3 = new Sprite("Platform3", "platform.png");

    ArrayList<DisplayObject> children = getChildren();



    public LabFiveGame() {
        super("Lab Four Test Game", 1000, 600);

        mario.addNewAnimation("idle", new int[] {0});
        mario.addNewAnimation("run", new int[] {1,2,3,4});
        mario.centerPivot();
        mario.setPosition(centerX-250, centerY);
        mario.setScale(0.5);
        mario.setAnim("idle");
        mario.playAnim();
        mario.setAnimSpeed(speed/2);

        //Physics
        mario.addRigidBody2D();

        coin.addNewAnimation("spin", new int[] {0,1,2,3,4,5,6,7,8,9});
        coin.centerPivot();
        coin.setPosition(centerX+250, centerY);
        coin.setScale(0.5);
        coin.setAnim("spin");
        coin.playAnim();
        coin.setAnimSpeed(speed/2);

        platform1.setPosition(mario.getPosX(), mario.getPosY()+225);
        platform1.setScale(0.25);
        platform1.addRigidBody2D();
        platform1.getRigidBody().toggleGravity(false);

        platform2.setPosition(mario.getPosX()+250, mario.getPosY());
        platform2.setScale(0.2);
        platform2.addRigidBody2D();
        platform2.getRigidBody().toggleGravity(false);

        platform3.setPosition(coin.getPosX(), coin.getPosY()+100);
        platform3.setScale(0.225);
        platform3.addRigidBody2D();
        platform3.getRigidBody().toggleGravity(false);

        GameWorld.addChild(coin);
        GameWorld.addChild(mario);
        GameWorld.addChild(Platforms);
        Platforms.addChild(platform1);
        //Platforms.addChild(platform2);
        //Platforms.addChild(platform3);

        Quest q1 = new Quest("Games are Fun", "Collect the coin");
        questManager.setActiveQuest(q1);

        coin.addEventListener(questManager, Event.COIN_PICKED_UP);
        mario.addEventListener(this, Event.COLLISION);
    }

    /**
     * Engine will automatically call this update method once per frame and pass to us
     * the set of keys (as keycode ints) that are currently being pressed down
     */
    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);
        GameWorld.update(pressedKeys);

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
            if (pressedKeys.contains(KeyEvent.VK_UP) && inBoundsTop(mario)) { //move up
                mario.setPosY(mario.getPosY() - speed);
            }
            if (pressedKeys.contains(KeyEvent.VK_DOWN) && inBoundsBottom(mario)) { //move down
                mario.setPosY(mario.getPosY() + speed);

            }

            if (pressedKeys.contains(KeyEvent.VK_SPACE)) {
                if (mario != null && platform1 != null) {
                    System.out.println("Mario " +mario.getHitbox());
                    System.out.println("Platform "+platform1.getHitbox());
                }
            }

            if (isMoving()) {
                mario.setAnim("run");

            }
        }

        //Idle animation
        if (mario != null && pressedKeys.isEmpty()) {
            mario.setAnim("idle");
        }

        if (mario != null && coin != null && mario.onTriggerEnter(coin)) {
            coin.dispatchEvent(new Event(Event.COIN_PICKED_UP, coin));
            questManager.completeQuest("Games are Fun");

        }


        if (mario != null && platform1 != null) {
            if (mario.collidesWith(platform1)) {
                mario.dispatchEvent(new Collision(Collision.GROUND, mario, platform1));
            }
        }

        //Exit game
        if (pressedKeys.contains(KeyEvent.VK_ESCAPE)) {
            exitGame();
            closeGame();
        }


        mouseEvents.clear();
    }

    /**
     * Engine automatically invokes draw() every frame as well. If we want to make sure mario gets drawn to
     * the screen, we need to make sure to override this method and call mario's draw method.
     */
    @Override
    public void draw(Graphics g) {
        super.draw(g);
        GameWorld.draw(g);

        Font f = new Font("GUI", Font.ITALIC, 20);

        g.drawString("Current Quest: "+questManager.getActiveQuest().getId(), 25, 25);
        g.drawString("Objective: "+questManager.getActiveQuest().getObjective(), 25, 50);

        if (questManager.getActiveQuest().isComplete()) {
            g.setFont(f);
            g.drawString("\""+questManager.getActiveQuest().getId()+"\" Completed!", centerX-135, centerY-200);
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
        LabFiveGame game = new LabFiveGame();
        game.start();

    }

}
