package net.edge.content.combat.strategy.rfd;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;

public final class Flambeed implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		return character.isNpc() && victim.isPlayer();
	}

	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
		Animation animation = new Animation(character.toMob().getDefinition().getAttackAnimation());
		character.animation(animation);
		return new CombatHit(character, victim, 1, CombatType.MELEE, true);
	}

	@Override
	public int attackDelay(Actor character) {
		return character.getAttackDelay();
	}

	@Override
	public int attackDistance(Actor character) {
		return 3;
	}

	@Override
	public int[] getMobs() {
		return new int[]{3494};
	}

}
