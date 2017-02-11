/*
 Brandon Sangston bss3cv
 CS 4730 Lab 1
 1/27/2017
 */

package edu.virginia.lab3test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.util.GameClock;
import sun.awt.image.BufferedImageDevice;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by BrandonSangston on 2/8/17.
 */

/**
 * Example game that utilizes our engine. We can create a simple prototype game with just a couple lines of code
 * although, for now, it won't be a very fun game :)
 * */
public class LabThreeGame extends Game {

    BufferedImage planetSprites = readImage("PLANETS.png");

    DisplayObjectContainer SolarSystem = new DisplayObjectContainer("Solar System");
    DisplayObjectContainer Planets = new DisplayObjectContainer("Planets");
    DisplayObject Background = new DisplayObject("Background", "space_background.png");

    Sprite sun = new Sprite("Sun", "sun.png");
    Sprite mercury = new Sprite("Mercury","mercury.png");
    Sprite venus = new Sprite("Venus","venus.png");
    Sprite earth = new Sprite("Earth","earth.png");
    Sprite mars = new Sprite("Mars","mars.png");
    Sprite jupiter = new Sprite("Jupiter","jupiter.png");
    Sprite saturn = new Sprite("Saturn","saturn.png");
    Sprite uranus = new Sprite("Uranus","uranus.png");
    Sprite neptune = new Sprite("Neptune","neptune.png");

    Sprite[] planets;

    int speed = 1;
    float rotSpeed = 0.1f;
    int frameClock = 0;

    Point center = new Point(getMainFrame().getWidth()/2, getMainFrame().getHeight()/2);
    int centerX = (int)center.getX();
    int centerY = (int)center.getY();


    /**
     * Constructor. See constructor in Game.java for details on the parameters given
     */
    public LabThreeGame() {
        super("Lab One Test Game", 800, 800);

        Background.centerPivot();
        Background.setRotation(180);
        Background.setScaleX(1.5); Background.setScaleY(1.5);

        //Scale and Position Sun
        SolarSystem.centerPivot();
        SolarSystem.setPosition(centerX - SolarSystem.getPivotX(), centerY - SolarSystem.getPivotY());

        //Create Display Tree Hierarchy
        addChild(Background);
        addChild(SolarSystem);

        SolarSystem.addChild(sun);
        SolarSystem.addChild(Planets);

        Planets.addChild(mercury);
        Planets.addChild(venus);
        Planets.addChild(earth);
        Planets.addChild(mars);
        Planets.addChild(jupiter);
        Planets.addChild(saturn);
        Planets.addChild(uranus);
        Planets.addChild(neptune);

        sun.setScale(1);
        Point oldPos = sun.getPosition();
        SolarSystem.setScale(0.075);
        sun.setPosition(oldPos);

        System.out.println(SolarSystem.getGlobalPosition());
        System.out.println(sun.getGlobalPosition());

        planets = new Sprite[]{mercury, venus, earth, mars, jupiter, saturn, uranus, neptune};

        mercury.setPosX(-100);
        mercury.setScale(0.25);

        venus.setPosX(1000);
        venus.setScale(0.25);

        earth.setPosition(-1200, 1200);
        earth.setScale(0.45);

        mars.setPosition(1500, -1500);
        mars.setScale(0.15);

        jupiter.setPosition(1750, 2500);
        jupiter.setScale(4);

        saturn.setPosition(centerX-500, -4000);
        saturn.setScale(3);

        uranus.setPosition(centerX-2000, -4500);
        uranus.setScale(3);

        neptune.setPosition(-5000, 0);
        neptune.setScale(1.5);



    }

    /**
     * Engine will automatically call this update method once per frame and pass to us
     * the set of keys (as strings) that are currently being pressed down
     */
    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);

        if (pressedKeys.size() > 0) {

            if (SolarSystem != null) {
                //Scale
                if (pressedKeys.contains(KeyEvent.VK_W)) { //scale up
                        SolarSystem.setScale(SolarSystem.getScale() * 1.01);

                }
                if (pressedKeys.contains(KeyEvent.VK_Q)) { //scale down
                        SolarSystem.setScale(SolarSystem.getScale() * 0.99);

                }

            }
        }

        if (SolarSystem != null) {
            SolarSystem.setRotation(SolarSystem.getRotation() + rotSpeed);
            sun.setRotation(sun.getRotation() + rotSpeed);
            mercury.setRotation(mercury.getRotation() + rotSpeed*3f);
            venus.setRotation(venus.getRotation() + rotSpeed*2);
            mars.setRotation(mars.getRotation() + rotSpeed*2.25f);
            earth.setRotation(earth.getRotation() + rotSpeed*3f);
            jupiter.setRotation(jupiter.getRotation() + rotSpeed*2f);
            saturn.setRotation(saturn.getRotation() + rotSpeed*3f);
            uranus.setRotation(uranus.getRotation() + rotSpeed*4f);
            neptune.setRotation(neptune.getRotation() + rotSpeed*2f);


            //TODO: fix elliptical & different rotation speeds
//            speed = 1;
//            for (Sprite p : planets) {
//                p.setPosition(p.getPosX() + 1/speed, p.getPosY() + 1/speed);
//                speed++;
//            }
//            speed = 1;

        }

        if (Background != null) {
            Background.setRotation(Background.getRotation() + 0.0075f);
        }

            //Exit game
        if (pressedKeys.contains(KeyEvent.VK_ESCAPE)) {
            exitGame();
            closeGame();
        }

        frameClock++;
    }

    /**
     * Engine automatically invokes draw() every frame as well. If we want to make sure sun gets drawn to
     * the screen, we need to make sure to override this method and call sun's draw method.
     */
    @Override
    public void draw(Graphics g) {
        super.draw(g);

    }

    /**
     * Quick main class that simply creates an instance of our game and starts the timer
     * that calls update() and draw() every frame
     */
    public static void main(String[] args) {
        LabThreeGame game = new LabThreeGame();
        game.start();

    }


}

