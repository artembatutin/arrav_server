package net.edge.world.content.combat.strategy.rfd;

import net.edge.world.content.combat.CombatSessionData;
import net.edge.world.content.combat.CombatType;
import net.edge.world.content.combat.strategy.CombatStrategy;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.model.Animation;

public final class Culinaromancer implements CombatStrategy {
	
	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return character.isNpc() && victim.isPlayer();
	}
	
	@Override
	public CombatSessionData outgoingAttack(EntityNode character, EntityNode victim) {
		character.animation(new Animation(15071));
		return new CombatSessionData(character, victim, 1, CombatType.MELEE, false);
	}
	
	@Override
	public int attackDelay(EntityNode character) {
		return character.getAttackSpeed();
	}
	
	@Override
	public int attackDistance(EntityNode character) {
		return 1;
	}
	
	@Override
	public int[] getNpcs() {
		return new int[]{3491};
	}
	
}
