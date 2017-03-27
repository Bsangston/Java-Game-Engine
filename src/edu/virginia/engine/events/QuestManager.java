package edu.virginia.engine.events;

import edu.virginia.engine.display.AnimatedShadowSprite;
import edu.virginia.engine.display.PickUp;

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
        if (event.getSource() instanceof AnimatedShadowSprite) {
            ((AnimatedShadowSprite) event.getSource()).setVisible(false);
        }
        /* Quest*/
        if (event.getSource() instanceof QuestManager) {

        }
    }
}
