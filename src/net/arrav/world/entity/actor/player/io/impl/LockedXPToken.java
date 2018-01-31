package net.arrav.world.entity.actor.player.io.impl;

import net.arrav.net.codec.login.LoginCode;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.io.PlayerIOToken;

import java.nio.MappedByteBuffer;

public class LockedXPToken extends PlayerIOToken {

	@Override
	public void encode(Player p, MappedByteBuffer buf) {
		putBoolean(buf, p.lockedXP);
	}

	@Override
	public void decode(Player p, MappedByteBuffer buf) {
		p.lockedXP = getBoolean(buf);
	}

	@Override
	public int offset() {
		return 1;
	}
}
