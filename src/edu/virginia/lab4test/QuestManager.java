package edu.virginia.lab4test;

import edu.virginia.engine.display.AnimatedSprite;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.PickUp;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.EventDispatcher;
import edu.virginia.engine.events.EventListener;
import edu.virginia.engine.events.IEventListener;

import java.util.HashMap;

/**
 * Created by BrandonSangston on 2/17/17.
 */
public class QuestManager extends EventDispatcher implements IEventListener{

    private HashMap<String, Quest> quests;

    public QuestManager() {
        quests = new HashMap<>();
    }


    public void addQuest(Quest q) {
        quests.put(q.getId(), q);
    }

    public Quest getQuest(String id) {
        if (quests.containsKey(id)) {
            return quests.get(id);
        }
        return null;
    }

    public boolean completeQuest(String id) {
        if (quests.containsKey(id) && quests.get(id).isActive() && !quests.get(id).isComplete()) {
            quests.get(id).setComplete(true);
            dispatchEvent(new Event(Event.QUEST_COMPLETE, this));
            return true;
        }
        return false;
    }

    public void setActiveQuest(Quest q) {
        if (quests.containsValue(q)) {
            for (Quest i : quests.values()) {
                if (i.isActive()) {
                    i.setActive(false);
                }
            }
            quests.get(q.getId()).setActive(true);
        } else {
            addQuest(q);
            q.setActive(true);
        }
    }

    public Quest getActiveQuest() {
        for (Quest q : quests.values()) {
            if (q.isActive()) {
                return q;
            }
        }
        return new Quest("None", "None");
    }

    public HashMap<String, Quest> getQuests() {
        return quests;
    }

    @Override
    public void handleEvent(Event event) {
        /* Pickup */
        if (event.getSource() instanceof PickUp) {
            ((PickUp) event.getSource()).setVisible(false);
        }
        /* Quest*/
       if (event.getSource() instanceof QuestManager) {

       }
    }
}
