package net.edge.net.packet.in;

import net.edge.content.item.pets.Pet;
import net.edge.content.skill.crafting.Tanning;
import net.edge.content.skill.summoning.Summoning;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;
import net.edge.world.entity.item.Item;

public final class ItemOnMobPacket implements IncomingPacket {

	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ON_NPC)) {
			return;
		}

		int itemId = payload.getShort(false, ByteTransform.A);
		int mob = payload.getShort(false, ByteTransform.A);
		int slot = payload.getShort(true, ByteOrder.LITTLE);
		int container = payload.getShort(false, ByteTransform.A);

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
