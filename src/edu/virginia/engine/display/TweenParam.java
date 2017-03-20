package edu.virginia.engine.display;

/**
 * Created by BrandonSangston on 3/17/17.
 */
public class TweenParam {

    private TweenableParam param;
    private double start;
    private double end;
    private double time;

    public TweenParam(TweenableParam paramToTween, double startVal, double endVal, double time) {
        this.param = paramToTween;
        this.start = startVal;
        this.end = endVal;
        this.time = time;
    }

    public TweenableParam getParam() {
        return param;
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

    public double getTime() {
        return time;
    }
}


