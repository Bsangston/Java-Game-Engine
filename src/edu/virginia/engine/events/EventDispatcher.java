package edu.virginia.engine.events;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by BrandonSangston on 2/17/17.
 */
public class EventDispatcher implements IEventDispatcher {

    protected HashMap<String, ArrayList<IEventListener>> eventListeners;

    public EventDispatcher() {
      eventListeners = new HashMap<>();
    }

    @Override
    public void addEventListener(IEventListener listener, String eventType) {
        if (eventListeners.containsKey(eventType)) {
            eventListeners.get(eventType).add(listener);
        } else {
            eventListeners.put(eventType, new ArrayList<>());
            eventListeners.get(eventType).add(listener);
        }

    }

    @Override
    public void removeEventListener(IEventListener listener, String eventType) {
        if (eventListeners.containsKey(eventType)) {
            eventListeners.get(eventType).remove(listener);
        }

    }

    @Override
    public void dispatchEvent(Event event) {
        if (eventListeners.containsKey(event.getEventType())) {
            for (IEventListener e : eventListeners.get(event.getEventType())) {
                e.handleEvent(event);
            }
        }
    }

    @Override
    public boolean hasEventListener(IEventListener listener, String eventType) {
        return eventListeners.containsKey(eventType) && eventListeners.get(eventType).contains(listener);
    }
}
