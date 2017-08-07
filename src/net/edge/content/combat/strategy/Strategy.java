package net.edge.content.combat.strategy;

import net.edge.content.combat.CombatHit;
import net.edge.world.entity.actor.Actor;

/**
 * The blueprint of a combat session that determines how a character will act in combat.
 * @author lare96 <http://github.com/lare96>
 */
public interface Strategy {
	
	/**
	 * Executed when the {@code actor} is hit by the {@code attacker}.
	 * @param actor the actor being hit.
	 * @param attacker  the attacker whom hit the actor.
	 * @param data      the combat session data chained to this hit.
	 */
	default void incomingAttack(Actor actor, Actor attacker, CombatHit data) {
		
	}

	/**
	 * Executed when the {@code actor} is hit by the {@code attacker}.
	 * @param actor	the actor being hit.
	 * @param attacker	the attacker whom hit the actor.
	 */
	default boolean canIncomingAttack(Actor actor, Actor attacker) {
		return true;
	}

	/**
	 * Determines if {@code actor} is able to make an attack on
	 * {@code victim}.
	 * @param actor the actor to has if able.
	 * @param victim    the victim of the attack.
	 * @return {@code true} if an attack can be made, {@code false} otherwise.
	 */
	boolean canOutgoingAttack(Actor actor, Actor victim);
	
	/**
	 * Executed when {@code actor} has passed the initial {@code canAttack}
	 * check and is about to attack {@code victim}.
	 * @param actor the actor that is attacking.
	 * @param victim    the actor being attacked.
	 * @return a container holding the data for the attack.
	 */
	CombatHit outgoingAttack(Actor actor, Actor victim);
	
	/**
	 * Determines the delay for when {@code actor} will attack.
	 * @param actor the actor waiting to attack.
	 * @return the value that the attack timer should be reset to.
	 */
	int attackDelay(Actor actor);
	
	/**
	 * Determines how close {@code actor} must be to attack.
	 * @param actor the actor that is attacking.
	 * @return the radius that the actor must be in to attack.
	 */
	int attackDistance(Actor actor);
	
	/**
	 * The NPCs that will be assigned this combat strategy.
	 * @return the array of assigned NPCs.
	 */
	int[] getMobs();
}
