package net.edge.content.combat.strategy.bandos;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.Strategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.entity.actor.mob.impl.gwd.GeneralGraardor;

public final class SergeantStrongstackStrategy implements Strategy {

	@Override
	public boolean canOutgoingAttack(Actor actor, Actor victim) {
		return victim.isPlayer() && GeneralGraardor.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatHit outgoingAttack(Actor actor, Actor victim) {
		actor.animation(new Animation(6154));
		return new CombatHit(actor, victim, 1, CombatType.MELEE, true);
	}

	@Override
	public int attackDelay(Actor actor) {
		return actor.getAttackDelay();
	}

	@Override
	public int attackDistance(Actor actor) {
		return 1;
	}

	@Override
	public int[] getMobs() {
		return new int[]{6261};
	}

}
