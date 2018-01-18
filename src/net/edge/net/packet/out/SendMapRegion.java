package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;

public final class SendMapRegion implements OutgoingPacket {
	private final Position position;
	
	public SendMapRegion(Position position) {
		this.position = position;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(73);
		buf.putShort(position.getRegionX() + 6, ByteTransform.A);
		buf.putShort(position.getRegionY() + 6);
		return buf;
	}
}
