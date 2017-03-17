package edu.virginia.engine.display;

/**
 * Created by BrandonSangston on 3/17/17.
 */
public class TweenParam {

    public enum String {
        X, Y, SCALE_X, SCALE_Y, ROTATION, ALPHA
    }

    private String param;
    private double start;
    private double end;
    private double time;

    public TweenParam(String paramToTween, double startVal, double endVal, double time) {
        this.param = paramToTween;
        this.start = startVal;
        this.end = endVal;
        this.time = time;
    }

    public String getParam() {
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


