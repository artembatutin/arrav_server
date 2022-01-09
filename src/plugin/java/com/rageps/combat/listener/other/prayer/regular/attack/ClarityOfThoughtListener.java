package com.rageps.combat.listener.other.prayer.regular.attack;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.Actor;
import com.rageps.combat.listener.SimplifiedListener;

public class ClarityOfThoughtListener extends SimplifiedListener<Player> {

	@Override
	public int modifyAttackLevel(Player attacker, Actor defender, int damage) {
		return damage * 21 / 20;
	}

}
