package net.edge.content.combat.strategy.bandos;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;
import net.edge.world.node.entity.npc.impl.gwd.GeneralGraardor;

public final class SergeantStrongstackCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return victim.isPlayer() && GeneralGraardor.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatHit outgoingAttack(EntityNode character, EntityNode victim) {
		character.animation(new Animation(6154));
		return new CombatHit(character, victim, 1, CombatType.MELEE, true);
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
		return new int[]{6261};
	}

}
