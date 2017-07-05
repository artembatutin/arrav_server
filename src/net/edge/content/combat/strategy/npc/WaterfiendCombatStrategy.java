package net.edge.content.combat.strategy.npc;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.node.entity.EntityNode;

public final class WaterfiendCombatStrategy implements CombatStrategy {
	
	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return character.isNpc() && victim.isPlayer();
	}
	
	@Override
	public CombatHit outgoingAttack(EntityNode character, EntityNode victim) {
		return new CombatHit(character, victim, CombatType.MELEE, true);//FIXME waterfiend uses ranged and magic.
	}
	
	@Override
	public int attackDelay(EntityNode character) {
		return character.getAttackSpeed();
	}
	
	@Override
	public int attackDistance(EntityNode character) {
		return 6;
	}
	
	@Override
	public int[] getNpcs() {
		return new int[]{5361};
	}
	
}
