/*
 Brandon Sangston bss3cv
 CS 4730 Lab 1
 1/27/2017
 */

package edu.virginia.lab3test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.util.GameClock;

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

    DisplayObjectContainer GameWorld = new DisplayObjectContainer("Game World");
    DisplayObjectContainer SolarSystem = new DisplayObjectContainer("Solar System");

    Sprite[] planets;

    /**
     * Constructor. See constructor in Game.java for details on the parameters given
     */
    public LabThreeGame() {
        super("Lab One Test Game", 1000, 600);

        Sprite.loadSpriteSheet(planetSprites, 5, 2);

        Sprite sun = new Sprite("Sun", "SUN.png");
        Sprite mercury = new Sprite("Mercury", 5);
        Sprite venus = new Sprite("Venus", 1);
        Sprite earth = new Sprite("Earth", 0);
        Sprite mars = new Sprite("Mars", 4);
        Sprite jupiter = new Sprite("Jupiter", 3);
        Sprite saturn = new Sprite("Saturn", 7);
        Sprite uranus = new Sprite("Uranus", 6);
        Sprite neptune = new Sprite("Neptune", 8);
        planets = new Sprite[]{sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune};

    }

    /**
     * Engine will automatically call this update method once per frame and pass to us
     * the set of keys (as strings) that are currently being pressed down
     */
    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);

        if (GameWorld != null) {
            GameWorld.update(pressedKeys);


        }

        //Exit game
        if (pressedKeys.contains(KeyEvent.VK_ESCAPE)) {
            exitGame();
            closeGame();
        }

    }

    /**
     * Engine automatically invokes draw() every frame as well. If we want to make sure mario gets drawn to
     * the screen, we need to make sure to override this method and call mario's draw method.
     */
    @Override
    public void draw(Graphics g) {
        super.draw(g);

		/* Same, just check for null in case a frame gets thrown in before Mario is initialized */
        if (GameWorld != null) {
            GameWorld.draw(g);
        }



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

