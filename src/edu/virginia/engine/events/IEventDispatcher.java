package edu.virginia.engine.events;

/**
 * Created by BrandonSangston on 2/17/17.
 */
public interface IEventDispatcher {

    public void addEventListener(IEventListener listener, String eventType);
    public void removeEventListener(IEventListener listener, String eventType);
    public void dispatchEvent(Event event);
    public boolean hasEventListener(IEventListener listener, String eventType);

}
