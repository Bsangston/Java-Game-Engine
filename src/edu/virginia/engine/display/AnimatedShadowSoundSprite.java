package edu.virginia.engine.display;

import edu.virginia.engine.cole_tween.Tween;
import edu.virginia.engine.cole_tween.TweenJuggler;
import edu.virginia.engine.controller.GamePad;

import java.awt.*;
import java.util.ArrayList;

import static edu.virginia.engine.cole_tween.TweenableParams.SCALE_X;
import static edu.virginia.engine.cole_tween.TweenableParams.SCALE_Y;

/**
 * Created by cole on 3/29/17.
 */
public class AnimatedShadowSoundSprite extends AnimatedShadowSprite {

    Sprite soundWave1 = new Sprite("soundWave1", "circle.png");
    Sprite soundWave2 = new Sprite("soundWave2", "circle.png");
    Sprite soundWave3 = new Sprite("soundWave2", "circle.png");

    Tween circleMove1 = new Tween(soundWave1);
    Tween circleMove2 = new Tween(soundWave2);
    Tween circleMove3 = new Tween(soundWave3);

    int loopPerSec = 0;

    double soundScale = 0.5f;

    double startTime;
    double numWaves;

    public AnimatedShadowSoundSprite(String id, String imageFileName, String shadowFileName, int cols, int rows) {
        super(id, imageFileName, shadowFileName, cols, rows);

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

        soundScale *= getScale();

    }

    public AnimatedShadowSoundSprite(String id, String shadow_filename, int cols, int rows) {
        super(id, shadow_filename, cols, rows);

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

        soundScale *= getScale();

    }

    public AnimatedShadowSoundSprite(String id) {
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

        soundScale *= getScale();
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<GamePad> gamePads){

        super.update(pressedKeys, gamePads);

        if(loopPerSec == 0){

            numWaves++;

            circleMove1.animate(SCALE_X, .05, soundScale, 1500);
            circleMove1.animate(SCALE_Y, .05, soundScale, 1500);
            TweenJuggler.add(circleMove1);

        }

        if(loopPerSec == 60){

            numWaves++;

            circleMove2.animate(SCALE_X, .05, soundScale, 1500);
            circleMove2.animate(SCALE_Y, .05, soundScale, 1500);
            TweenJuggler.add(circleMove2);



        }

        if(loopPerSec == 120){

            numWaves++;

            circleMove3.animate(SCALE_X, .05, soundScale, 1500);
            circleMove3.animate(SCALE_Y, .05, soundScale, 1500);
            TweenJuggler.add(circleMove3);
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

        loopPerSec = (loopPerSec + 1) % 180;
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
    public void toggleShadow(boolean s){
        super.toggleShadow(s);

        soundWave1.setVisible(s);
        soundWave2.setVisible(s);
        soundWave3.setVisible(s);
    }

    @Override
    public boolean collidesWith(DisplayObject other){

        return soundWave1.getHitbox().intersects(other.getHitbox())
                || soundWave2.getHitbox().intersects(other.getHitbox())
                || soundWave3.getHitbox().intersects(other.getHitbox());
    }

    public double getSoundScale() {
        return soundScale;
    }

    public void setSoundScale(double soundScale) {
        this.soundScale = soundScale;
    }

    @Override
    public void setScale(double scale) {
        super.setScale(scale);
        setSoundScale(getScale()*0.5f);
    }
}

