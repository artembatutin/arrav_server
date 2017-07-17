package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.actor.player.Player;

public final class SendConfig implements OutgoingPacket {
	
	private final int id, state;
	
	public SendConfig(int id, int state) {
		this.id = id;
		this.state = state;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		if(state < Byte.MIN_VALUE || state > Byte.MAX_VALUE) {
			msg.message(87);
			msg.putShort(id, ByteOrder.LITTLE);
			msg.putInt(state, ByteOrder.MIDDLE);
			return msg.getBuffer();
		}
		msg.message(36);
		msg.putShort(id, ByteOrder.LITTLE);
		msg.put(state);
		return msg.getBuffer();
	}
}
