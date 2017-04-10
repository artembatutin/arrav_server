package net.edge.world.model.node.entity.npc.strategy;

import net.edge.world.content.combat.CombatSessionData;
import net.edge.world.content.combat.strategy.CombatStrategy;
import net.edge.world.model.node.entity.EntityNode;
import net.edge.world.model.node.entity.npc.Npc;

/**
 * Represents a dynamic combat strategy which will <b>not</b> be initialised on start-up to a set
 * of npcs. This class should be used to chain combat strategies to certain npcs when they are
 * spawned.
 * <p></p>
 * The use of this combat strategy is to be used when you need to define explicit attributes
 * for a variety of npcs who use the same combat strategy. This class also supplies a
 * npc object rather then a numerical value.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class DynamicCombatStrategy<T extends Npc> implements CombatStrategy {
	
	/**
	 * The npc this combat strategy is for.
	 */
	protected final T npc;
	
	/**
	 * Constructs a new {@link DynamicCombatStrategy}.
	 * @param npc {@link #npc}.
	 */
	public DynamicCombatStrategy(T npc) {
		this.npc = npc;
	}
	
	/**
	 * Determines if {@link #npc} can attack the specified {@code victim}.
	 * @param victim the victim to determine this for.
	 * @return {@code true} if the {@link #npc} can, {@code false} otherwise.
	 */
	public abstract boolean canOutgoingAttack(EntityNode victim);
	
	/**
	 * Executed when {@link #npc} has passed the initial {@code canAttack}
	 * check and is about to attack {@code victim}.
	 * @param victim the character being attacked.
	 * @return a container holding the data for the attack.
	 */
	public abstract CombatSessionData outgoingAttack(EntityNode victim);
	
	/**
	 * Executed when the {@link #npc} is hit by the {@code attacker}.
	 * @param attacker the attacker whom hit the character.
	 * @param data     the combat session data chained to this hit.
	 */
	public abstract void incomingAttack(EntityNode attacker, CombatSessionData data);
	
	/**
	 * Determines the delay for when {@link #npc} will attack.
	 * @return the value that the attack timer should be reset to.
	 */
	public abstract int attackDelay();
	
	/**
	 * Determines how close {@link #npc} must be to attack.
	 * @return the radius that the npc must be in to attack.
	 */
	public abstract int attackDistance();
	
	@Override
	public final boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return canOutgoingAttack(victim);
	}
	
	@Override
	public final CombatSessionData outgoingAttack(EntityNode character, EntityNode victim) {
		return outgoingAttack(victim);
	}
	
	@Override
	public final void incomingAttack(EntityNode character, EntityNode attacker, CombatSessionData data) {
		incomingAttack(attacker, data);
	}
	
	@Override
	public final int attackDelay(EntityNode character) {
		return attackDelay();
	}
	
	@Override
	public final int attackDistance(EntityNode character) {
		return attackDistance();
	}
	
	@Override
	public final int[] getNpcs() {
		return null;
	}
}
