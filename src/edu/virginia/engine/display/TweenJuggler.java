package edu.virginia.engine.display;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by BrandonSangston on 3/17/17.
 */
public class TweenJuggler {

    public static Queue<Tween> tweens;

    private static TweenJuggler tweenJuggler = new TweenJuggler();

    private TweenJuggler() { }

    public static TweenJuggler getInstance() {
        return tweenJuggler;
    }

    public static void add(Tween tween) {
        tweens.add(tween);
    }

}


