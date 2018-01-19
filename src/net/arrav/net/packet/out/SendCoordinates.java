package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

public final class SendCoordinates implements OutgoingPacket {
	
	private final Position position;
	
	public SendCoordinates(Position position) {
		this.position = position;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(85);
		buf.put(position.getY() - (player.getLastRegion().getRegionY() * 8), ByteTransform.C);
		buf.put(position.getX() - (player.getLastRegion().getRegionX() * 8), ByteTransform.C);
		return buf;
	}
}
