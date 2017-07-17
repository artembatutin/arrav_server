package net.edge.content.combat.strategy.bandos;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.node.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.node.actor.mob.impl.gwd.GeneralGraardor;

public final class SergeantStrongstackCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		return victim.isPlayer() && GeneralGraardor.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
		character.animation(new Animation(6154));
		return new CombatHit(character, victim, 1, CombatType.MELEE, true);
	}

	@Override
	public int attackDelay(Actor character) {
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(Actor character) {
		return 1;
	}

	@Override
	public int[] getNpcs() {
		return new int[]{6261};
	}

}
