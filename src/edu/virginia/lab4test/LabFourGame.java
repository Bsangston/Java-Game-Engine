package edu.virginia.lab4test;

import edu.virginia.engine.controller.GamePad;
import edu.virginia.engine.display.*;
import edu.virginia.engine.events.*;
import edu.virginia.engine.events.Event;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by BrandonSangston on 2/17/17.
 */
public class LabFourGame extends Game {

    int speed = 12;

    QuestManager questManager = new QuestManager();

    DisplayObjectContainer GameWorld = new DisplayObjectContainer("GameWorld");

    AnimatedSprite mario = new AnimatedSprite("Mario", "bigmario_sprites.png", 4, 2);
    PickUp coin = new PickUp("Gold", "coin.png", 10, 1);

    public LabFourGame() {
        super("Lab Four Test Game", 1000, 600);

        mario.addNewAnimation("idle", new int[] {0});
        mario.addNewAnimation("run", new int[] {1,2,3,4});
        mario.centerPivot();
        mario.setPosition(centerX-250, centerY);
        mario.setScaleX(0.5);
        mario.setScaleY(0.5);
        mario.setAnim("idle");
        mario.playAnim();
        mario.setAnimSpeed(speed/2);

        coin.addNewAnimation("spin", new int[] {0,1,2,3,4,5,6,7,8,9});
        coin.centerPivot();
        coin.setPosition(centerX+250, centerY);
        coin.setScale(0.5);
        coin.setAnim("spin");
        coin.playAnim();
        coin.setAnimSpeed(speed/2);

        GameWorld.addChild(coin);
        GameWorld.addChild(mario);

        Quest q1 = new Quest("Games are Fun", "Collect the coin");
        questManager.setActiveQuest(q1);

        coin.addEventListener(questManager, Event.COIN_PICKED_UP);
    }

    /**
     * Engine will automatically call this update method once per frame and pass to us
     * the set of keys (as keycode ints) that are currently being pressed down
     */
    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<GamePad> gamePads) {
        super.update(pressedKeys, gamePads);
        GameWorld.update(pressedKeys, gamePads);

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
        return (!s.isFacingRight() && s.getPosX() >= -s.getScaledWidth()/2) ||
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
        LabFourGame game = new LabFourGame();
        game.start();

    }

}
