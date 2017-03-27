package edu.virginia.engine.events;


import edu.virginia.engine.display.Tween;

/**
 * Created by BrandonSangston on 3/17/17.
 */
public class TweenEvent extends Event {

    public final static String TWEEN_COMPLETE = "TWEEN_COMPLETE";
    public final static String TWEEN_START = "TWEEN_START";
    public final static String TWEEN_Tick = "TWEEN_TICK";

    public TweenEvent(String eventType, IEventDispatcher source) {
        super(eventType, source);
    }

    Tween getTween(){
        return (Tween)source;
    }


}
