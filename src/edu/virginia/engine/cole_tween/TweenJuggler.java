package edu.virginia.engine.cole_tween;

import java.util.ArrayList;

public class TweenJuggler {
    public static ArrayList<Tween> activeTweens = new ArrayList<Tween>();

    public TweenJuggler() {
        activeTweens = new ArrayList<Tween>();
    }

    public static void add(Tween tween) {
            activeTweens.add(tween);
    }

    public static void remove(Tween tween) {
        activeTweens.remove(tween);
    }

    //invoked every frame by Game, calls update() on every Tween and cleans up old/complete Tweens
    public static void nextFrame() {

        ArrayList<Tween> tempRemove = new ArrayList<>();

        ArrayList<Tween> temp = new ArrayList<>(activeTweens);

        if(temp != null){
            for (Tween t : temp) {
                if (t.isComplete()) {
                    tempRemove.add(t);
                } else {
                    t.update();
                }
            }
        }

        for (Tween t: tempRemove) {
            remove(t);
        }
    }
}
