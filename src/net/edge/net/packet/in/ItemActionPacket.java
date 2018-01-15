package net.edge.net.packet.in;

import net.edge.Application;
import net.edge.action.ActionContainer;
import net.edge.action.impl.ItemAction;
import net.edge.content.skill.runecrafting.Runecrafting;
import net.edge.content.skill.runecrafting.pouch.PouchType;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.ItemDefinition;

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
			case 16:
				secondClick(player, payload);
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
		if(Application.DEBUG) {
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
	
	/**
	 * Handles the third slot of an item action.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void secondClick(Player player, IncomingMsg payload) {
		int container = payload.getShort(true, ByteTransform.A);
		int slot = payload.getShort(true, ByteOrder.LITTLE);
		int id = payload.getShort(true, ByteTransform.A);
		if(Application.DEBUG) {
			player.message("Item action: second click, ID: " + id);
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
		switch(id) {
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
	
	/**
	 * Handles the third slot of an item action.
	 * @param player  the player to handle this for.
	 * @param payload the payload for reading the sent data.
	 */
	private void thirdClick(Player player, IncomingMsg payload) {
		int container = payload.getShort(true, ByteTransform.A);
		int slot = payload.getShort(true, ByteOrder.LITTLE);
		int id = payload.getShort(true, ByteTransform.A);
		if(Application.DEBUG) {
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
