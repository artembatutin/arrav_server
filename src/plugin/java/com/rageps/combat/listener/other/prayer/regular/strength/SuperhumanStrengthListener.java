package com.rageps.combat.listener.other.prayer.regular.strength;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.Actor;
import com.rageps.combat.listener.SimplifiedListener;

public class SuperhumanStrengthListener extends SimplifiedListener<Player> {

	@Override
	public int modifyStrengthLevel(Player attacker, Actor defender, int damage) {
		return damage * 11 / 10;
	}

}