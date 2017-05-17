package net.edge.world.content.combat.strategy.bandos;

import net.edge.world.content.combat.CombatSessionData;
import net.edge.world.content.combat.CombatType;
import net.edge.world.content.combat.strategy.CombatStrategy;
import net.edge.world.model.node.entity.EntityNode;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.npc.impl.gwd.GeneralGraardor;

public final class SergeantStrongstackCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return victim.isPlayer() && GeneralGraardor.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatSessionData outgoingAttack(EntityNode character, EntityNode victim) {
		character.animation(new Animation(6154));
		return new CombatSessionData(character, victim, 1, CombatType.MELEE, true);
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
