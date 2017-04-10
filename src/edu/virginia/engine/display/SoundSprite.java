package edu.virginia.engine.display;


import edu.virginia.engine.cole_tween.*;
import edu.virginia.engine.controller.GamePad;
import edu.virginia.engine.events.*;
import edu.virginia.engine.events.Event;

import java.awt.*;
import java.util.ArrayList;

import static edu.virginia.engine.cole_tween.TweenableParams.*;


public class SoundSprite extends Sprite {

    Sprite soundWave1 = new Sprite("soundWave1", "circle.png");
    Sprite soundWave2 = new Sprite("soundWave2", "circle.png");
    Sprite soundWave3 = new Sprite("soundWave2", "circle.png");

    Tween circleMove1 = new Tween(soundWave1);
    Tween circleMove2 = new Tween(soundWave2);
    Tween circleMove3 = new Tween(soundWave3);


    double startTime;
    double numWaves;

    public SoundSprite(String id) {
        super(id);

        numWaves = 0;

        soundWave1.setScaleX(.05);
        soundWave1.setScaleY(.05);
        soundWave1.setVisible(false);

        soundWave2.setScaleX(.05);
        soundWave2.setScaleY(.05);
        soundWave2.setVisible(false);

        soundWave3.setScaleX(.05);
        soundWave3.setScaleY(.05);
        soundWave3.setVisible(false);
    }

    public SoundSprite(String id, String imageFileName) {
        super(id, imageFileName);

        numWaves = 0;

        soundWave1.setScaleX(.05);
        soundWave1.setScaleY(.05);
        soundWave1.setVisible(false);

        soundWave2.setScaleX(.05);
        soundWave2.setScaleY(.05);
        soundWave2.setVisible(false);

        soundWave3.setScaleX(.05);
        soundWave3.setScaleY(.05);
        soundWave3.setVisible(false);
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<GamePad> gamePads){

        super.update(pressedKeys, gamePads);

        if(numWaves == 0){

            numWaves++;

            circleMove1.animate(SCALE_X, .05, .5, 1500);
            circleMove1.animate(SCALE_Y, .05, .5, 1500);
            TweenJuggler.add(circleMove1);

            startTime = System.currentTimeMillis();
        }

        if(numWaves == 1 && System.currentTimeMillis() - startTime > 500){

            numWaves++;

            circleMove2.animate(SCALE_X, .05, .5, 1500);
            circleMove2.animate(SCALE_Y, .05, .5, 1500);
            TweenJuggler.add(circleMove2);

        }

        if(numWaves == 2 && System.currentTimeMillis() - startTime > 1000){

            numWaves++;

            circleMove3.animate(SCALE_X, .05, .5, 1500);
            circleMove3.animate(SCALE_Y, .05, .5, 1500);
            TweenJuggler.add(circleMove3);

        }

        if(TweenJuggler.activeTweens.size() < 3 && numWaves == 3){

            if(!TweenJuggler.activeTweens.contains(circleMove1)){
                circleMove1.animate(SCALE_X, .05, .5, 1500);
                circleMove1.animate(SCALE_Y, .05, .5, 1500);
                TweenJuggler.add(circleMove1);
            } else if(!TweenJuggler.activeTweens.contains(circleMove2)){
                circleMove2.animate(SCALE_X, .05, .5, 1500);
                circleMove2.animate(SCALE_Y, .05, .5, 1500);
                TweenJuggler.add(circleMove2);
            } else if(!TweenJuggler.activeTweens.contains(circleMove3)){
                circleMove3.animate(SCALE_X, .05, .5, 1500);
                circleMove3.animate(SCALE_Y, .05, .5, 1500);
                TweenJuggler.add(circleMove3);
            }
        }

        if(soundWave1 != null){
            soundWave1.setPosition(this.getPosX(), this.getPosY());
        }

        if(soundWave2 != null){
            soundWave2.setPosition(this.getPosX(), this.getPosY());
        }

        if(soundWave3 != null){
            soundWave3.setPosition(this.getPosX(), this.getPosY());
        }

    }

    @Override
    public void draw(Graphics g){

        if(soundWave3 != null)
            soundWave3.draw(g);

        if(soundWave2 != null)
            soundWave2.draw(g);

        if(soundWave1 != null)
            soundWave1.draw(g);


        super.draw(g);
    }

    @Override
    public void setVisible(boolean visible){
        super.setVisible(visible);
        soundWave1.setVisible(visible);
        soundWave2.setVisible(visible);
        soundWave3.setVisible(visible);
    }

    @Override
    public boolean collidesWith(DisplayObject other){
        return soundWave1.getHitbox().intersects(other.getHitbox());
    }

}
