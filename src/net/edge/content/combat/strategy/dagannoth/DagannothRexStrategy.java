package net.edge.content.combat.strategy.dagannoth;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.Strategy;
import net.edge.world.entity.actor.Actor;

import java.util.Arrays;
import java.util.Objects;

public final class DagannothRexStrategy implements Strategy {

	@Override
	public boolean canOutgoingAttack(Actor actor, Actor victim) {
		return actor.isMob() && victim.isPlayer();
	}
	
	@Override
	public void incomingAttack(Actor actor, Actor attacker, CombatHit data) {
		if(data.getType().equals(CombatType.RANGED) || data.getType().equals(CombatType.MELEE)) {
			attacker.toPlayer().message("Your attacks are completely blocked...");
			Arrays.stream(data.getHits()).filter(Objects::nonNull).forEach(h -> h.setAccurate(false));
			return;
		}
	}

	@Override
	public CombatHit outgoingAttack(Actor actor, Actor victim) {
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
		return new int[]{2883};
	}

}
