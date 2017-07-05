package net.edge.content.combat.strategy.rfd;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;

public final class AgrithNaNa implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return character.isNpc() && victim.isPlayer();
	}

	@Override
	public CombatHit outgoingAttack(EntityNode character, EntityNode victim) {
		character.animation(new Animation(3501));
		return new CombatHit(character, victim, 1, CombatType.MELEE, false);
	}

	@Override
	public int attackDelay(EntityNode character) {
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(EntityNode character) {
		return 2;
	}

	@Override
	public int[] getNpcs() {
		return new int[]{3493};
	}

}
