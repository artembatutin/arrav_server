package net.edge.net.packet.in;

import net.edge.action.ActionContainer;
import net.edge.action.impl.ItemAction;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.activity.ActivityManager;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemDefinition;

/**
 * The message sent from the client when the player clicks an item.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemActionPacket implements IncomingPacket {
	
	public static final ActionContainer<ItemAction> ITEM_ACTION = new ActionContainer<>();
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ACTION))
			return;
		
		switch(opcode) {
			case 122:
				firstClick(player, payload);
				break;
			case 75:
				thirdClick(player, payload);
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_ACTION);
	}
	
	/**
	 * Handles the first slot of an item action.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void firstClick(Player player, IncomingMsg payload) {
		int container = payload.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
		int slot = payload.getShort(false, ByteTransform.A);
		int id = payload.getShort(false, ByteOrder.LITTLE);
		if(slot < 0 || container < 0 || id < 0 || id > ItemDefinition.DEFINITIONS.length)
			return;
		Item item = player.getInventory().get(slot);
		if(item == null || item.getId() != id)
			return;
		player.getCombatBuilder().cooldown(true);
		ItemAction e = ITEM_ACTION.get(item.getId());
		if(e != null)
			e.click(player, item, container, slot, 1);
	}
	
	/**
	 * Handles the third slot of an item action.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void thirdClick(Player player, IncomingMsg payload) {
		int container = payload.getShort(true, ByteTransform.A);
		int slot = payload.getShort(true, ByteOrder.LITTLE);
		int id = payload.getShort(true, ByteTransform.A);
		if(slot < 0 || container < 0 || id < 0 || id > ItemDefinition.DEFINITIONS.length) {
			return;
		}
		Item item = player.getInventory().get(slot);
		player.getCombatBuilder().cooldown(true);
		ItemAction e = ITEM_ACTION.get(item.getId());
		if(e != null)
			e.click(player, item, container, slot, 3);
		if(item.getDefinition().getName().contains("Black mask")) {
			player.getInventory().replace(item.getId(), 8921, true);//black mask discharge
		}
	}
}
