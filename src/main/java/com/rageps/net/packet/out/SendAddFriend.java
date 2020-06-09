package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendAddFriend implements OutgoingPacket {

	private final long username;
	private int world;
	private boolean display;

	public SendAddFriend(long username, int world, boolean display) {
		this.username = username;
		this.world = world;
		this.display = display;
	}
	public SendAddFriend(long username, int world) {
		this(username, world, true);
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(50);
		world = world != 0 ? world + 9 : world;
		out.putLong(username);
		out.put(world);
		out.put(display ? 1 : 0);
		return out;
	}
}
