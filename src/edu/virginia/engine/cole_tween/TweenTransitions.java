package edu.virginia.engine.cole_tween;


public class TweenTransitions {

    /*public double applyTransition(double elapsedTime) {
    }*/

    public static double linear(double timeCompleted, double tweenTime) {
        return timeCompleted/tweenTime;
    }

    public static double quadratic(double timeCompleted, double tweenTime){
        return Math.pow(timeCompleted / tweenTime, 2);
    }
}
