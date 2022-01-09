package com.rageps.combat.listener.other.prayer.regular.defence;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.Actor;
import com.rageps.combat.listener.SimplifiedListener;

public class SteelSkinListener extends SimplifiedListener<Player> {

	@Override
	public int modifyDefenceLevel(Actor attacker, Player defender, int damage) {
		return damage * 23 / 20;
	}

}
