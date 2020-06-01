package com.rageps.action.item;

import com.rageps.action.ActionInitializer;
import com.rageps.action.impl.ItemAction;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.content.itemBoxes.BoxConstants;
import com.rageps.content.itemBoxes.ItemBoxHandler;
import com.rageps.world.entity.item.Item;

/**
 * @author Tamatea Schofield <tamateea@gmail.com>
 */
public class ItemBoxes extends ActionInitializer {
    @Override
    public void init() {
        ItemAction action = new ItemAction() {
            @Override
            public boolean click(Player player, Item item, int container, int slot, int click) {
                if(click == 1)
                ItemBoxHandler.open(player, item.getId());
                //Todo add right click to check rewards.
                return true;
            }
        };
        action.register(BoxConstants.ABBADON_BOX_ID, BoxConstants.BEGINER_D_SLAYER_BOX_ID, BoxConstants.OBLIVION_BOX_ID,
                BoxConstants.SPIDER_BOX_ID, BoxConstants.UBER_BOX_ID);
    }
}
