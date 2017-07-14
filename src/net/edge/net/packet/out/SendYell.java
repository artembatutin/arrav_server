package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

public final class SendYell implements OutgoingPacket {
	
	private final String author, message;
	private final Rights rank;
	
	public SendYell(String author, String message, Rights rank) {
		this.author = author;
		this.message = message;
		this.rank = rank;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(210, PacketType.VARIABLE_BYTE);
		msg.putCString(author);
		msg.putCString(message);
		msg.putShort(rank.getProtocolValue());
		msg.endVarSize();
		return msg.getBuffer();
	}
}
