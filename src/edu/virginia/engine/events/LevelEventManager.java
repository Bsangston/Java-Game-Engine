package edu.virginia.engine.events;

import edu.virginia.levels.StartScreen;
import edu.virginia.levels.Stimulus;

/**
 * Created by cole on 4/24/17.
 */
public class LevelEventManager extends EventDispatcher implements IEventListener{

    Stimulus s;

    public LevelEventManager(Stimulus wrapper){
        s = wrapper;
    }


    @Override
    public void handleEvent(Event event) {
        if(event.eventType.equals("start_level_1")) {
            s.removeChildren();
            s.addChild(s.getL1());
        }
        if(event.eventType.equals("start_level_2")){
            s.removeChildren();
            s.addChild(s.getL2());
        }
        if(event.eventType.equals("start_level_3")){
            s.removeChildren();
            s.addChild(s.getL3());
        }
        if(event.eventType.equals("start_level_4")){
            s.removeChildren();
            s.addChild(s.getL4());
        }
        if(event.eventType.equals("start_level_5")){
            s.removeChildren();
            s.addChild(s.getL5());
        }
        if(event.eventType.equals("end_level_1")){
            s.removeChildren();
            s.getStartScreen().setCurrentLevel(2);
            s.addChild(s.getStartScreen());

            if(!s.getStartScreen().getUnlockedLevels().contains(2))
                s.getStartScreen().getUnlockedLevels().add(2);
        }
        if(event.eventType.equals("end_level_2")){
            s.removeChildren();
            s.getStartScreen().setCurrentLevel(3);
            s.addChild(s.getStartScreen());

            if(!s.getStartScreen().getUnlockedLevels().contains(3))
                s.getStartScreen().getUnlockedLevels().add(3);
        }
        if(event.eventType.equals("end_level_3")){
            s.removeChildren();
            s.getStartScreen().setCurrentLevel(4);
            s.addChild(s.getStartScreen());

            if(!s.getStartScreen().getUnlockedLevels().contains(4))
                s.getStartScreen().getUnlockedLevels().add(4);
        }
        if(event.eventType.equals("end_level_4")){
            s.removeChildren();
            s.getStartScreen().setCurrentLevel(5);
            s.addChild(s.getStartScreen());

            if(!s.getStartScreen().getUnlockedLevels().contains(5))
                s.getStartScreen().getUnlockedLevels().add(5);
        }
        if(event.eventType.equals("end_level_5")){
            s.removeChildren();
            s.getStartScreen().setGameWon(true);
            s.addChild(s.getStartScreen());
        }
    }
}
