/*
 Brandon Sangston bss3cv
 CS 4730 Lab 1
 1/27/2017
 */

package edu.virginia.lab3test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.util.GameClock;
import sun.awt.image.BufferedImageDevice;

import javax.sound.midi.MidiSystem;
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

    DisplayObjectContainer SolarSystem = new DisplayObjectContainer("Solar System");
    DisplayObjectContainer Planets = new DisplayObjectContainer("Planets");
    DisplayObject Background = new DisplayObject("Background", "space_background.png");

    DisplayObjectContainer p1= new DisplayObjectContainer("p1");
    DisplayObjectContainer p2= new DisplayObjectContainer("p2");
    DisplayObjectContainer p3= new DisplayObjectContainer("p3");
    DisplayObjectContainer p4= new DisplayObjectContainer("p4");
    DisplayObjectContainer p5= new DisplayObjectContainer("p5");
    DisplayObjectContainer p6= new DisplayObjectContainer("p6");
    DisplayObjectContainer p7= new DisplayObjectContainer("p7");
    DisplayObjectContainer p8= new DisplayObjectContainer("p8");

    Sprite sun = new Sprite("Sun", "sun.png");
    Sprite mercury = new Sprite("Mercury","mercury.png");
    Sprite venus = new Sprite("Venus","venus.png");
    Sprite earth = new Sprite("Earth","earth.png");
    Sprite mars = new Sprite("Mars","mars.png");
    Sprite jupiter = new Sprite("Jupiter","jupiter.png");
    Sprite saturn = new Sprite("Saturn","saturn.png");
    Sprite uranus = new Sprite("Uranus","uranus.png");
    Sprite neptune = new Sprite("Neptune","neptune.png");

    Sprite moon = new Sprite("Moon", "moon.png");
    Sprite iaepetus = new Sprite("Iaepetus", "iaepetus.png");
    Sprite ganymede = new Sprite("Ganymede", "ganymede.png");

    int speed = 1;
    float rotSpeed = 0.05f;

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

        //Invisible "sun" for each planet to control different rotation speeds
        Planets.setPosition(centerX, centerY-sun.getScaledHeight()/2);
        Planets.centerPivot();
        Planets.addChild(p1);
        Planets.addChild(p2);
        Planets.addChild(p3);
        Planets.addChild(p4);
        Planets.addChild(p5);
        Planets.addChild(p6);
        Planets.addChild(p7);
        Planets.addChild(p8);

        //Planets
        p1.addChild(mercury);
        p2.addChild(venus);
        p3.addChild(earth);
        p4.addChild(mars);
        p5.addChild(jupiter);
        p6.addChild(saturn);
        p7.addChild(uranus);
        p8.addChild(neptune);

        //Moons
        earth.addChild(moon);
        jupiter.addChild(ganymede);
        saturn.addChild(iaepetus);


        for (DisplayObject p : Planets.getChildren()) {
            p.setPosition(sun.getPosition());
            p.setPivot(sun.getPivot());
        }

        sun.setScale(1);
        SolarSystem.setScale(0.1);
        Planets.setPosition(centerX, centerY-sun.getScaledHeight()/2);

        mercury.setPosition(centerX - sun.getScaledWidth(), centerY + sun.getScaledHeight());
        mercury.setScale(0.25);

        venus.setPosition(centerX - sun.getScaledWidth()*2, centerY - sun.getScaledHeight()*2);
        venus.setScale(0.2);

        earth.setPosition(centerX - sun.getScaledWidth()*3, centerY + sun.getScaledHeight()*3);
        earth.setScale(0.45);

        mars.setPosition(centerX + sun.getScaledWidth()*2, centerY - sun.getScaledHeight()*4);
        mars.setScale(0.2);

        jupiter.setPosition(centerX, centerY + sun.getScaledHeight()*8);
        jupiter.setScale(5);
        ganymede.setScale(0.2);
        ganymede.setPosX(ganymede.getPosX() - ganymede.getScaledWidth()/4);

        saturn.setPosition(centerX, centerY - sun.getScaledHeight()*12);
        saturn.setScale(5);
        iaepetus.setScale(0.125);
        iaepetus.setPosX(saturn.getPosX() - iaepetus.getScaledWidth()/4);

        uranus.setPosition(centerX - sun.getScaledWidth()*20, centerY);
        uranus.setScale(8);

        neptune.setPosition(centerX + sun.getScaledWidth()*25, centerY);
        neptune.setScale(3);



    }

    /**
     * Engine will automatically call this update method once per frame and pass to us
     * the set of keys (as strings) that are currently being pressed down
     */
    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);

        if (Planets != null ) Planets.setPosition(Planets.getPosX(), Planets.getPosY() + 20);

        if (pressedKeys.size() > 0) {

            //Transform
            if (pressedKeys.contains(KeyEvent.VK_LEFT)) { //move right
                setPosX(getPosX() + speed);
            }
            if (pressedKeys.contains(KeyEvent.VK_RIGHT)) { //move left
                setPosX(getPosX() - speed);
            }
            if (pressedKeys.contains(KeyEvent.VK_DOWN)) { //move up
                setPosY(getPosY() - speed);
            }
            if (pressedKeys.contains(KeyEvent.VK_UP)) { //move down
                setPosY(getPosY() + speed);

            }

            if (SolarSystem != null) {

                //Scale
                if (pressedKeys.contains(KeyEvent.VK_W)) { //scale up
                    SolarSystem.setScale(SolarSystem.getScale() * 1.01);

                }
                if (pressedKeys.contains(KeyEvent.VK_Q)) { //scale down
                    SolarSystem.setScale(SolarSystem.getScale() * 0.99);

                }

                if (pressedKeys.contains(KeyEvent.VK_S)) { //rotate counterclockwise
                    SolarSystem.setRotation(SolarSystem.getRotation() + 0.25f);

                }
                if (pressedKeys.contains(KeyEvent.VK_A)) { //rotate clockwise
                    SolarSystem.setRotation(SolarSystem.getRotation() - 0.25f);

                }
            }
            if (Background != null) {
                if (pressedKeys.contains(KeyEvent.VK_S)) { //rotate counterclockwise
                    Background.setRotation(Background.getRotation() - 0.01f);

                }
                if (pressedKeys.contains(KeyEvent.VK_A)) { //rotate clockwise
                    Background.setRotation(Background.getRotation() + 0.01f);

                }
            }

        }

        if (Planets != null ) Planets.setPosition(Planets.getPosX(), Planets.getPosY() + 20);

        if (SolarSystem != null) {
            SolarSystem.setRotation(SolarSystem.getRotation() + rotSpeed);

            if (sun != null) sun.setRotation(sun.getRotation() + rotSpeed);

            if (p1 != null) p1.setRotation(p1.getRotation() + rotSpeed * 3f);
            if (p2 != null) p2.setRotation(p2.getRotation() + rotSpeed * 2.5f);
            if (p3 != null) p3.setRotation(p3.getRotation() + rotSpeed * 2f);
            if (p4 != null) p4.setRotation(p4.getRotation() + rotSpeed * 1.25f);
            if (p5 != null) p5.setRotation(p5.getRotation() + rotSpeed * 1.15f);
            if (p6 != null) p6.setRotation(p6.getRotation() + rotSpeed * 0.9f);
            if (p7 != null) p7.setRotation(p7.getRotation() + rotSpeed * 0.75f);
            if (p8 != null) p8.setRotation(p8.getRotation() + rotSpeed * 0.5f);

            if (mercury != null) mercury.setRotation(mercury.getRotation() + rotSpeed * 5f);
            if (venus != null) venus.setRotation(venus.getRotation() + rotSpeed * 4f);
            if (earth != null) earth.setRotation(earth.getRotation() + rotSpeed * 3f);
            if (moon != null) moon.setRotation(moon.getRotation() + rotSpeed * 2f);
            if (mars != null) mars.setRotation(mars.getRotation() + rotSpeed * 1.25f);
            if (jupiter != null) jupiter.setRotation(jupiter.getRotation() + rotSpeed * 1.75f);
            if (saturn != null) saturn.setRotation(saturn.getRotation() + rotSpeed * 3f);
            if (uranus != null) uranus.setRotation(uranus.getRotation() + rotSpeed * 2.5f);
            if (neptune != null) neptune.setRotation(neptune.getRotation() + rotSpeed * 2.2f);


            //Elliptical orbits
            if (Planets != null ) Planets.setPosition(Planets.getPosX(), Planets.getPosY() - 40);


            if (Background != null) {
                Background.setRotation(Background.getRotation() + 0.0075f);
            }

            //Exit game
            if (pressedKeys.contains(KeyEvent.VK_ESCAPE)) {
                exitGame();
                closeGame();
            }

        }
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

