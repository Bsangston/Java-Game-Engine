package edu.virginia.engine.events;

import edu.virginia.engine.display.DisplayObject;

/**
 * Created by BrandonSangston on 3/12/17.
 */
public class Collision extends Event {

    private DisplayObject collidee;

    public Collision(IEventDispatcher source, DisplayObject other) {
        super(Event.COLLISION, source);
        collidee = other;
    }

    public DisplayObject getCollidee() {
        return collidee;
    }
}
