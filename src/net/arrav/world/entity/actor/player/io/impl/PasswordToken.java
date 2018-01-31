package net.arrav.world.entity.actor.player.io.impl;

import net.arrav.net.codec.login.LoginCode;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.io.PlayerIOToken;

import java.nio.MappedByteBuffer;

public class PasswordToken extends PlayerIOToken {

	private static final int PASSWORD_SIZE = 17;

	@Override
	public void encode(Player p, MappedByteBuffer buf) {
		putString(buf, p.credentials.password, PASSWORD_SIZE);
	}

	@Override
	public void decode(Player p, MappedByteBuffer buf) {
		//not used.
	}

	@Override
	public LoginCode check(Player p, MappedByteBuffer buf) {
		String pass = getString(buf);
		if(!pass.equals(p.credentials.password)) {
			return LoginCode.INVALID_CREDENTIALS;
		}
		return LoginCode.NORMAL;
	}

	@Override
	public int offset() {
		return PASSWORD_SIZE;
	}
}
