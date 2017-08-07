package net.edge.content.combat.strategy.rfd;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;

public final class Flambeed implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(Actor actor, Actor victim) {
		return actor.isMob() && victim.isPlayer();
	}

	@Override
	public CombatHit outgoingAttack(Actor actor, Actor victim) {
		Animation animation = new Animation(actor.toMob().getDefinition().getAttackAnimation());
		actor.animation(animation);
		return new CombatHit(actor, victim, 1, CombatType.MELEE, true);
	}

	@Override
	public int attackDelay(Actor actor) {
		return actor.getAttackDelay();
	}

	@Override
	public int attackDistance(Actor actor) {
		return 3;
	}

	@Override
	public int[] getMobs() {
		return new int[]{3494};
	}

}
