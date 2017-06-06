package net.edge.content.combat;

import net.edge.util.Stopwatch;
import net.edge.world.node.NodeState;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * A cache of players who have inflicted damage on another player in a combat
 * session.
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatDamage {
	
	/**
	 * The tool.mapviewer of players who have inflicted damage.
	 */
	private final Map<EntityNode, DamageCounter> attackers = new HashMap<>();
	
	/**
	 * Registers damage in the backing collection for {@code character}. This
	 * method has no effect if the character isn't a {@code PLAYER} or if
	 * {@code amount} is below {@code 0}.
	 * @param character the character to register damage for.
	 * @param amount    the amount of damage to register.
	 */
	public void add(EntityNode character, int amount) {
		if(amount > 0) {
			DamageCounter counter = attackers.putIfAbsent(character, new DamageCounter(amount));
			if(counter != null)
				counter.incrementAmount(amount);
		}
	}
	
	/**
	 * Determines which player in the backing collection has inflicted the most
	 * damage.
	 * @return the player who has inflicted the most damage, or an empty
	 * optional if there are no entries.
	 */
	public Optional<Npc> getNpcKiller() {
		int amount = 0;
		Npc killer = null;
		for(Entry<EntityNode, DamageCounter> entry : attackers.entrySet()) {
			DamageCounter counter = entry.getValue();
			EntityNode entity = entry.getKey();
			
			if(!entity.isNpc() || entity.isDead() || entity.getState() != NodeState.ACTIVE || counter.isTimeout() || !entity.getPosition().withinDistance(entity.getPosition(), 25))
				continue;
			if(counter.getAmount() > amount) {
				amount = counter.getAmount();
				killer = entity.toNpc();
			}
		}
		return Optional.ofNullable(killer);
	}
	
	/**
	 * Determines which player in the backing collection has inflicted the most
	 * damage.
	 * @return the player who has inflicted the most damage, or an empty
	 * optional if there are no entries.
	 */
	public Optional<Player> getPlayerKiller() {
		int amount = 0;
		Player killer = null;
		for(Entry<EntityNode, DamageCounter> entry : attackers.entrySet()) {
			DamageCounter counter = entry.getValue();
			EntityNode entity = entry.getKey();
			
			if(!entity.isPlayer() || entity.isDead() || entity.getState() != NodeState.ACTIVE || counter.isTimeout() || !entity.getPosition().withinDistance(entity.getPosition(), 25))
				continue;
			if(counter.getAmount() > amount) {
				amount = counter.getAmount();
				killer = entity.toPlayer();
			}
		}
		return Optional.ofNullable(killer);
	}
	
	/**
	 * Determines which entity in the backing collection has inflicted the most
	 * damage.
	 * @return the player who has inflicted the most damage, or an empty
	 * optional if there are no entries.
	 */
	public Optional<EntityNode> calculateProperKiller() {
		int amount = 0;
		EntityNode killer = null;
		for(Entry<EntityNode, DamageCounter> entry : attackers.entrySet()) {
			DamageCounter counter = entry.getValue();
			EntityNode entity = entry.getKey();
			
			if(entity.isDead() || entity.getState() != NodeState.ACTIVE || counter.isTimeout() || !entity.getPosition().withinDistance(entity.getPosition(), 25))
				continue;
			if(counter.getAmount() > amount) {
				amount = counter.getAmount();
				killer = entity;
			}
		}
		return Optional.ofNullable(killer);
	}
	
	/**
	 * Clears all data from the backing collection.
	 */
	public void clear() {
		attackers.clear();
	}
	
	/**
	 * A counter that will track the amount of damage dealt and whether that
	 * damaged has timed out or not.
	 * @author lare96 <http://github.com/lare96>
	 */
	private static final class DamageCounter {
		
		/**
		 * The amount of damage within this counter.
		 */
		private int amount;
		
		/**
		 * The stopwatch that will determine when a timeout occurs.
		 */
		private final Stopwatch stopwatch = new Stopwatch().reset();
		
		/**
		 * Creates a new {@link DamageCounter}.
		 * @param amount the amount of damage within this counter.
		 */
		public DamageCounter(int amount) {
			this.amount = amount;
		}
		
		/**
		 * Gets the amount of damage within this counter.
		 * @return the amount of damage.
		 */
		public int getAmount() {
			return amount;
		}
		
		/**
		 * Increments the amount of damage within this counter.
		 * @param amount the amount to increment by.
		 */
		public void incrementAmount(int amount) {
			if(this.isTimeout()) {
				this.amount = 0;
			}
			this.amount += amount;
			this.stopwatch.reset();
		}
		
		/**
		 * Determines if this counter has timed out or not.
		 * @return {@code true} if this counter has timed out, {@code false}
		 * otherwise.
		 */
		public boolean isTimeout() {
			return stopwatch.elapsed(CombatConstants.DAMAGE_CACHE_TIMEOUT, TimeUnit.SECONDS);
		}
	}
}
