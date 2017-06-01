package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.message.InputMessageListener;
import net.edge.content.item.ItemAction;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemDefinition;

/**
 * The message sent from the client when the player clicks an item.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemActionMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
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
	private void firstClick(Player player, ByteMessage payload) {
		int container = payload.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
		int slot = payload.getShort(false, ByteTransform.A);
		int id = payload.getShort(false, ByteOrder.LITTLE);
		if(slot < 0 || container < 0 || id < 0 || id > ItemDefinition.DEFINITIONS.length)
			return;
		Item item = player.getInventory().get(slot);
		if(item == null || item.getId() != id)
			return;
		player.getCombatBuilder().cooldown(true);
		ItemAction.first(player, item, container, slot);
	}
	
	/**
	 * Handles the third slot of an item action.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void thirdClick(Player player, ByteMessage payload) {
		int container = payload.getShort(true, ByteTransform.A);
		int slot = payload.getShort(true, ByteOrder.LITTLE);
		int id = payload.getShort(true, ByteTransform.A);
		if(slot < 0 || container < 0 || id < 0 || id > ItemDefinition.DEFINITIONS.length) {
			return;
		}
		Item item = player.getInventory().get(slot);
		player.getCombatBuilder().cooldown(true);
		ItemAction.third(player, item, container, slot);
	}
}
