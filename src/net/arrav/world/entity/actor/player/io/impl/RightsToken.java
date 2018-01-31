package net.arrav.world.entity.actor.player.io.impl;

import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.actor.player.io.PlayerIOToken;
import net.arrav.world.locale.Position;

import java.nio.MappedByteBuffer;

public class RightsToken extends PlayerIOToken {

	@Override
	public void encode(Player p, MappedByteBuffer buf) {
		buf.put((byte) p.getRights().ordinal());
	}

	@Override
	public void decode(Player p, MappedByteBuffer buf) {
		p.setRights(Rights.getIndex(buf.get()));
	}

	@Override
	public int offset() {
		return 1;
	}
}
