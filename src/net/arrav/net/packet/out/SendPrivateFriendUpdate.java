package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendPrivateFriendUpdate implements OutgoingPacket {
	
	private final long name;
	private final boolean online;
	
	public SendPrivateFriendUpdate(long name, boolean online) {
		this.name = name;
		this.online = online;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		int value = online ? 1 : 0;
		if(value != 0)
			value += 9;
		out.message(50);
		out.putLong(name);
		out.put(value);
		return out;
	}
}
