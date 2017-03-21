package edu.virginia.engine.display;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.EventDispatcher;
import edu.virginia.engine.events.IEventListener;

import java.util.ArrayList;

/**
 * Created by matthewleon on 3/17/17.
 *  Object representing on Sprite being tweened in some way. Can have multiple tweenParam objects
 */
public class Tween extends EventDispatcher {

    public static final String LINEAR = "LINEAR";
    public static final String QUADRATIC = "QUADRATIC";

    private TweenParam tparam;
    private TweenTransition transition;
    private DisplayObject sprite;
    private double percentComplete;

    public Tween(DisplayObject object) {
        //params = new ArrayList<TweenParam>();
        sprite = object;
        percentComplete = 0;
    }

    public Tween(DisplayObject object, TweenTransition transition) {
        //params = new ArrayList<TweenParam>();
        sprite = object;
        percentComplete = 0;
    }

    public void animate(TweenableParam fieldToAnimate, double startVal, double endVal, double time) {
        TweenParam tparam = new TweenParam(fieldToAnimate, startVal, endVal, time);

    }

    public void update() {

        if ((int)percentComplete == 0) {
            sprite.dispatchEvent(new Event(Event.TWEEN_START, sprite));
        } else if (isComplete()) {
            sprite.dispatchEvent(new Event(Event.TWEEN_END, sprite));
        }

        double tick = tparam.getEnd()/tparam.getTime();

        if (tparam.getStart() > tparam.getEnd()) {
            tick = -tick;
        }

        switch(tparam.getParam()) {
            case ALPHA:
                float a = sprite.getAlpha();
                sprite.setAlpha((float)(a + tick));
                break;
            case ROTATION:
                float r = sprite.getRotation();
                sprite.setRotation((float)(r + tick));
                break;
            case X:
                int x = sprite.getPosX();
                sprite.setPosX(x + (int)tick);
                break;
            case Y:
                int y = sprite.getPosY();
                sprite.setPosY(y + (int)tick);
                break;
            case SCALE_X:
                double sx = sprite.getScaleX();
                sprite.setScaleX(sx + tick);
                break;
            case SCALE_Y:
                double sy = sprite.getScaleY();
                sprite.setScaleX(sy + tick);
                break;
        }

        sprite.dispatchEvent(new Event(Event.TWEEN_TICK, sprite));
        percentComplete += (100*tick)/tparam.getEnd();

    }

    public boolean isComplete() {
        return percentComplete >= 100;

    }

    public void setValue(String param, double value) {

    }

}
