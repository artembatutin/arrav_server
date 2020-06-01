package com.rageps.world.entity.actor.combat.attack.listener.other.prayer.regular.strength;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.attack.listener.SimplifiedListener;

public class BurstOfStrengthListener extends SimplifiedListener<Player> {

	@Override
	public int modifyStrengthLevel(Player attacker, Actor defender, int damage) {
		return damage * 21 / 20;
	}

}
