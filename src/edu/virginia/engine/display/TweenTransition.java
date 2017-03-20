package edu.virginia.engine.display;

/**
 * Created by BrandonSangston on 3/17/17.
 */
public class TweenTransition {

    public static final String LINEAR = "LINEAR";
    public static final String QUADRATIC = "QUADRATIC";

    String transitionType;


    TweenTransition(String type) {
        transitionType = type;
    }

    public void applyTransition(double percentDone) {
        //linear percentDone = x
        //quadratic percentDone = ?

        if (transitionType.equals(TweenTransition.LINEAR)) {
            percentDone = percentDone;
        } else if (transitionType.equals(TweenTransition.QUADRATIC)) {
            percentDone = percentDone*percentDone;
        }
    }

    public void easeInOut(double percentDone) {

    }
}