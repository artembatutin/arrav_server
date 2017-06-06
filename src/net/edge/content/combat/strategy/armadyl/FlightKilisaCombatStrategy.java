package net.edge.content.combat.strategy.armadyl;

import net.edge.content.combat.CombatSessionData;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;
import net.edge.world.node.entity.npc.impl.gwd.KreeArra;

/**
 * Represents the flight kilisa combat strategy.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class FlightKilisaCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return victim.isPlayer() && KreeArra.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatSessionData outgoingAttack(EntityNode character, EntityNode victim) {
		character.animation(new Animation(character.toNpc().getDefinition().getAttackAnimation()));
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
		return new int[]{6227};
	}

}
