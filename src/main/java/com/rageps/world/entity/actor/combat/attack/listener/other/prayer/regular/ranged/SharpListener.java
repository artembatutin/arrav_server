package com.rageps.world.entity.actor.combat.attack.listener.other.prayer.regular.ranged;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.attack.listener.SimplifiedListener;

public class SharpListener extends SimplifiedListener<Player> {
	
	@Override
	public int modifyRangedLevel(Player attacker, Actor defender, int damage) {
		return damage * 21 / 20;
	}
	
}
