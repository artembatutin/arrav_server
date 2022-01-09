package com.rageps.net.refactor.packet.in.handler;

import com.rageps.content.item.pets.Pet;
import com.rageps.content.skill.crafting.Tanning;
import com.rageps.content.skill.summoning.Summoning;
import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.packet.in.model.ItemOnMobPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.World;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.Item;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnMobPacketPacketHandler implements PacketHandler<ItemOnMobPacketPacket> {

    @Override
    public void handle(Player player, ItemOnMobPacketPacket packet) {
        if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ON_NPC)) {
            return;
        }

        int itemId = packet.getItemId();
        int mob = packet.getMob();
        int slot = packet.getSlot();
        int container = packet.getContainer();

        Item item = null;
        if(container == 3214) {
            item = player.getInventory().get(slot);
        }
        Mob usedOn = World.get().getMobRepository().get(mob - 1);

        if(item == null || usedOn == null || item.getId() != itemId) {
            return;
        }
        if(Summoning.itemOnNpc(player, usedOn, item)) {
            return;
        }
        if(Pet.feed(player, usedOn, item)) {
            return;
        }
        if(Tanning.openInterface(player, item, usedOn)) {
            return;
        }
        player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_ON_NPC);


    }
}
