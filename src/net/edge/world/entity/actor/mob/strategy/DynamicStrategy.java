package net.edge.world.entity.actor.mob.strategy;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.strategy.Strategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

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
public abstract class DynamicStrategy<T extends Mob> implements Strategy {
	
	/**
	 * The npc this combat strategy is for.
	 */
	protected final T npc;
	
	/**
	 * Constructs a new {@link DynamicStrategy}.
	 * @param npc {@link #npc}.
	 */
	public DynamicStrategy(T npc) {
		this.npc = npc;
	}
	
	/**
	 * Determines if {@link #npc} can attack the specified {@code victim}.
	 * @param victim the victim to determine this for.
	 * @return {@code true} if the {@link #npc} can, {@code false} otherwise.
	 */
	public abstract boolean canOutgoingAttack(Actor victim);

	/**
	 * Determines if {@link #npc} can be attacked by the specified {@code attacker}.
	 * @param attacker	the attacker who is trying to attack.
	 * @return {@code true} if {@code attacker} can
	 */
	public boolean canIncomingAttack(Actor attacker) {
		return true;
	}

	/**
	 * Executed when {@link #npc} has passed the initial {@code canAttack}
	 * check and is about to attack {@code victim}.
	 * @param victim the character being attacked.
	 * @return a container holding the data for the attack.
	 */
	public abstract CombatHit outgoingAttack(Actor victim);
	
	/**
	 * Executed when the {@link #npc} is hit by the {@code attacker}.
	 * @param attacker the attacker whom hit the character.
	 * @param data     the combat session data chained to this hit.
	 */
	public abstract void incomingAttack(Actor attacker, CombatHit data);
	
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
	public final boolean canOutgoingAttack(Actor actor, Actor victim) {
		return canOutgoingAttack(victim);
	}

	@Override
	public final boolean canIncomingAttack(Actor actor, Actor attacker) {
		return canIncomingAttack(attacker);
	}

	@Override
	public final CombatHit outgoingAttack(Actor actor, Actor victim) {
		return outgoingAttack(victim);
	}
	
	@Override
	public final void incomingAttack(Actor actor, Actor attacker, CombatHit data) {
		incomingAttack(attacker, data);
	}
	
	@Override
	public final int attackDelay(Actor actor) {
		return attackDelay();
	}
	
	@Override
	public final int attackDistance(Actor actor) {
		return attackDistance();
	}
	
	@Override
	public final int[] getMobs() {
		return null;
	}
}
