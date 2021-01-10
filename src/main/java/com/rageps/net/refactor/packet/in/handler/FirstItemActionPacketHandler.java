package com.rageps.net.refactor.packet.in.handler;

import com.rageps.action.impl.ItemAction;
import com.rageps.net.refactor.packet.in.model.FirstItemActionPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.ItemDefinition;

import static com.rageps.action.ActionContainers.ITEM_ACTION;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class FirstItemActionPacketHandler implements PacketHandler<FirstItemActionPacket> {

    @Override
    public void handle(Player player, FirstItemActionPacket packet) {

        int slot = packet.getSlot();
        int container = packet.getContainer();;
        int id = packet.getId();

        if(World.get().getEnvironment().isDebug()) {
            player.message("Item action: First click, ID: " + id);
        }
        if(slot < 0 || container < 0 || id < 0 || id > ItemDefinition.DEFINITIONS.length)
            return;
        Item item = player.getInventory().get(slot);
        if(item == null || item.getId() != id) {
            return;
        }
        player.getCombat().reset(false, false);
        ItemAction e = ITEM_ACTION.get(item.getId());
        if(e != null)
            e.click(player, item, container, slot, 1);
        switch(id) {
        }


    }
}
