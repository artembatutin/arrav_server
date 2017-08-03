package net.edge.content.combat.strategy.armadyl;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.entity.actor.mob.impl.gwd.KreeArra;

/**
 * Represents the flight kilisa combat strategy.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class FlightKilisaCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		return victim.isPlayer() && KreeArra.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
		character.animation(new Animation(character.toNpc().getDefinition().getAttackAnimation()));
		return new CombatHit(character, victim, 1, CombatType.MELEE, true);
	}

	@Override
	public int attackDelay(Actor character) {
		return character.getAttackDelay();
	}

	@Override
	public int attackDistance(Actor character) {
		return 1;
	}

	@Override
	public int[] getMobs() {
		return new int[]{6227};
	}

}
