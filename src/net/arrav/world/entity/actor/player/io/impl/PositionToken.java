package net.arrav.world.entity.actor.player.io.impl;

import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.io.PlayerIOToken;
import net.arrav.world.locale.Position;

import java.nio.MappedByteBuffer;

public class PositionToken extends PlayerIOToken {

	@Override
	public void encode(Player p, MappedByteBuffer buf) {
		buf.putShort((short) p.getPosition().getX());
		buf.putShort((short) p.getPosition().getY());
		buf.put((byte) p.getPosition().getZ());
	}

	@Override
	public void decode(Player p, MappedByteBuffer buf) {
		int x = buf.getShort();
		int y = buf.getShort();
		int z = buf.get();
		p.setPosition(new Position(x, y, z));
	}

	@Override
	public int offset() {
		return 5;
	}
}
