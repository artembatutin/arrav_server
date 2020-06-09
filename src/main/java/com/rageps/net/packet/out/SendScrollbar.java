package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendScrollbar implements OutgoingPacket {

	private final int id;
	private final int size;

	public SendScrollbar(int id, int size) {
		this.id = id;
		this.size = size;
	}

	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(79);
		out.putInt(id);
		out.putShort(size);
		return out;
	}

	@Override
	public int size() {
		return 6;
	}
}
