package edu.virginia.engine.cole_tween;

public class TweenParam {

    private TweenableParams param;
    private double startVal;
    private double endVal;
    private double tweenTime;

    TweenParam(TweenableParams paramToTween, double startVal, double endVal, double time) {
        this.param = paramToTween;
        this.startVal = startVal;
        this.endVal = endVal;
        this.tweenTime = time;
    }

    public TweenableParams getParam() {
        return param;
    }

    public double getStartVal() {
        return startVal;
    }

    public double getEndVal() {
        return endVal;
    }

    double getTweenTime() {
        return tweenTime;
    }

    /*public boolean isComplete(){
        System.out.println(this.startTime + " + " + getTweenTime() + " = " +
                (this.startTime + getTweenTime()) + " > " + System.currentTimeMillis());
        return (this.startTime + getTweenTime()) > System.currentTimeMillis();
    }

    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }*/
}
