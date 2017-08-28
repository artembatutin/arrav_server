package net.edge.world.entity.actor.combat;

import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.hit.Hit;

import java.util.List;

public interface CombatEffect {
	
	default boolean canEffect(Actor attacker, Actor defender, Hit hit) {
		return true;
	}
	
	void execute(Actor attacker, Actor defender, Hit hit, List<Hit> hits);
}
