package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.ByteTransform;
import net.edge.net.message.InputMessageListener;
import net.edge.world.content.minigame.MinigameHandler;
import net.edge.world.content.pets.Pet;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager.ActivityType;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemDefinition;
import net.edge.world.node.item.ItemNode;
import net.edge.world.node.region.Region;

/**
 * The message sent from the client when the player drops an item.
 * @author lare96 <http://github.com/lare96>
 */
public final class DropItemMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityType.DROP_ITEM)) {
			return;
		}
		
		int id = payload.getShort(false, ByteTransform.A);
		payload.get(false);
		payload.get(false);
		int slot = payload.getShort(false, ByteTransform.A);
		if(slot < 0 || id < 0)
			return;
		Item item = player.getInventory().get(slot);
		if(item == null || item.getId() != id)
			return;
		
		if(!MinigameHandler.execute(player, m -> m.canDrop(player, item, slot))) {
			return;
		}
		if(Pet.canDrop(player, item)) {
			return;
		}
		if(!item.getDefinition().isTradable()) {
			player.getMessages().sendItemOnInterfaceSlot(14171, item, 0);
			player.getMessages().sendString("Are you sure you want to destroy this item?", 14174);
			player.getMessages().sendString("Yes", 14175);
			player.getMessages().sendString("No", 14176);
			player.getMessages().sendString("", 14177);
			player.getMessages().sendString("This item is valuable, you will not get it back", 14182);
			player.getMessages().sendString("once clicked Yes.", 14183);
			player.getMessages().sendString(item.getDefinition().getName(), 14184);
			
			player.getAttr().get("destroy_item_slot").set(slot);
			player.getMessages().sendChatInterface(14170);
			return;
		}
		
		if(player.getSkillActionTask().isPresent())
			player.getSkillActionTask().get().cancel();
		
		int amount = ItemDefinition.DEFINITIONS[id].isStackable() ? item.getAmount() : 1;
		player.getInventory().remove(new Item(id, amount), slot);
		Region region = player.getRegion();
		if(region != null) {
			region.register(new ItemNode(new Item(id, amount), player.getPosition(), player));
			player.getActivityManager().execute(ActivityType.DROP_ITEM);
		}
	}
}
