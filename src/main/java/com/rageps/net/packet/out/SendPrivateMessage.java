package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendPrivateMessage implements OutgoingPacket {
	
	private final long name;
	private final int rights, size;
	private final byte[] message;
	
	public SendPrivateMessage(long name, int rights, byte[] message, int size) {
		this.name = name;
		this.rights = rights;
		this.message = message;
		this.size = size;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(196, GamePacketType.VARIABLE_BYTE);
		out.putLong(name);
		out.putInt(player.relations.getPrivateMessageId());
		out.put(rights);
		out.putBytes(message, size);
		out.endVarSize();
		return out;
	}
}
