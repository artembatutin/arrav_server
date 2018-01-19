package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendConfig implements OutgoingPacket {
	
	private final int id, state;
	
	public SendConfig(int id, int state) {
		this.id = id;
		this.state = state;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		if(state < Byte.MIN_VALUE || state > Byte.MAX_VALUE) {
			buf.message(87);
			buf.putShort(id, ByteOrder.LITTLE);
			buf.putInt(state, ByteOrder.MIDDLE);
			return buf;
		}
		buf.message(36);
		buf.putShort(id, ByteOrder.LITTLE);
		buf.put(state);
		return buf;
	}
}
