package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.ItemOnPlayerPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.Item;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnPlayerPacketPacketHandler implements PacketHandler<ItemOnPlayerPacketPacket> {

    @Override
    public void handle(Player player, ItemOnPlayerPacketPacket packet) {
        if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ON_PLAYER))
            return;

        int container = packet.getContainer();
        int index = packet.getIndex();
        int itemUsed = packet.getItemUsed();
        int itemSlot = packet.getItemSlot();

        Item item = player.getInventory().get(itemSlot);
        Player usedOn = World.get().getPlayers().get(index - 1);

        if(container < 0 || item == null || usedOn == null || itemUsed < 0)
            return;
        if(item.getId() != itemUsed)
            return;

        player.getMovementListener().append(() -> {
            if(player.getPosition().withinDistance(usedOn.getPosition(), 1)) {
                //action
            }
        });
        player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_ON_PLAYER);
    }
}
