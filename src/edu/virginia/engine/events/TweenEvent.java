package edu.virginia.engine.events;


import edu.virginia.engine.display.Tween;

/**
 * Created by BrandonSangston on 3/17/17.
 */
public class TweenEvent extends Event {

    final static String TWEEN_COMPLETE = "TWEEN_COMPLETE";
    final static String TWEEN_START = "TWEEN_START";


    public TweenEvent(String eventType, IEventDispatcher source) {
        super(eventType, source);
    }

    Tween getTween(){
        return (Tween)source;
    }


}
