package net.arrav.world.entity.actor.combat.attack.listener.other.prayer.regular.ranged;

import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.attack.listener.SimplifiedListener;
import net.arrav.world.entity.actor.player.Player;

public class SharpListener extends SimplifiedListener<Player> {
	
	@Override
	public int modifyRangedLevel(Player attacker, Actor defender, int damage) {
		return damage * 21 / 20;
	}
	
}
