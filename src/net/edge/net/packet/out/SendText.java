package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendText implements OutgoingPacket {
	
	private final int id;
	private final String text;
	
	public SendText(int id, String text) {
		this.id = id;
		this.text = text;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(126, PacketType.VARIABLE_SHORT);
		msg.putCString(text);
		msg.putShort(id, ByteTransform.A);
		msg.endVarSize();
		msg.endVarSize();
		return msg.getBuffer();
	}
}
