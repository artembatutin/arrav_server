package net.arrav.world.entity.actor.combat.attack.listener.other.prayer.regular.magic;

import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.attack.listener.SimplifiedListener;
import net.arrav.world.entity.actor.player.Player;

public class MysticMightListener extends SimplifiedListener<Player> {
	
	@Override
	public int modifyMagicLevel(Player attacker, Actor defender, int level) {
		return level * 23 / 20;
	}
	
}
