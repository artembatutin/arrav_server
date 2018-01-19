package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

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
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(196, GamePacketType.VARIABLE_BYTE);
		buf.putLong(name);
		buf.putInt(player.getPrivateMessage().getLastMessage().getAndIncrement());
		buf.put(rights);
		buf.putBytes(message, size);
		buf.endVarSize();
		return buf;
	}
}
