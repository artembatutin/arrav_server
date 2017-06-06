package net.edge.content.combat.strategy;

import net.edge.content.combat.CombatSessionData;
import net.edge.world.node.entity.EntityNode;

/**
 * The blueprint of a combat session that determines how a character will act in
 * combat.
 * @author lare96 <http://github.com/lare96>
 */
public interface CombatStrategy {
	
	/**
	 * Executed when the {@code character} is hit by the {@code attacker}.
	 * @param character the character being hit.
	 * @param attacker  the attacker whom hit the character.
	 * @param data      the combat session data chained to this hit.
	 */
	default void incomingAttack(EntityNode character, EntityNode attacker, CombatSessionData data) {
		
	}
	
	/**
	 * Determines if {@code character} is able to make an attack on
	 * {@code victim}.
	 * @param character the character to has if able.
	 * @param victim    the character being attacked.
	 * @return {@code true} if an attack can be made, {@code false} otherwise.
	 */
	boolean canOutgoingAttack(EntityNode character, EntityNode victim);
	
	/**
	 * Executed when {@code character} has passed the initial {@code canAttack}
	 * check and is about to attack {@code victim}.
	 * @param character the character that is attacking.
	 * @param victim    the character being attacked.
	 * @return a container holding the data for the attack.
	 */
	CombatSessionData outgoingAttack(EntityNode character, EntityNode victim);
	
	/**
	 * Determines the delay for when {@code character} will attack.
	 * @param character the character waiting to attack.
	 * @return the value that the attack timer should be reset to.
	 */
	int attackDelay(EntityNode character);
	
	/**
	 * Determines how close {@code character} must be to attack.
	 * @param character the character that is attacking.
	 * @return the radius that the character must be in to attack.
	 */
	int attackDistance(EntityNode character);
	
	/**
	 * The NPCs that will be assigned this combat strategy.
	 * @return the array of assigned NPCs.
	 */
	int[] getNpcs();
}
