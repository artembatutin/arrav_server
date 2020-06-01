package com.rageps.net.packet.out;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendMoveComponent implements OutgoingPacket {
	
	private final int id, x, y;
	
	public SendMoveComponent(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(70);
		out.putShort(x);
		out.putShort(y, ByteOrder.LITTLE);
		out.putShort(id, ByteOrder.LITTLE);
		return out;
	}
}
