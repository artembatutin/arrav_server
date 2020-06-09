package com.rageps.net.packet.out;

import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendText implements OutgoingPacket {
	
	private final int id;
	private final String text;
	
	public SendText(int id, String text) {
		this.id = id;
		this.text = text;
	}
	public SendText(String text, int id) {
		this(id, text);
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(126, GamePacketType.VARIABLE_SHORT);
		out.putCString(text);
		out.putShort(id, ByteTransform.A);
		out.endVarSize();
		out.endVarSize();
		return out;
	}
}
