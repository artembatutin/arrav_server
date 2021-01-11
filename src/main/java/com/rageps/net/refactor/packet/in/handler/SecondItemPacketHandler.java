package com.rageps.net.refactor.packet.in.handler;

import com.rageps.action.impl.ItemAction;
import com.rageps.content.skill.runecrafting.Runecrafting;
import com.rageps.content.skill.runecrafting.pouch.PouchType;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.FirstItemActionPacket;
import com.rageps.net.refactor.packet.in.model.SecondItemActionPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.ItemDefinition;

import static com.rageps.action.ActionContainers.ITEM_ACTION;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SecondItemPacketHandler implements PacketHandler<SecondItemActionPacket> {

    @Override
    public void handle(Player player, SecondItemActionPacket packet) {

        int slot = packet.getSlot();
        int container = packet.getContainer();
        ;
        int id = packet.getId();

        if (World.get().getEnvironment().isDebug()) {
            player.message("Item action: second click, ID: " + id);
        }
        if (slot < 0 || container < 0 || id < 0 || id > ItemDefinition.DEFINITIONS.length) {
            return;
        }
        Item item = player.getInventory().get(slot);
        if (item == null || item.getId() != id) {
            return;
        }
        player.getCombat().reset(false, false);
        ItemAction e = ITEM_ACTION.get(item.getId());
        if (e != null)
            e.click(player, item, container, slot, 3);
        switch (id) {
            case 5509:
                Runecrafting.empty(player, PouchType.SMALL);
                break;

            case 5510:
                Runecrafting.empty(player, PouchType.MEDIUM);
                break;

            case 5512:
                Runecrafting.empty(player, PouchType.LARGE);
                break;

            case 5514:
                Runecrafting.empty(player, PouchType.GIANT);
                break;
        }
    }
}
