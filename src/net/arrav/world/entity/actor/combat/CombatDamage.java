package net.arrav.world.entity.actor.combat;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.arrav.util.Stopwatch;
import net.arrav.world.entity.EntityState;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

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
	 * The damages of players who have inflicted damage.
	 */
	private final Object2ObjectOpenHashMap<Actor, DamageCounter> attackers = new Object2ObjectOpenHashMap<>();

	private Hit lastHit;

	/**
	 * Registers damage in the backing collection for {@code character}. This
	 * method has no effect if the character isn't a {@code PLAYER} or if
	 * {@code amount} is below {@code 0}.
	 * @param character the character to register damage for.
	 * @param hit the hit to register.
	 */
	public void add(Actor character, Hit hit) {
		if(hit.getDamage() > 0) {
			DamageCounter counter = attackers.putIfAbsent(character, new DamageCounter(hit.getDamage()));
			if(counter != null)
				counter.incrementAmount(hit.getDamage());
			lastHit = hit;
		}
	}
	
	/**
	 * Determines which player in the backing collection has inflicted the most
	 * damage.
	 * @return the player who has inflicted the most damage, or an empty
	 * optional if there are no entries.
	 */
	public Optional<Mob> getNpcKiller() {
		int amount = 0;
		Mob killer = null;
		for(Entry<Actor, DamageCounter> entry : attackers.entrySet()) {
			DamageCounter counter = entry.getValue();
			Actor entity = entry.getKey();
			
			if(!entity.isMob() || entity.isDead() || entity.getState() != EntityState.ACTIVE || counter.isTimeout() || !entity.getPosition().withinDistance(entity.getPosition(), 25))
				continue;
			if(counter.getAmount() > amount) {
				amount = counter.getAmount();
				killer = entity.toMob();
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
		for(Entry<Actor, DamageCounter> entry : attackers.entrySet()) {
			DamageCounter counter = entry.getValue();
			Actor entity = entry.getKey();
			
			if(!entity.isPlayer() || entity.isDead() || entity.getState() != EntityState.ACTIVE || counter.isTimeout() || !entity.getPosition().withinDistance(entity.getPosition(), 25))
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
	public Optional<Actor> calculateProperKiller() {
		int amount = 0;
		Actor killer = null;
		for(Entry<Actor, DamageCounter> entry : attackers.entrySet()) {
			DamageCounter counter = entry.getValue();
			Actor entity = entry.getKey();
			
			if(entity.isDead() || entity.getState() != EntityState.ACTIVE || counter.isTimeout() || !entity.getPosition().withinDistance(entity.getPosition(), 25))
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
	 * Gets the last hit added to the damage map.
	 * @return the last hit
	 */
	public Hit getLastHit() {
		return lastHit;
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
