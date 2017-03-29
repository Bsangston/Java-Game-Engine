package edu.virginia.engine.cole_tween;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.events.EventDispatcher;
import java.util.ArrayList;

public class Tween extends EventDispatcher{
    //    ------check these to make sure------
    ArrayList<TweenParam> params;
    DisplayObject spr;
    TweenTransitions trans;
    double percentDone;
    double startTime;

    public Tween(DisplayObject object) {
        this.spr = object;
        params = new ArrayList<TweenParam>();
    }

    public Tween(DisplayObject object, TweenTransitions transition) {
        this.spr = object;
        this.trans = transition;
        params = new ArrayList<TweenParam>();
    }

    @SuppressWarnings("Duplicates")
    public void animate(TweenableParams fieldToAnimate, double startVal, double endVal, double time) {

        startTime = System.currentTimeMillis();

        switch (fieldToAnimate){
            case SCALE_X:
                spr.setScaleX(startVal);
                break;
            case SCALE_Y:
                spr.setScaleY(startVal);
                break;
            case POS_X:
                spr.setPosition((int)startVal, (int)spr.getPosition().getY());
                break;
            case POS_Y:
                spr.setPosition((int)spr.getPosition().getX(), (int)startVal);
                break;
            case ALPHA:
                spr.setAlpha((float) startVal);
                break;
            case ROTATION:
                spr.setRotation((int)startVal);
                break;
        }

        TweenParam temp = new TweenParam(fieldToAnimate, startVal, endVal, (float) time);
        params.add(temp);

        //this.dispatchEvent(new TweenEvent(TweenEvent.TWEEN_START_EVENT, this));
    }


    public void update() {

        for(TweenParam param : params){
            
            double elapsedTime = System.currentTimeMillis() - this.startTime;
            double progressRatio = TweenTransitions.quadratic(elapsedTime, param.getTweenTime());
            
            double updateVal = ((param.getEndVal() - param.getStartVal()) * progressRatio) + param.getStartVal();

            percentDone = updateVal * 100;

            switch (param.getParam()){
                case SCALE_X:
                    spr.setScaleX(updateVal);
                    break;
                case SCALE_Y:
                    spr.setScaleY(updateVal);
                    break;
                case POS_X:
                    spr.setPosition((int)updateVal, (int)spr.getPosition().getY());
                    break;
                case POS_Y:
                    spr.setPosition((int)spr.getPosition().getX(), (int)updateVal);
                    break;
                case ALPHA:
                    spr.setAlpha((float)updateVal);
                    break;
                case ROTATION:
                    spr.setRotation((int)updateVal);
                    break;
            }
        }

    } //invoked once per frame by the TweenJuggler. Updates this tween / DisplayObject

    //how to check this?
    //check if we reached endval
    public boolean isComplete() {

        for (TweenParam param : params) {
            if ((System.currentTimeMillis() - this.startTime) < param.getTweenTime()) {
                return false;
            }
        }

        //this.dispatchEvent(new TweenEvent(TweenEvent.TWEEN_COMPLETE_EVENT, this));
        return true;
    }

    public void setValue(TweenableParams param, double value) {

    }

    public double getPercentDone() {
        return percentDone;
    }

    public void setPercentDone(double percentDone) {
        this.percentDone = percentDone;
    }
}
