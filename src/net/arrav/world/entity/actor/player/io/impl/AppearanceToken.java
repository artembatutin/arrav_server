package net.arrav.world.entity.actor.player.io.impl;

import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.PlayerAppearance;
import net.arrav.world.entity.actor.player.io.PlayerIOToken;

import java.nio.MappedByteBuffer;

public class AppearanceToken extends PlayerIOToken {

	@Override
	public void encode(Player p, MappedByteBuffer buf) {
		for(int i = 0; i < PlayerAppearance.SIZE; i++) {
			buf.putInt(p.getAppearance().getValues()[i]);
		}
	}

	@Override
	public void decode(Player p, MappedByteBuffer buf) {
		int[] a = new int[PlayerAppearance.SIZE];
		for(int i = 0; i < PlayerAppearance.SIZE; i++) {
			a[i] = buf.getInt();
		}
		p.getAppearance().setValues(a);
	}

	@Override
	public int offset() {
		return PlayerAppearance.SIZE * 4;
	}
}
