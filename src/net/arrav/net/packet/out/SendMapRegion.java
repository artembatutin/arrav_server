package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

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
