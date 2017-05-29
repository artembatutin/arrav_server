package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.message.InputMessageListener;
import net.edge.world.World;
import net.edge.world.content.pets.Pet;
import net.edge.world.content.skill.crafting.Tanning;
import net.edge.world.content.skill.summoning.Summoning;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;
import net.edge.world.node.item.Item;

public final class ItemOnNpcMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ON_NPC)) {
			return;
		}
		
		int itemId = payload.getShort(false, ByteTransform.A);
		int npc = payload.getShort(false, ByteTransform.A);
		int slot = payload.getShort(true, ByteOrder.LITTLE);
		int container = payload.getShort(false, ByteTransform.A);
		
		Item item = null;
		if(container == 3214) {
			item = player.getInventory().get(slot);
		}
		Npc usedOn = World.getNpcs().get(npc - 1);
		
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
