package edu.virginia.engine.display;

/**
 * Created by BrandonSangston on 3/17/17.
 */
public class TweenTransition {

    public static void applyTransition(double percentDone) {

    }

    public static void linearTransition(double percentDone){
        percentDone += percentDone;
    }

    public static void setQuadraticTransition(double percentDone) {
        percentDone += percentDone*percentDone;
    }

    public static void easeInOut(double percentDone) {

    }
}