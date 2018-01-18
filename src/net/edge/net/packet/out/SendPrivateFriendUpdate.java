package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendPrivateFriendUpdate implements OutgoingPacket {
	
	private final long name;
	private final boolean online;
	
	public SendPrivateFriendUpdate(long name, boolean online) {
		this.name = name;
		this.online = online;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		int value = online ? 1 : 0;
		if(value != 0)
			value += 9;
		buf.message(50);
		buf.putLong(name);
		buf.put(value);
		return buf;
	}
}
