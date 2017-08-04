package net.edge.content.combat.strategy.rfd;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;

public final class AgrithNaNa implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		return character.isMob() && victim.isPlayer();
	}

	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
		character.animation(new Animation(3501));
		return new CombatHit(character, victim, 1, CombatType.MELEE, false);
	}

	@Override
	public int attackDelay(Actor character) {
		return character.getAttackDelay();
	}

	@Override
	public int attackDistance(Actor character) {
		return 2;
	}

	@Override
	public int[] getMobs() {
		return new int[]{3493};
	}

}
