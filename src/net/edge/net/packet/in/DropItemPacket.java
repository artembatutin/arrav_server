package net.edge.net.packet.in;

import net.edge.GameConstants;
import net.edge.content.market.MarketItem;
import net.edge.content.minigame.MinigameHandler;
import net.edge.content.item.pets.Pet;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.IncomingPacket;
import net.edge.net.packet.out.SendItemOnInterfaceSlot;
import net.edge.util.log.impl.DropItemLog;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager.ActivityType;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.ItemDefinition;
import net.edge.world.entity.item.GroundItem;

/**
 * The message sent from the client when the player drops an item.
 * @author lare96 <http://github.com/lare96>
 */
public final class DropItemPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(GameConstants.DROP_DISABLED) {
			player.message("Dropping items has temporary been disabled!");
			return;
		}
		if(player.getActivityManager().contains(ActivityType.DROP_ITEM)) {
			return;
		}
		
		int id = payload.getShort(false, ByteTransform.A);
		payload.get(false);
		payload.get(false);
		int slot = payload.getShort(false, ByteTransform.A);
		if(slot < 0 || id < 0)
			return;
		System.out.println(id);
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
			player.out(new SendItemOnInterfaceSlot(14171, item, 0));
			player.text(14174, "Are you sure you want to destroy this item?");
			player.text(14175, "Yes");
			player.text(14176, "No");
			player.text(14177, "");
			player.text(14182, "This item is valuable, you will not get it back");
			player.text(14183, "once clicked Yes.");
			player.text(14184, item.getDefinition().getName());
			player.getAttr().get("destroy_item_slot").set(slot);
			player.chatWidget(14170);
			return;
		}
		if(player.getSkillActionTask().isPresent()) {
			player.getSkillActionTask().get().cancel();
		}
		int amount = ItemDefinition.DEFINITIONS[id].isStackable() ? item.getAmount() : 1;
		int removed = player.getInventory().remove(new Item(id, amount), slot);
		System.out.println(removed);
		if(removed == 1) {//if removed 1 slot.
			player.getRegion().ifPresent(r -> r.register(new GroundItem(new Item(id, amount), player.getPosition(), player)));
		} else {
			player.getInventory().updateSingle(null, null, slot, true);
		}
		player.getActivityManager().execute(ActivityType.DROP_ITEM);
	}
}
