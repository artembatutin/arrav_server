package net.edge.content.combat.strategy.mob;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.Strategy;
import net.edge.world.entity.actor.Actor;

public final class WaterfiendStrategy implements Strategy {
	
	@Override
	public boolean canOutgoingAttack(Actor actor, Actor victim) {
		return actor.isMob() && victim.isPlayer();
	}
	
	@Override
	public CombatHit outgoingAttack(Actor actor, Actor victim) {
		return new CombatHit(actor, victim, CombatType.MELEE, true);//FIXME waterfiend uses ranged and magic.
	}
	
	@Override
	public int attackDelay(Actor actor) {
		return actor.getAttackDelay();
	}
	
	@Override
	public int attackDistance(Actor actor) {
		return 6;
	}
	
	@Override
	public int[] getMobs() {
		return new int[]{5361};
	}
	
}
