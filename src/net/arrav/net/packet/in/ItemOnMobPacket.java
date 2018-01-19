package net.arrav.net.packet.in;

import io.netty.buffer.ByteBuf;
import net.arrav.content.item.pets.Pet;
import net.arrav.content.skill.crafting.Tanning;
import net.arrav.content.skill.summoning.Summoning;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.World;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.activity.ActivityManager;
import net.arrav.world.entity.item.Item;

public final class ItemOnMobPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, ByteBuf buf) {
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
