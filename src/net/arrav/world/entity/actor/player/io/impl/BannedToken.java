package net.arrav.world.entity.actor.player.io.impl;

import net.arrav.net.codec.login.LoginCode;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.io.PlayerIOToken;

import java.nio.MappedByteBuffer;

public class BannedToken extends PlayerIOToken {

	@Override
	public void encode(Player p, MappedByteBuffer buf) {
		putBoolean(buf, p.banned);
	}

	@Override
	public void decode(Player p, MappedByteBuffer buf) {
		//not used.
	}

	@Override
	public LoginCode check(Player p, MappedByteBuffer buf) {
		p.banned = getBoolean(buf);
		if(p.banned) {
			return LoginCode.ACCOUNT_DISABLED;
		}
		return LoginCode.NORMAL;
	}

	@Override
	public int offset() {
		return 1;
	}
}
