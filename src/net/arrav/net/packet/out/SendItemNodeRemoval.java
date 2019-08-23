package net.arrav.net.packet.out;

import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.GroundItem;

public final class SendItemNodeRemoval implements OutgoingPacket {
	
	private final GroundItem item;
	
	public SendItemNodeRemoval(GroundItem item) {
		this.item = item;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(156);
		out.put(0, ByteTransform.S);
		out.putShort(item.getItem().getId());
		return out;
	}
	
	@Override
	public GamePacket coordinatePacket(Player player) {
		return new SendCoordinates(item.getPosition()).write(player);
	}
}
