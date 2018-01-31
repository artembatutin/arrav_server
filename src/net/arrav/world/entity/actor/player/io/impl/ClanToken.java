package net.arrav.world.entity.actor.player.io.impl;

import net.arrav.content.clanchat.ClanManager;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.io.PlayerIOToken;

import java.nio.MappedByteBuffer;

public class ClanToken extends PlayerIOToken {

	private static final int CLAN_SIZE = 17;

	@Override
	public void encode(Player p, MappedByteBuffer buf) {
		putString(buf, p.getClan().map(clanMember -> clanMember.getClan().getOwner()).orElse(""), CLAN_SIZE);
	}

	@Override
	public void decode(Player p, MappedByteBuffer buf) {
		String clan = getString(buf);
		if(clan.length() > 0)
			ClanManager.get().join(p, clan);
	}

	@Override
	public int offset() {
		return CLAN_SIZE;
	}
}
