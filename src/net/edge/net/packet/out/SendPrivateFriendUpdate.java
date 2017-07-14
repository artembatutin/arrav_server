package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendPrivateFriendUpdate implements OutgoingPacket {
	
	private final long name;
	private final boolean online;
	
	public SendPrivateFriendUpdate(long name, boolean online) {
		this.name = name;
		this.online = online;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		int value = online ? 1 : 0;
		if(value != 0)
			value += 9;
		msg.message(50);
		msg.putLong(name);
		msg.put(value);
		return msg.getBuffer();
	}
}
