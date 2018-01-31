package net.arrav.world.entity.actor.player.io.impl;

import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.io.PlayerIOToken;

import java.nio.MappedByteBuffer;

public class DonatedToken extends PlayerIOToken {

	@Override
	public void encode(Player p, MappedByteBuffer buf) {
		buf.putInt(p.getTotalDonated(false));
	}

	@Override
	public void decode(Player p, MappedByteBuffer buf) {
		p.totalDonated = buf.getInt();
	}

	@Override
	public int offset() {
		return 4;
	}
}
