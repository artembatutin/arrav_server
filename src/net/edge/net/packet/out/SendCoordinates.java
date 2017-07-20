package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.world.locale.Position;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendCoordinates implements OutgoingPacket {
	
	private final Position position;
	
	public SendCoordinates(Position position) {
		this.position = position;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(85);
		msg.put(position.getY() - (player.getLastRegion().getRegionY() * 8), ByteTransform.C);
		msg.put(position.getX() - (player.getLastRegion().getRegionX() * 8), ByteTransform.C);
		return msg.getBuffer();
	}
}
