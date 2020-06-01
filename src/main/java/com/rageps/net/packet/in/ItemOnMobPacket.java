package com.rageps.net.packet.in;

import com.rageps.content.item.pets.Pet;
import com.rageps.content.skill.crafting.Tanning;
import com.rageps.content.skill.summoning.Summoning;
import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.Item;

public final class ItemOnMobPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ON_NPC)) {
			return;
		}
		
		int itemId = buf.getShort(false, ByteTransform.A);
		int mob = buf.getShort(false, ByteTransform.A);
		int slot = buf.getShort(true, ByteOrder.LITTLE);
		int container = buf.getShort(false, ByteTransform.A);
		
		Item item = null;
		if(container == 3214) {
			item = player.getInventory().get(slot);
		}
		Mob usedOn = World.get().getMobs().get(mob - 1);
		
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
