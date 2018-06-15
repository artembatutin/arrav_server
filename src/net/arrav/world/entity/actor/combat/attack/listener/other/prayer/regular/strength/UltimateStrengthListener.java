package net.arrav.world.entity.actor.combat.attack.listener.other.prayer.regular.strength;

import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.attack.listener.SimplifiedListener;
import net.arrav.world.entity.actor.player.Player;

public class UltimateStrengthListener extends SimplifiedListener<Player> {

	@Override
	public int modifyStrengthLevel(Player attacker, Actor defender, int damage) {
		return damage * 23 / 20;
	}

}
