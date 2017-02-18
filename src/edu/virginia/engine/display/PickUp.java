package edu.virginia.engine.display;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.EventListener;

/**
 * Created by BrandonSangston on 2/17/17.
 */
public class PickUp extends AnimatedSprite{

    int value;

    public PickUp(String id, String imageFileName, int cols, int rows) {
        super(id, imageFileName, cols, rows);
        value = 1;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}
