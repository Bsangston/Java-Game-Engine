package edu.virginia.engine.display;

import java.util.ArrayList;

/**
 * Created by matthewleon on 3/17/17.
 *  Object representing on Sprite being tweened in some way. Can have multiple tweenParam objects
 */
public class Tween {

    private ArrayList<TweenParam> params;
    private DisplayObject sprite;

    Tween(DisplayObject object) {
        params = new ArrayList<TweenParam>();
        sprite = object;
    }

    Tween(DisplayObject object, TweenTransition transition) {
        params = new ArrayList<TweenParam>();
        sprite = object;
    }

    public void animate(TweenableParam fieldToAnimate, double startVal, double endVal, double time) {
        TweenParam tparam = new TweenParam(fieldToAnimate, startVal, endVal, time);

    }

    public void update() {

    }

    public boolean isComplete() {
        return true;
    }

    public void setValue(String param, double value) {

    }
}
