package com.rageps.combat.listener.other.prayer.regular.ranged;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.Actor;
import com.rageps.combat.listener.SimplifiedListener;

public class SharpListener extends SimplifiedListener<Player> {
	
	@Override
	public int modifyRangedLevel(Player attacker, Actor defender, int damage) {
		return damage * 21 / 20;
	}
	
}
