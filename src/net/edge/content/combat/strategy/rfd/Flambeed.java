package net.edge.content.combat.strategy.rfd;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;

public final class Flambeed implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return character.isNpc() && victim.isPlayer();
	}

	@Override
	public CombatHit outgoingAttack(EntityNode character, EntityNode victim) {
		Animation animation = new Animation(character.toNpc().getDefinition().getAttackAnimation());
		character.animation(animation);
		return new CombatHit(character, victim, 1, CombatType.MELEE, true);
	}

	@Override
	public int attackDelay(EntityNode character) {
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(EntityNode character) {
		return 3;
	}

	@Override
	public int[] getNpcs() {
		return new int[]{3494};
	}

}
