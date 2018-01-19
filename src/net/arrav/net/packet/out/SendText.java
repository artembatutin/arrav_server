package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendText implements OutgoingPacket {
	
	private final int id;
	private final String text;
	
	public SendText(int id, String text) {
		this.id = id;
		this.text = text;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(126, GamePacketType.VARIABLE_SHORT);
		buf.putCString(text);
		buf.putShort(id, ByteTransform.A);
		buf.endVarSize();
		buf.endVarSize();
		return buf;
	}
}
