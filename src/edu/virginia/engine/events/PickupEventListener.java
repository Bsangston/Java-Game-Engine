package edu.virginia.engine.events;

import edu.virginia.engine.display.PickUp;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;

/**
 * Created by BrandonSangston on 2/17/17.
 */
public class PickupEventListener implements IEventListener {

    public static final String GOLD_COIN_PICKUP = "GOLD_COIN_PICKUP";
    public static final String SILVER_COIN_PICKUP = "SILVER_COIN_PICKUP";
    public static final String COPPER_COIN_PICKUP = "COPPER_COIN_PICKUP";

    @Override
    public void handleEvent(Event event) {
        PickUp coin = (PickUp)event.getSource();

        coin.setVisible(false);
    }
}

