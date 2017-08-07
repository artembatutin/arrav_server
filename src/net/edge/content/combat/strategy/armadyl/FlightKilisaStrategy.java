package net.edge.content.combat.strategy.armadyl;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.Strategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.entity.actor.mob.impl.gwd.KreeArra;

/**
 * Represents the flight kilisa combat strategy.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class FlightKilisaStrategy implements Strategy {

	@Override
	public boolean canOutgoingAttack(Actor actor, Actor victim) {
		return victim.isPlayer() && KreeArra.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatHit outgoingAttack(Actor actor, Actor victim) {
		actor.animation(new Animation(actor.toMob().getDefinition().getAttackAnimation()));
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
		return new int[]{6227};
	}

}
