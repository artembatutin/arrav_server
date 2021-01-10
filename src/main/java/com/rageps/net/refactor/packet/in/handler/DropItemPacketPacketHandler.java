package com.rageps.net.refactor.packet.in.handler;

import com.rageps.GameConstants;
import com.rageps.content.item.pets.Pet;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.net.refactor.packet.in.model.DropItemPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.out.model.ItemOnInterfaceSlotPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.GroundItem;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.ItemDefinition;
import com.rageps.world.entity.item.container.ItemContainer;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class DropItemPacketPacketHandler implements PacketHandler<DropItemPacketPacket> {

    @Override
    public void handle(Player player, DropItemPacketPacket packet) {
        if(GameConstants.DROP_DISABLED) {
            player.message("Dropping items has temporary been disabled!");
            return;
        }
        if(player.getActivityManager().contains(ActivityManager.ActivityType.DROP_ITEM)) {
            return;
        }

        int slot = packet.getSlot();
        int id = packet.getId();

        if(slot < 0 || id < 0)
            return;
        Item item = player.getInventory().get(slot);
        if(item == null || item.getId() != id)
            return;
        ItemDefinition def = ItemDefinition.get(item.getId());
        if(def == null)
            return;
        if(!MinigameHandler.execute(player, m -> m.canDrop(player, item, slot))) {
            return;
        }
        if(id == 21432) {//book of diplomacy.
            player.message("This item cannot be dropped.");
            return;
        }
        if(Pet.canDrop(player, item)) {
            return;
        }
        if(!item.getDefinition().isTradable() || item.getDefinition().destroyable()) {
            player.send(new ItemOnInterfaceSlotPacket(14171, item, 0));
            player.interfaceText(14174, "Are you sure you want to destroy this item?");
            player.interfaceText(14175, "Yes");
            player.interfaceText(14176, "No");
            player.interfaceText(14177, "");
            player.interfaceText(14182, "This item is valuable, you will not get it back");
            player.interfaceText(14183, "once clicked Yes.");
            player.interfaceText(14184, item.getDefinition().getName());
            player.getAttributeMap().set(PlayerAttributes.DESTROY_ITEM_SLOT, slot);
            player.chatWidget(14170);
            return;
        }
        if(player.getSkillActionTask().isPresent()) {
            player.getSkillActionTask().get().cancel();
        }
        int amount = ItemDefinition.DEFINITIONS[id].isStackable() ? item.getAmount() : 1;
        player.getInventory().policy = ItemContainer.StackPolicy.NEVER;
        int removed = player.getInventory().remove(new Item(id, amount), slot);
        if(removed == 1) {//if removed 1 slot.
            new GroundItem(new Item(id, amount), player.getPosition(), player).create();
        } else {
            player.getInventory().updateSingle(null, null, slot, true);
        }
        player.getInventory().policy = ItemContainer.StackPolicy.STANDARD;
        player.getActivityManager().execute(ActivityManager.ActivityType.DROP_ITEM);



    }
}
