package net.edge.world.content.combat;

import net.edge.task.EventListener;
import net.edge.world.World;
import net.edge.world.content.combat.strategy.CombatStrategy;
import net.edge.world.locale.Boundary;
import net.edge.world.locale.Location;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.NpcAggression;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

import java.util.concurrent.TimeUnit;

/**
 * Controls and gives access to the main parts of the combat process such as
 * starting and ending combat sessions.
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatBuilder {
	
	/**
	 * The combat type this character is attacking with.
	 */
	private CombatType combatType;
	
	/**
	 * The character in control of this combat builder.
	 */
	private final EntityNode character;
	
	/**
	 * The current character that the controller is attacking.
	 */
	private EntityNode currentVictim;
	
	/**
	 * The last character that attacked the controller.
	 */
	private EntityNode aggressor;
	
	/**
	 * The task that handles the entire combat process.
	 */
	private CombatSession combatTask;
	
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
	 * Creates a new {@link CombatBuilder}.
	 * @param character the character in control of this combat builder.
	 */
	public CombatBuilder(EntityNode character) {
		this.character = character;
	}
	
	/**
	 * Prompts the controller to attack {@code target}. If the controller is
	 * already attacking the target this method has no effect.
	 * @param target the character that this controller will be prompted to attack.
	 */
	public void attack(EntityNode target) {
		
		if(character.equals(target))
			return;
		
		if(character.isPlayer() && target.isNpc() && character.toPlayer().getRights().equals(Rights.DEVELOPER)) {
			character.toPlayer().message("[DEBUG NPC ID] Npc = " + target.toNpc().getId() + ", position = " + target.toNpc().getPosition().toString());
		}
		if(target.equals(currentVictim)) {
			determineStrategy();
			if(new Boundary(character.getPosition(), character.size()).within(currentVictim.getPosition(), currentVictim.size(), strategy.attackDistance(character))) {
				character.getMovementQueue().reset();
			}
		}
		if(character.isPlayer() && target.isNpc()) {
			Npc npc = target.toNpc();
			Player player = (Player) character;
			if(!npc.isSpawnedFor(player) && npc.getSpawnedFor() != null) {
				player.message("I should mind my own business...");
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
		World.submit(distanceTask);
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
		currentVictim = null;
		combatTask = null;
		combatType = null;
		attackTimer = 0;
		strategy = null;
		cooldown = 0;
		character.faceEntity(null);
		character.setFollowing(false);
		if(character.isNpc() && !character.isDead()) {
			character.getMovementQueue().smartWalk(character.toNpc().getOriginalPosition());
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
		return pjingCheck();
	}
	
	/**
	 * Normally used for the game RuneScape, Pjing mean when in a PKing zone (Player Killing zone) and
	 * someone just died and the person who killed the guy is low on health, a "PJer" comes and kill the
	 * person because he's on low health and the PJer is normally full health.
	 * @return pjing check condition.
	 */
	public boolean pjingCheck() {
		return !character.getLastCombat().elapsed(5, TimeUnit.SECONDS);
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
	public EntityNode getCharacter() {
		return character;
	}
	
	/**
	 * Gets the current character that the controller is attacking.
	 * @return the character the controller is attacking
	 */
	public EntityNode getVictim() {
		return currentVictim;
	}
	
	/**
	 * Gets the last character that attacked the controller.
	 * @return the last character that attacked.
	 */
	public EntityNode getAggressor() {
		return aggressor;
	}
	
	/**
	 * Sets the value for {@link CombatBuilder#aggressor}.
	 * @param aggressor the new value to set.
	 */
	void setAggressor(EntityNode aggressor) {
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
	public CombatSession getCombatTask() {
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
	 * An {@link EventListener} implementation that is used to listen for the
	 * controller to become in proper range of the victim.
	 * @author lare96 <http://github.com/lare96>
	 */
	private static final class CombatDistanceListener extends EventListener {
		
		/**
		 * The combat builder owned by the controller.
		 */
		private final CombatBuilder builder;
		
		/**
		 * The victim that will be listened for.
		 */
		private final EntityNode victim;
		
		/**
		 * Create a new {@link CombatDistanceListener}.
		 * @param builder the combat builder owned by the controller.
		 * @param victim  the victim that will be listened for.
		 */
		CombatDistanceListener(CombatBuilder builder, EntityNode victim) {
			super.attach(builder.getCharacter().isPlayer() ? builder.getCharacter().toPlayer() : builder.getCharacter().toNpc());
			this.builder = builder;
			this.victim = victim;
		}
		
		@Override
		public boolean canRun() {
			builder.determineStrategy();
			builder.attackTimer = 0;
			builder.cooldown = 0;
			
			if(!builder.character.getPosition().isViewableFrom(victim.getPosition())) {
				builder.reset();
				this.cancel();
				return false;
			}
			
			if(!Location.inMultiCombat(builder.getCharacter()) && victim.getCombatBuilder().isBeingAttacked() && !victim.getCombatBuilder().getAggressor().equals(builder.getCharacter())) {
				if(builder.getCharacter().isPlayer())
					builder.getCharacter().toPlayer().message("They are already under attack!");
				builder.reset();
				this.cancel();
				return false;
			}
			
			if(builder.character.isNpc()) {
				Npc npc = builder.character.toNpc();
				if(!npc.getPosition().isViewableFrom(npc.getOriginalPosition()) && npc.getDefinition().retreats()) {
					NpcAggression.retreat(npc);
					this.cancel();
					return false;
				}
			}
			return new Boundary(builder.character.getPosition(), builder.character.size()).within(victim.getPosition(), victim.size(), builder.strategy.attackDistance(builder.getCharacter()));
		}
		
		@Override
		public void run() {
			builder.getCharacter().getMovementQueue().reset();
			builder.currentVictim = victim;
			
			if(builder.combatTask == null || !builder.combatTask.isRunning()) {
				builder.combatTask = new CombatSession(builder);
				World.submit(builder.combatTask);
			}
		}
	}
}
