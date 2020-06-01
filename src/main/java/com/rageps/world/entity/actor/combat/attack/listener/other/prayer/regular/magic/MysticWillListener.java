package com.rageps.world.entity.actor.combat.attack.listener.other.prayer.regular.magic;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.attack.listener.SimplifiedListener;

public class MysticWillListener extends SimplifiedListener<Player> {
	
	@Override
	public int modifyMagicLevel(Player attacker, Actor defender, int level) {
		return level * 21 / 20;
	}
	
}
