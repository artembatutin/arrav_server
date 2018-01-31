package net.arrav.world.entity.actor.player.io.impl;

import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.io.PlayerIOToken;

import java.nio.MappedByteBuffer;

public class VotedToken extends PlayerIOToken {

	@Override
	public void encode(Player p, MappedByteBuffer buf) {
		buf.putInt(p.totalVotes);
	}

	@Override
	public void decode(Player p, MappedByteBuffer buf) {
		p.totalVotes = buf.getInt();
	}

	@Override
	public int offset() {
		return 4;
	}
}
