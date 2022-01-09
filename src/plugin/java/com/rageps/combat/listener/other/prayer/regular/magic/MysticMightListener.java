package com.rageps.combat.listener.other.prayer.regular.magic;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.Actor;
import com.rageps.combat.listener.SimplifiedListener;

public class MysticMightListener extends SimplifiedListener<Player> {
	
	@Override
	public int modifyMagicLevel(Player attacker, Actor defender, int level) {
		return level * 23 / 20;
	}
	
}
