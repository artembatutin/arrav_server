package net.arrav.world.entity.actor.player.io.impl;

import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.io.PlayerIOToken;

import java.nio.MappedByteBuffer;

public class UsernameToken extends PlayerIOToken {

	private static final int USERNAME_SIZE = 17;

	@Override
	public void encode(Player p, MappedByteBuffer buf) {
		putString(buf, p.credentials.username, USERNAME_SIZE);
	}

	@Override
	public void decode(Player p, MappedByteBuffer buf) {
		p.credentials.username = getString(buf);
	}

	@Override
	public int offset() {
		return USERNAME_SIZE;
	}
}
