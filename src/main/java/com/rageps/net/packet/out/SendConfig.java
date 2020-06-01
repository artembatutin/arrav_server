package com.rageps.net.packet.out;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendConfig implements OutgoingPacket {
	
	private final int id, state;
	
	public SendConfig(int id, int state) {
		this.id = id;
		this.state = state;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		if(state < Byte.MIN_VALUE || state > Byte.MAX_VALUE) {
			out.message(87);
			out.putShort(id, ByteOrder.LITTLE);
			out.putInt(state, ByteOrder.MIDDLE);
			return out;
		}
		out.message(36);
		out.putShort(id, ByteOrder.LITTLE);
		out.put(state);
		return out;
	}
}
