package edu.virginia.engine.events;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventDispatcher;

/**
 * Created by BrandonSangston on 2/18/17.
 */
public class Quest {

    String id;
    String objective;
    boolean isActive;
    boolean isComplete;

    public Quest(String id) {
        this.id = id;
    }

    public Quest(String id, String objective) {
        this.id = id;
        this.objective = objective;

    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getId() {
        return id;
    }

    public String getObjective() {
        return objective;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isComplete() {
        return isComplete;
    }
}
