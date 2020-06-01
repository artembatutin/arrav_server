package com.rageps.world.entity.actor.combat.attack.listener.other.prayer.regular.attack;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.attack.listener.SimplifiedListener;

public class ClarityOfThoughtListener extends SimplifiedListener<Player> {

	@Override
	public int modifyAttackLevel(Player attacker, Actor defender, int damage) {
		return damage * 21 / 20;
	}

}
