package edu.virginia.engine.events;

/**
 * Created by BrandonSangston on 2/17/17.
 */
public class EventListener implements IEventListener {

    public EventListener() {

    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventType().equals(Event.TWEEN_START)) {

        }
        else if (event.getEventType().equals(Event.TWEEN_END)) {

        }
        else if (event.getEventType().equals(Event.TWEEN_TICK)) {

        }

    }

}
