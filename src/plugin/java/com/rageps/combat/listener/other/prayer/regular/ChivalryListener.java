package com.rageps.combat.listener.other.prayer.regular;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.Actor;
import com.rageps.combat.listener.SimplifiedListener;

public class ChivalryListener extends SimplifiedListener<Player> {

	@Override
	public int modifyAttackLevel(Player attacker, Actor defender, int level) {
		return level * 23 / 20;
	}

	@Override
	public int modifyStrengthLevel(Player attacker, Actor defender, int level) {
		return level * 59 / 50;
	}

	@Override
	public int modifyDefenceLevel(Actor attacker, Player defender, int level) {
		return level * 6 / 5;
	}

}
