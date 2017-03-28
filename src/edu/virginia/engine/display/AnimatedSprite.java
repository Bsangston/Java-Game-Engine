package edu.virginia.engine.display;
import edu.virginia.engine.controller.GamePad;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.awt.*;

/**
 * Created by BrandonSangston on 2/5/17.
 */
public class AnimatedSprite extends Sprite {

    protected BufferedImage[] frameList;
    protected HashMap<String, int[]> frames; //each animation paired with an int array containing start and end indices

    private int currentFrame = 0;
    private int startIndex = 0;
    private int endIndex = 0;
    private String currentAnim;
    private String prevAnim;
    private int[] currentFrameSet;

    private int framesPerImage = 6, transitionFrames = 3, frameClock = 0;
    private boolean isPlaying = false;

    public AnimatedSprite(String id, String imageFileName, int cols, int rows)
    {
        super(id, imageFileName);
        frameList = parseSpriteSheet(super.getDisplayImage(), cols, rows);
        super.setImage(frameList[0]);
        frames = new HashMap<String, int[]>();

        setHitbox(0,0, getDisplayImage().getWidth(), getDisplayImage().getHeight());
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<GamePad> gamePads) {
	    super.update(pressedKeys, gamePads);

	    if (isPlaying) {
	        playAnim(currentAnim);
        }

    }

    public void stopAnim() {
        isPlaying = false;
    }

    public void playAnim() {
        isPlaying = true;
    }

    public void setAnim(String name) { //returns true if setting to different animation, false otherwise

        prevAnim = currentAnim;
        currentAnim = name;

        if (currentAnim != null && !currentAnim.equalsIgnoreCase(name)) {
           currentFrame = startIndex;
        }

        currentFrameSet = frames.get(name);
        startIndex = currentFrameSet[0];
        endIndex = currentFrameSet.length;
    }

    private boolean isTransition(String name) {

        if (prevAnim != null && !prevAnim.equalsIgnoreCase(name)) {
            prevAnim = currentAnim;
            return true;
        }
        return false;
    }

    private void playAnim(String name) {

        int frameSpeed = framesPerImage;

        if (isTransition(name)) {
            frameSpeed = transitionFrames;

        }
        if (frameClock >= frameSpeed) {
            super.setImage(frameList[currentFrame++]);

            if (currentFrame >= endIndex) {
                currentFrame = startIndex;
            }
            frameClock = 0;
        }
        ++frameClock;
    }

    public void addNewAnimation(String name, int[] set) {
        frames.put(name, set);
    }

    public void setAnimSpeed(int framesPerImage) {
	    this.framesPerImage = framesPerImage;
    }

    public int getAnimSpeed() { return framesPerImage; }

    public String getCurrentAnim() {
	    return currentAnim;
    }



}
