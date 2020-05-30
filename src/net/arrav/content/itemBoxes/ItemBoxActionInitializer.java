package net.arrav.content.itemBoxes;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ItemAction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;

/**
 * @author Tamatea Schofield <tamateea@gmail.com>
 */
public class ItemBoxActionInitializer extends ActionInitializer {
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
