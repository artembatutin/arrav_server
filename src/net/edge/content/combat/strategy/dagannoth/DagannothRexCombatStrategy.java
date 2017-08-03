package net.edge.content.combat.strategy.dagannoth;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.entity.actor.Actor;

import java.util.Arrays;
import java.util.Objects;

public final class DagannothRexCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		return character.isNpc() && victim.isPlayer();
	}
	
	@Override
	public void incomingAttack(Actor character, Actor attacker, CombatHit data) {
		if(data.getType().equals(CombatType.RANGED) || data.getType().equals(CombatType.MELEE)) {
			attacker.toPlayer().message("Your attacks are completely blocked...");
			Arrays.stream(data.getHits()).filter(Objects::nonNull).forEach(h -> h.setAccurate(false));
			return;
		}
	}

	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
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
		return new int[]{2883};
	}

}
