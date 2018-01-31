package net.arrav.world.entity.actor.player.io.impl;

import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.io.PlayerIOToken;

import java.nio.MappedByteBuffer;

public class MutedToken extends PlayerIOToken {

	@Override
	public void encode(Player p, MappedByteBuffer buf) {
		putBoolean(buf, p.muted);
	}

	@Override
	public void decode(Player p, MappedByteBuffer buf) {
		p.muted = getBoolean(buf);
		if(HostManager.contains(p.credentials.username, HostListType.MUTED_IP)) {
			p.ipMuted = true;
		}
	}

	@Override
	public int offset() {
		return 1;
	}
}
