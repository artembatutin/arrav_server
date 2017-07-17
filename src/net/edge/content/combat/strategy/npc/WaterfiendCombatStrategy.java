package net.edge.content.combat.strategy.npc;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.entity.actor.Actor;

public final class WaterfiendCombatStrategy implements CombatStrategy {
	
	@Override
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		return character.isNpc() && victim.isPlayer();
	}
	
	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
		return new CombatHit(character, victim, CombatType.MELEE, true);//FIXME waterfiend uses ranged and magic.
	}
	
	@Override
	public int attackDelay(Actor character) {
		return character.getAttackSpeed();
	}
	
	@Override
	public int attackDistance(Actor character) {
		return 6;
	}
	
	@Override
	public int[] getNpcs() {
		return new int[]{5361};
	}
	
}
