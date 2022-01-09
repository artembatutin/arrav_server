package com.rageps.net.refactor.packet.in.handler;

import com.rageps.action.impl.ItemAction;
import com.rageps.content.skill.runecrafting.Runecrafting;
import com.rageps.content.skill.runecrafting.pouch.PouchType;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.SecondItemActionPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.ItemDefinition;

import static com.rageps.action.ActionContainers.ITEM_ACTION;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ThirdItemPacketHandler implements PacketHandler<SecondItemActionPacket> {

    @Override
    public void handle(Player player, SecondItemActionPacket packet) {

        int slot = packet.getSlot();
        int container = packet.getContainer();
        ;
        int id = packet.getId();

        if(World.get().getEnvironment().isDebug()) {
            player.message("Item action: third click, ID: " + id);
        }
        if(slot < 0 || container < 0 || id < 0 || id > ItemDefinition.DEFINITIONS.length) {
            return;
        }
        Item item = player.getInventory().get(slot);
        if(item == null || item.getId() != id) {
            return;
        }
        player.getCombat().reset(false, false);
        ItemAction e = ITEM_ACTION.get(item.getId());
        if(e != null)
            e.click(player, item, container, slot, 3);
        if(item.getDefinition().getName().contains("Black mask")) {
            player.getInventory().replace(item.getId(), 8921, true);//black mask discharge
        }
        switch(item.getId()) {
            case 5509:
                Runecrafting.examine(player, PouchType.SMALL);
                break;

            case 5510:
                Runecrafting.examine(player, PouchType.MEDIUM);
                break;

            case 5512:
                Runecrafting.examine(player, PouchType.LARGE);
                break;

            case 5514:
                Runecrafting.examine(player, PouchType.GIANT);
                break;
        }
    }
}
