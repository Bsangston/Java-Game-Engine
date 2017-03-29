package edu.virginia.engine.cole_tween;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventDispatcher;

public class TweenEvent extends Event {
    private Tween tween;
    public final static String TWEEN_COMPLETE_EVENT = "TWEEN_COMPLETE_EVENT";
    public final static String TWEEN_UPDATE_EVENT = "TWEEN_UPDATE_EVENT";
    public final static String TWEEN_START_EVENT = "TWEEN_START_EVENT";

    public TweenEvent(String eventType, IEventDispatcher source) {
        super(eventType, source);
    }

    public Tween getTween() {
        return tween;
    }
}
