package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

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
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(196, PacketType.VARIABLE_BYTE);
		msg.putLong(name);
		msg.putInt(player.getPrivateMessage().getLastMessage().getAndIncrement());
		msg.put(rights);
		msg.putBytes(message, size);
		msg.endVarSize();
		return msg.getBuffer();
	}
}
