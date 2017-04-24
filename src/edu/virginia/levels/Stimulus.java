package edu.virginia.levels;

import edu.virginia.engine.controller.GamePad;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.events.LevelEventManager;
import edu.virginia.engine.events.QuestManager;

import java.awt.*;
import java.util.ArrayList;

public class Stimulus extends Game{

    LevelEventManager levelEventManager;

    StartScreen home = new StartScreen(this, 1);
    Level1 l1 = new Level1(this);
    Level2 l2 = new Level2(this);
    Level3 l3 = new Level3(this);
    Level4 l4 = new Level4(this);
    Level5 l5 = new Level5(this);

    boolean startScreen = true;
    int currentLevel = 1;

    public Stimulus() {
        super("Stimulus", 1400, 850);

        levelEventManager = new LevelEventManager(this);

        home.addEventListener(levelEventManager, "start_level_1");
        home.addEventListener(levelEventManager, "start_level_2");
        home.addEventListener(levelEventManager, "start_level_3");
        home.addEventListener(levelEventManager, "start_level_4");
        home.addEventListener(levelEventManager, "start_level_5");

        l1.addEventListener(levelEventManager, "end_level_1");
        l2.addEventListener(levelEventManager, "end_level_2");
        l3.addEventListener(levelEventManager, "end_level_3");
        l4.addEventListener(levelEventManager, "end_level_4");
        l5.addEventListener(levelEventManager, "end_level_5");

        this.addChild(home);

    }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<GamePad> gamePads) {
        super.update(pressedKeys, gamePads);


    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
    }

    public static void main(String[] args){
        Stimulus game = new Stimulus();
        game.start();
    }

    public Level1 getL1() {
        return l1;
    }

    public Level2 getL2() {
        return l2;
    }

    public Level3 getL3() {
        return l3;
    }

    public Level4 getL4() {
        return l4;
    }

    public Level5 getL5() {
        return l5;
    }

    public StartScreen getStartScreen(){
        return home;
    }
}
