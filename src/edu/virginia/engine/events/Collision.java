package edu.virginia.engine.events;

import edu.virginia.engine.display.DisplayObject;

/**
 * Created by BrandonSangston on 3/12/17.
 */
public class Collision extends Event {

    public static final String GROUND = "GROUND";
    public static final String WALL = "WALL";
    public static final String ENEMY = "ENEMY";
    public static final String PICKUP = "PICKUP";


    private DisplayObject collidee;
    private String collisionType;

    public Collision(String collisionType, IEventDispatcher source, DisplayObject other) {
        super(Event.COLLISION, source);
        this.collisionType = collisionType;
        collidee = other;
    }

    public DisplayObject getCollidee() {
        return collidee;
    }
}
