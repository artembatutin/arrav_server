package net.arrav.net.packet.in;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.activity.ActivityManager;
import net.arrav.world.entity.item.Item;

/**
 * The message sent from the client when a player uses an item on another player.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemOnPlayerPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, ByteBuf buf) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ON_PLAYER))
			return;
		
		int container = buf.getShort(ByteTransform.A, ByteOrder.BIG);
		int index = buf.getShort();
		int itemUsed = buf.getShort();
		int itemSlot = buf.getShort(false, ByteTransform.A, ByteOrder.LITTLE);
		Item item = player.getInventory().get(itemSlot);
		Player usedOn = World.get().getPlayers().get(index - 1);
		
		if(container < 0 || item == null || usedOn == null || itemUsed < 0)
			return;
		if(item.getId() != itemUsed)
			return;
		
		player.getMovementListener().append(() -> {
			if(player.getPosition().withinDistance(usedOn.getPosition(), 1)) {
			
			}
		});
		player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_ON_PLAYER);
	}
}
