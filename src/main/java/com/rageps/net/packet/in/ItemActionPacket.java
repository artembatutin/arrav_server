package com.rageps.net.packet.in;

import com.rageps.RagePS;
import com.rageps.action.ActionContainer;
import com.rageps.action.impl.ItemAction;
import com.rageps.content.skill.runecrafting.Runecrafting;
import com.rageps.content.skill.runecrafting.pouch.PouchType;
import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.ItemDefinition;

/**
 * The message sent from the client when the player clicks an item.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemActionPacket implements IncomingPacket {
	
	public static final ActionContainer<ItemAction> ITEM_ACTION = new ActionContainer<>();
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ACTION))
			return;
		
		switch(opcode) {
			case 122:
				firstClick(player, buf);
				break;
			case 16:
				secondClick(player, buf);
			case 75:
				thirdClick(player, buf);
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_ACTION);
	}
	
	/**
	 * Handles the first slot of an item action.
	 * @param player the player to handle this for.
	 * @param buf the buffer for reading the sent data.
	 */
	private void firstClick(Player player, GamePacket buf) {
		int container = buf.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
		int slot = buf.getShort(false, ByteTransform.A);
		int id = buf.getShort(false, ByteOrder.LITTLE);
		if(World.get().getEnvironment().isDebug()) {
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
	 * @param player the player to handle this for.
	 * @param buf the buffer for reading the sent data.
	 */
	private void secondClick(Player player, GamePacket buf) {
		int container = buf.getShort(true, ByteTransform.A);
		int slot = buf.getShort(true, ByteOrder.LITTLE);
		int id = buf.getShort(true, ByteTransform.A);
		if(World.get().getEnvironment().isDebug()) {
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
	 * @param player the player to handle this for.
	 * @param buf the buffer for reading the sent data.
	 */
	private void thirdClick(Player player, GamePacket buf) {
		int container = buf.getShort(true, ByteTransform.A);
		int slot = buf.getShort(true, ByteOrder.LITTLE);
		int id = buf.getShort(true, ByteTransform.A);
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
