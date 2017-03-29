package edu.virginia.engine.events;

/**
 * Created by BrandonSangston on 2/17/17.
 */
public class Event {

    public static final String COIN_PICKED_UP = "COIN_PICKED_UP";
    public static final String QUEST_COMPLETE = "QUEST_COMPLETE";
    public static final String COLLISION = "COLLISION";
    public static final String TWEEN_START = "TWEEN_START";
    public static final String TWEEN_TICK = "TWEEN_TICK";
    public static final String TWEEN_END = "TWEEN_END";
    public static final String SOUND_SPRITE_COLLISION = "SOUND_SPRITE_COLLISION";

    protected String eventType;
    protected IEventDispatcher source; //the object that created this event

    public Event(String eventType, IEventDispatcher source) {
        this.eventType = eventType;
        this.source = source;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setSource(IEventDispatcher source) {
        this.source = source;
    }

    public IEventDispatcher getSource() {
        return source;
    }
}

