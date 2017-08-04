package net.edge.content.combat;

import net.edge.task.TaskListener;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.World;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.MobAggression;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

import java.util.concurrent.TimeUnit;

/**
 * Controls and gives access to the main parts of the combat process such as
 * starting and ending combat sessions.
 * @author lare96 <http://github.com/lare96>
 */
public final class Combat {
	
	/**
	 * The combat type this character is attacking with.
	 */
	private CombatType combatType;
	
	/**
	 * The character in control of this combat builder.
	 */
	private final Actor character;
	
	/**
	 * The current character that the controller is attacking.
	 */
	private Actor currentVictim;
	
	/**
	 * The last character that attacked the controller.
	 */
	private Actor aggressor;
	
	/**
	 * The task that handles the entire combat process.
	 */
	private CombatTask combatTask;
	
	/**
	 * The combat strategy this character attacking with.
	 */
	private CombatStrategy strategy;
	
	/**
	 * The task that handles the pre-combat process.
	 */
	private CombatDistanceListener distanceTask;
	
	/**
	 * The cache of damage dealt to this controller during combat.
	 */
	private final CombatDamage damageCache = new CombatDamage();
	
	/**
	 * The timer that controls how long this character must wait to attack.
	 */
	private int attackTimer;
	
	/**
	 * The cooldown timer used when the character breaks the combat session.
	 */
	private int cooldown;
	
	/**
	 * Creates a new {@link Combat}.
	 * @param character the character in control of this combat builder.
	 */
	public Combat(Actor character) {
		this.character = character;
	}
	
	/**
	 * Prompts the controller to attack {@code target}. If the controller is
	 * already attacking the target this method has no effect.
	 * @param target the character that this controller will be prompted to attack.
	 */
	public void attack(Actor target) {
		if(character.same(target)) {
			character.getMovementQueue().reset();
			return;
		}
		if(character.isPlayer() && target.isNpc() && character.toPlayer().getRights().equals(Rights.ADMINISTRATOR)) {
			character.toPlayer().message("[DEBUG NPC ID] Mob = " + target.toMob().getId() + ", position = " + target.toMob().getPosition().toString());
		}
		//if(target.same(currentVictim)) {
		//	determineStrategy();
		//	if(new Boundary(character.getPosition(), character.size()).within(currentVictim.getPosition(), currentVictim.size(), strategy.attackDistance(character))) {
		//		character.getMovementQueue().reset();
		//	}
		//}
		if(character.isPlayer() && target.isNpc()) {
			Mob mob = target.toMob();
			Player player = (Player) character;
			if(mob.getOwner() != -1 && !mob.isOwner(player)) {
				player.message("I should mind my own business...");
				character.getMovementQueue().reset();
				return;
			}
		}
		
		character.getMovementQueue().follow(target);
		if(combatTask != null && combatTask.isRunning()) {
			currentVictim = target;
			if(character.isPlayer()) {
				Player player = (Player) character;
				if(player.isAutocast() || player.getCastSpell() == null || attackTimer < 1) {
					cooldown = 0;
				}
			}
			return;
		}
		if(distanceTask != null && distanceTask.isRunning())
			distanceTask.cancel();
		distanceTask = new CombatDistanceListener(this, target);
		World.get().submit(distanceTask);
	}
	
	/**
	 * Instantly executes the combat task regardless of its state. Should really
	 * only be used for instant special attacks.
	 */
	public void instant() {
		combatTask.execute();
	}
	
	/**
	 * Resets this builder by discarding various values associated with the
	 * combat process.
	 */
	public void reset() {
		if(distanceTask != null)
			distanceTask.cancel();
		if(combatTask != null)
			combatTask.cancel();
		if(currentVictim != null) {
			Actor ag = currentVictim.getCombat().getAggressor();
			if(ag != null && ag.same(character))
				ag.getCombat().setAggressor(null);
		}
		aggressor = null;
		currentVictim = null;
		combatTask = null;
		combatType = null;
		strategy = null;
		cooldown = 0;
		attackTimer = 0;
		character.faceEntity(null);
		character.setFollowing(false);
		if(character.isNpc() && !character.isDead()) {
			character.getMovementQueue().smartWalk(character.toMob().getOriginalPosition());
		}
		
	}
	
	/**
	 * Resets the attack timer to it's original value based on the combat
	 * strategy.
	 */
	public void resetAttackTimer() {
		if(strategy == null)
			return;
		attackTimer = strategy.attackDelay(character);
	}
	
	/**
	 * Sets the attack timer to a value of {@code 0}.
	 */
	void clearAttackTimer() {
		attackTimer = 0;
	}
	
	/**
	 * Starts the cooldown sequence for this controller.
	 * @param resetAttack if the attack timer should be reset.
	 */
	public void cooldown(boolean resetAttack) {
		if(strategy == null)
			return;
		cooldown = 10;
		character.setFollowing(false);
		if(resetAttack) {
			attackTimer = strategy.attackDelay(character);
		}
	}
	
	/**
	 * Calculates and sets the combat strategy.
	 */
	public void determineStrategy() {
		this.strategy = character.determineStrategy();
	}
	
	/**
	 * Resets the combat strategy.
	 */
	public void resetStrategy() {
		this.strategy = null;
	}
	
	/**
	 * Determines if this character is attacking another character.
	 * @return {@code true} if this character is attacking another character,
	 * {@code false} otherwise.
	 */
	public boolean isAttacking() {
		return currentVictim != null;
	}
	
	/**
	 * Determines if this character is being attacked by another character.
	 * @return {@code true} if this character is being attacked by another
	 * character, {@code false} otherwise.
	 */
	public boolean isBeingAttacked() {
		return aggressor != null || pjingCheck();
	}
	
	/**
	 * Normally used for the game RuneScape, Pjing mean when in a PKing zone (Player Killing zone) and
	 * someone just died and the person who killed the guy is low on health, a "PJer" comes and kill the
	 * person because he's on low health and the PJer is normally full health.
	 * @return pjing check condition.
	 */
	public boolean pjingCheck() {
		return !character.getLastCombat().elapsed(5, TimeUnit.SECONDS) && character.inWilderness();
	}
	
	/**
	 * Determines if this character is being attacked by or attacking another
	 * character.
	 * @return {@code true} if this player is in combat, {@code false}
	 * otherwise.
	 */
	public boolean inCombat() {
		return isAttacking() || isBeingAttacked();
	}
	
	/**
	 * Determines if this combat builder is in cooldown mode.
	 * @return {@code true} if this combat builder is in cooldown mode,
	 * {@code false} otherwise.
	 */
	public boolean isCooldown() {
		return cooldown > 0;
	}
	
	/**
	 * Gets the cooldown timer used when the character breaks the combat
	 * session.
	 * @return the cooldown timer.
	 */
	public int getCooldown() {
		return cooldown;
	}
	
	/**
	 * Decrements the cooldown timer used when the character breaks the combat
	 * session.
	 */
	public void decrementCooldown() {
		cooldown--;
	}
	
	/**
	 * Gets the timer that controls how long this character must wait to attack.
	 * @return the timer determines when the controller attacks.
	 */
	public int getAttackTimer() {
		return attackTimer;
	}
	
	/**
	 * Decrements the timer that controls how long this character must wait to
	 * attack.
	 */
	public void decrementAttackTimer() {
		attackTimer--;
	}
	
	/**
	 * Gets the character in control of this combat builder.
	 * @return the character in control.
	 */
	public Actor getCharacter() {
		return character;
	}
	
	/**
	 * Gets the current character that the controller is attacking.
	 * @return the character the controller is attacking
	 */
	public Actor getVictim() {
		return currentVictim;
	}
	
	/**
	 * Gets the last character that attacked the controller.
	 * @return the last character that attacked.
	 */
	public Actor getAggressor() {
		return aggressor;
	}
	
	/**
	 * Sets the value for {@link Combat#aggressor}.
	 * @param aggressor the new value to set.
	 */
	void setAggressor(Actor aggressor) {
		this.aggressor = aggressor;
	}
	
	/**
	 * Gets the combat strategy this character attacking with.
	 * @return the combat strategy.
	 */
	public CombatStrategy getStrategy() {
		return strategy;
	}
	
	/**
	 * Gets the combat type this character is attacking with.
	 * @return the combat type.
	 */
	public CombatType getCombatType() {
		if(combatType == null) {
			return CombatType.NONE;
		}
		return combatType;
	}
	
	/**
	 * Sets the combat type this character is attacking with.
	 * @param type the type to set.
	 */
	public void setCombatType(CombatType type) {
		this.combatType = type;
	}
	
	/**
	 * Gets the task that handles the entire combat process.
	 * @return the task for the combat process.
	 */
	public CombatTask getCombatTask() {
		return combatTask;
	}
	
	/**
	 * Gets the cache of damage dealt to this controller during combat.
	 * @return the cache of damage.
	 */
	public CombatDamage getDamageCache() {
		return damageCache;
	}
	
	/**
	 * An {@link TaskListener} implementation that is used to listen for the
	 * controller to become in proper range of the victim.
	 * @author lare96 <http://github.com/lare96>
	 */
	private static final class CombatDistanceListener extends TaskListener {
		
		/**
		 * The combat builder owned by the controller.
		 */
		private final Combat builder;
		
		/**
		 * The victim that will be listened for.
		 */
		private final Actor victim;
		
		/**
		 * Create a new {@link CombatDistanceListener}.
		 * @param builder the combat builder owned by the controller.
		 * @param victim  the victim that will be listened for.
		 */
		CombatDistanceListener(Combat builder, Actor victim) {
			super.attach(builder.getCharacter().isPlayer() ? builder.getCharacter().toPlayer() : builder.getCharacter().toMob());
			this.builder = builder;
			this.victim = victim;
		}
		
		@Override
		public boolean canRun() {
			builder.determineStrategy();
			builder.attackTimer = 0;
			builder.cooldown = 0;
			if(!builder.character.getPosition().withinDistance(victim.getPosition(), builder.character.getViewingDistance())) {
				builder.reset();
				this.cancel();
				if(builder.character.isNpc()) {
					Mob mob = builder.character.toMob();
					MobAggression.retreat(mob);
				}
				return false;
			}
			
			if(!builder.getCharacter().inMulti() && victim.getCombat().isBeingAttacked() && !victim.getCombat().getAggressor().same(builder.getCharacter())) {
				if(builder.getCharacter().isPlayer()) {
					Player player = builder.getCharacter().toPlayer();
					player.message("They are already under attack!");
					player.getMovementQueue().reset();
				}
				builder.reset();
				this.cancel();
				return false;
			}
			return true;
			//return new Boundary(builder.character.getPosition(), builder.character.size()).within(victim.getPosition(), victim.size(), builder.strategy.attackDistance(builder.getActor()));
		}
		
		@Override
		public void run() {
			builder.getCharacter().getMovementQueue().reset();
			builder.currentVictim = victim;
			
			if(builder.combatTask == null || !builder.combatTask.isRunning()) {
				builder.combatTask = new CombatTask(builder);
				World.get().submit(builder.combatTask);
			}
		}
	}
}
