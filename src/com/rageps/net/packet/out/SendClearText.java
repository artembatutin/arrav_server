package com.rageps.net.packet.out;

import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendClearText implements OutgoingPacket {
	
	private final int start, count;
	
	public SendClearText(int start, int count) {
		this.start = start;
		this.count = count;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		for(int i = start; i < start + count; i++) {
			player.interfaceTexts.remove(i);
		}
		out.message(127, GamePacketType.FIXED);
		out.putShort(start, ByteTransform.A);
		out.putShort(count, ByteTransform.A);
		return out;
	}
}
