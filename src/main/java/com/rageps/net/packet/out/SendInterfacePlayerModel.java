package com.rageps.net.packet.out;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendInterfacePlayerModel implements OutgoingPacket {
	
	private final int id;
	
	public SendInterfacePlayerModel(int id) {
		this.id = id;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(185);
		out.putShort(id, ByteTransform.A, ByteOrder.LITTLE);
		return out;
	}
}
