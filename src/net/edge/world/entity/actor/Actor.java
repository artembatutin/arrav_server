package net.edge.world.entity.actor;

import com.google.common.base.Preconditions;
import net.edge.content.combat.Combat;
import net.edge.world.*;
import net.edge.task.Task;
import net.edge.util.MutableNumber;
import net.edge.util.Stopwatch;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.effect.CombatEffectType;
import net.edge.content.combat.magic.CombatSpell;
import net.edge.content.combat.magic.CombatWeaken;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.locale.Position;
import net.edge.world.entity.Entity;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.EntityType;
import net.edge.world.entity.actor.attribute.AttributeMap;
import net.edge.world.entity.actor.move.ForcedMovement;
import net.edge.world.entity.actor.move.MovementQueue;
import net.edge.world.entity.actor.move.MovementQueueListener;
import net.edge.world.entity.actor.move.path.AStarPathFinder;
import net.edge.world.entity.actor.move.path.distance.Manhattan;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;
import net.edge.world.entity.actor.update.UpdateFlag;
import net.edge.world.entity.actor.update.UpdateFlagHolder;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * The {@link Entity} implementation representing a node that is an entity. This includes {@link Player}s and {@link Mob}s.
 * @author lare96 <http://github.com/lare96>
 */
public abstract class Actor extends Entity {
	
	/**
	 * The current teleport stage that this player is in.
	 */
	private int teleportStage;
	
	/**
	 * An {@link AttributeMap} instance assigned to this {@code Actor}.
	 */
	protected final AttributeMap attr = new AttributeMap();
	
	/**
	 * The combat builder that will handle all combat operations for this entity.
	 */
	private final Combat combat = new Combat(this);
	
	/**
	 * The movement queue that will handle all movement processing for this entity.
	 */
	private final MovementQueue movementQueue = new MovementQueue(this);
	
	/**
	 * The movement queue listener that will allow for actions to be appended to
	 * the end of the movement queue.
	 */
	private final MovementQueueListener movementListener = new MovementQueueListener(this);
	
	/**
	 * An {@link UpdateFlagHolder} instance assigned to this {@code MobileEntity}.
	 */
	protected final UpdateFlagHolder flags = new UpdateFlagHolder();
	
	/**
	 * The collection of stopwatches used for various timing operations.
	 */
	private final Stopwatch lastCombat = new Stopwatch(), freezeTimer = new Stopwatch(), stunTimer = new Stopwatch();
	
	/**
	 * The slot this entity has been assigned to.
	 */
	private int slot = -1;
	
	/**
	 * The flag that determines if this entity is visible or not.
	 */
	private boolean visible = true;
	
	/**
	 * The aggression tick timer to not check npc aggression each tick.
	 */
	private int aggressionTick;
	
	/**
	 * The viewing distance of a {@link Actor}.
	 */
	private int viewingDistance = 30;
	
	/**
	 * The amount of poison damage this entity has.
	 */
	private final MutableNumber poisonDamage = new MutableNumber();
	
	/**
	 * The type of poison that was previously applied.
	 */
	private PoisonType poisonType;
	
	/**
	 * The primary movement direction of this entity.
	 */
	private Direction primaryDirection = Direction.NONE;
	
	/**
	 * The secondary movement direction of this entity.
	 */
	private Direction secondaryDirection = Direction.NONE;
	
	/**
	 * The last movement direction made by this entity.
	 */
	private Direction lastDirection = Direction.NONE;
	
	/**
	 * The forced movement.
	 */
	private ForcedMovement forcedMovement;
	
	/**
	 * The flag that determines if this entity needs placement.
	 */
	private boolean needsPlacement;
	
	/**
	 * The flag that determines if this entity needs a region update.
	 */
	private boolean needsRegionUpdate;
	
	/**
	 * The combat spell currently being casted by this entity.
	 */
	private CombatSpell currentlyCasting;
	
	/**
	 * The current animation being performed by this entity.
	 */
	private Animation animation;
	
	/**
	 * The current graphic being performed by this entity.
	 */
	private Graphic graphic;
	
	/**
	 * The current text being forced by this entity.
	 */
	private String forcedText;
	
	/**
	 * The current index being faced by this entity.
	 */
	private int faceIndex;
	
	/**
	 * The current coordinates being face by this entity.
	 */
	private Position facePosition;
	
	/**
	 * The current primary hit being dealt on this entity.
	 */
	private Hit primaryHit;
	
	/**
	 * The current secondary hit being dealt on this entity.
	 */
	private Hit secondaryHit;
	
	/**
	 * The last known region that this {@code Player} was in.
	 */
	private Position lastRegion;
	
	/**
	 * The delay representing how long this entity is frozen for.
	 */
	private long freezeDelay;
	
	/**
	 * The delay representing how long this entity is stunned for.
	 */
	private long stunDelay;
	
	/**
	 * The flag determining if this entity should fight back when attacked.
	 */
	private boolean autoRetaliate;
	
	/**
	 * The flag determining if this entity is following someone.
	 */
	private boolean following;
	
	/**
	 * The entity this entity is currently following.
	 */
	private Actor followentity;
	
	/**
	 * The flag determining if this entity is dead.
	 */
	private boolean dead;
	
	/**
	 * The pathfinder used by this {@link Actor}.
	 */
	private final AStarPathFinder aStarPathFinder;

	/**
	 * Creates a new {@link Actor}.
	 * @param position the position of this entity in the world.
	 * @param type     the type of node that this entity is.
	 */
	public Actor(Position position, EntityType type) {
		super(position, type);
		setPosition(position);
		this.autoRetaliate = (type == EntityType.NPC);
		this.aStarPathFinder = new AStarPathFinder(this, new Manhattan());
	}
	
	public abstract void preUpdate();
	
	public abstract void update();
	
	public void postUpdate() {
		primaryDirection = Direction.NONE;
		secondaryDirection = Direction.NONE;
		needsRegionUpdate = false;
		needsPlacement = false;
		animation = null;
		flags.clear();
	}
	
	/**
	 * Sets the value for {@link Entity#position}.
	 * @param position the new value to set.
	 */
	@Override
	public void setPosition(Position position) {
		//Updating the region if the entity entered another one.
		if(getSlot() != -1 && getPosition() != null && getPosition().getRegion() != position.getRegion()) {
			World.getRegions().getRegion(getPosition().getRegion()).removeChar(this);
			World.getRegions().getRegion(position.getRegion()).addChar(this);
		}
		super.setPosition(position);
	}
	
	public boolean same(Actor other) {
		return other != null && getType().equals(other.getType()) && getSlot() == other.getSlot();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(this == obj)
			return true;
		if(!(obj instanceof Actor))
			return false;
		Actor other = (Actor) obj;
		return this.same(other);
	}
	
	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + getType().ordinal();
		result = prime * result + getSlot();
		return result;
	}
	
	@Override
	public String toString() {
		if(slot == -1)
			return "unregistered entity node.";
		if(isPlayer())
			return World.get().getPlayers().get(slot - 1).toString();
		else if(isNpc())
			return World.get().getMobs().get(slot - 1).toString();
		throw new IllegalStateException("Invalid entity node type!");
	}
	
	/**
	 * Condition if the {@link Actor} is in multi combat area.
	 * @return multi area combat flag.
	 */
	public abstract boolean inMulti();
	
	/**
	 * Condition if the {@link Actor} is in wilderness area.
	 * @return wilderness area flag.
	 */
	public abstract boolean inWilderness();
	
	/**
	 * Condition if this {@link Actor} is active in the world.
	 * @return {@code true} if it is, false otherwise.
	 */
	public abstract boolean active();
	
	/**
	 * Attempts to move this entity node to the {@code destination}.
	 */
	public abstract void move(Position destination);
	
	/**
	 * The death appending method when the {@link Actor} is about to die.
	 */
	public abstract void appendDeath();
	
	/**
	 * Weakens this entity using {@code effect}.
	 * @param effect the effect to use to weaken this entity.
	 * @return {@code true} if the entity was weakened, {@code false}
	 * otherwise.
	 */
	public abstract boolean weaken(CombatWeaken effect);
	
	/**
	 * Gets a set of local players.
	 * @return local players
	 */
	public abstract Set<Player> getLocalPlayers();
	
	/**
	 * Gets a set of local mobs.
	 * @return local mobs
	 */
	public abstract Set<Mob> getLocalMobs();
	
	/**
	 * Gets the attack speed for this entity.
	 * @return the attack speed.
	 */
	public abstract int getAttackDelay();
	
	/**
	 * Gets this entity's current health.
	 * @return this charater's health.
	 */
	public abstract int getCurrentHealth();
	
	/**
	 * Decrements this entity's health based on {@code hit}.
	 * @param hit the hit to decrement this entity's health by.
	 * @return the modified hit after the health was decremented.
	 */
	public abstract Hit decrementHealth(Hit hit);
	
	/**
	 * Calculates and retrieves the combat strategy for this entity.
	 * @return the combat strategy.
	 */
	public abstract CombatStrategy determineStrategy();
	
	/**
	 * Gets the base attack level for this entity based on {@code type}, used
	 * for combat calculations.
	 * @param type the combat type.
	 * @return the base attack level.
	 */
	public abstract int getBaseAttack(CombatType type);
	
	/**
	 * Gets the base defence level for this entity based on {@code type},
	 * used for combat calculations.
	 * @param type the combat type.
	 * @return the base defence level.
	 */
	public abstract int getBaseDefence(CombatType type);
	
	/**
	 * Executed on a successful hit, used primarily for poison effects.
	 * @param entity the victim in this successful hit.
	 * @param type   the combat type currently being used.
	 */
	public abstract void onSuccessfulHit(Actor entity, CombatType type);
	
	/**
	 * Restores this entity's health level by {@code amount}.
	 * @param amount the amount to restore this health level by.
	 */
	public abstract void healEntity(int amount);
	
	/**
	 * Applies poison with an intensity of {@code type} to the entity.
	 * @param type the poison type to apply.
	 */
	public void poison(PoisonType type) {
		poisonType = type;
		CombatUtil.effect(this, CombatEffectType.POISON);
	}
	
	/**
	 * Calculates and returns the size of this entity.
	 * @return the size of this entity.
	 */
	public final int size() {
		if(isPlayer())
			return 1;
		return toNpc().getDefinition().getSize();
	}
	
	/**
	 * Gets the center position for this entity node.
	 * @return the center position for this entity node.
	 */
	public final Position getCenterPosition() {
		if(isPlayer()) {
			return this.getPosition();
		}
		return new Position((this.getPosition().getX() + toNpc().getDefinition().getSize() / 2), this.getPosition().getY() + (toNpc().getDefinition().getSize() / 2));
	}
	
	/**
	 * Executes {@code animation} for this entity.
	 * @param animation the animation to execute, or {@code null} to reset the current
	 *                  animation.
	 */
	public final void animation(Animation animation) {
		if(animation == null)
			animation = new Animation(65535, Animation.AnimationPriority.HIGH);
		if(this.animation == null || this.animation.getPriority().getValue() <= animation.getPriority().getValue()) {
			this.animation = animation.copy();
			flags.flag(UpdateFlag.ANIMATION);
		}
	}
	
	/**
	 * Executes {@code graphic} for this entity.
	 * @param graphic the graphic to execute.
	 */
	public final void graphic(Graphic graphic) {
		if(graphic == null) {
			graphic = new Graphic(-1);
		}
		this.graphic = graphic.copy();
		flags.flag(UpdateFlag.GRAPHIC);
	}
	
	/**
	 * Executes {@code forcedText} for this entity.
	 * @param forcedText the forced text to execute.
	 */
	public final void forceChat(String forcedText) {
		this.forcedText = forcedText;
		flags.flag(UpdateFlag.FORCE_CHAT);
	}
	
	/**
	 * Prompts this entity to face {@code entity}.
	 * @param entity the entity to face, or {@code null} to reset the face.
	 */
	public final void faceEntity(Actor entity) {
		this.faceIndex = entity == null ? 65535 : entity.isPlayer() ? entity.slot + 32768 : entity.slot;
		flags.flag(UpdateFlag.FACE_ENTITY);
	}
	
	/**
	 * Prompts this entity to face {@code position}.
	 * @param position the position to face.
	 */
	public final void facePosition(Position position) {
		if(this.isPlayer() && this.toPlayer().getActivityManager().contains(ActivityManager.ActivityType.FACE_POSITION)) {
			return;
		}
		if(position == null)
			facePosition = new Position(0, 0);
		else
			facePosition = new Position(2 * position.getX() + 1, 2 * position.getY() + 1);
		flags.flag(UpdateFlag.FACE_COORDINATE);
	}
	
	/**
	 * Attempts to view a direction.
	 * @param direction the direction to face.
	 */
	public final void faceDirection(Direction direction) {
		Position copy = new Position(this.getPosition().getX() + direction.getX(), this.getPosition().getY() + direction.getY());
		facePosition(copy);
	}
	
	/**
	 * Deals {@code hit} on this entity as a primary hitmark.
	 * @param hit the hit to deal on this entity.
	 */
	private void primaryDamage(Hit hit) {
		primaryHit = decrementHealth(Objects.requireNonNull(hit));
		flags.flag(UpdateFlag.PRIMARY_HIT);
	}
	
	/**
	 * Deals {@code hit} on this entity as a secondary hitmark.
	 * @param hit the hit to deal on this entity.
	 */
	private void secondaryDamage(Hit hit) {
		secondaryHit = decrementHealth(Objects.requireNonNull(hit));
		flags.flag(UpdateFlag.SECONDARY_HIT);
	}
	
	/**
	 * Deals a series of hits to this entity.
	 * @param hits the hits to deal to this entity.
	 */
	public final void damage(Hit... hits) {
		Preconditions.checkArgument(hits.length >= 1 && hits.length <= 4);
		switch(hits.length) {
			case 1:
				sendDamage(hits[0]);
				break;
			case 2:
				sendDamage(hits[0], hits[1]);
				break;
			case 3:
				sendDamage(hits[0], hits[1], hits[2]);
				break;
			case 4:
				sendDamage(hits[0], hits[1], hits[2], hits[3]);
				break;
		}
	}
	
	/**
	 * Deals {@code hit} to this entity.
	 * @param hit the hit to deal to this entity.
	 */
	private void sendDamage(Hit hit) {
		if(flags.get(UpdateFlag.PRIMARY_HIT)) {
			secondaryDamage(hit);
			return;
		}
		primaryDamage(hit);
	}
	
	/**
	 * Deals {@code hit} and {@code hit2} to this entity.
	 * @param hit  the first hit to deal to this entity.
	 * @param hit2 the second hit to deal to this entity.
	 */
	private void sendDamage(Hit hit, Hit hit2) {
		sendDamage(hit);
		secondaryDamage(hit2);
	}
	
	/**
	 * Deals {@code hit}, {@code hit2}, and {@code hit3} to this entity.
	 * @param hit  the first hit to deal to this entity.
	 * @param hit2 the second hit to deal to this entity.
	 * @param hit3 the third hit to deal to this entity.
	 */
	private void sendDamage(Hit hit, Hit hit2, Hit hit3) {
		sendDamage(hit, hit2);
		
		World.get().submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(getState() != EntityState.ACTIVE) {
					return;
				}
				sendDamage(hit3);
			}
		});
	}
	
	/**
	 * Deals {@code hit}, {@code hit2}, {@code hit3}, and {@code hit4} to this
	 * entity.
	 * @param hit  the first hit to deal to this entity.
	 * @param hit2 the second hit to deal to this entity.
	 * @param hit3 the third hit to deal to this entity.
	 * @param hit4 the fourth hit to deal to this entity.
	 */
	private void sendDamage(Hit hit, Hit hit2, Hit hit3, Hit hit4) {
		sendDamage(hit, hit2);
		
		World.get().submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(getState() != EntityState.ACTIVE) {
					return;
				}
				sendDamage(hit3, hit4);
			}
		});
	}
	
	/**
	 * Prepares to cast the {@code spell} on {@code victim}.
	 * @param spell  the spell to cast on the victim.
	 * @param victim the victim that the spell will be cast on.
	 * @return the delay of the hit depending on projectile.
	 */
	public final int prepareSpell(CombatSpell spell, Actor victim) {
		currentlyCasting = spell;
		return currentlyCasting.startCast(this, victim);
	}
	
	/**
	 * Freezes this entity for the desired time in {@code SECONDS}.
	 * @param time the time to freeze this entity for.
	 */
	public final void freeze(long time) {
		if(isFrozen())
			return;
		freezeDelay = time;
		freezeTimer.reset();
		movementQueue.reset();
	}
	
	/**
	 * Unfreezes this entity completely allowing them to reestablish
	 * movement.
	 */
	public final void unfreeze() {
		freezeDelay = 0;
		freezeTimer.stop();
	}
	
	/**
	 * Stuns this entity for the desired time in {@code SECONDS}.
	 * @param time the time to stun this entity for.
	 */
	public final void stun(long time) {
		if(isStunned())
			return;
		stunDelay = time;
		stunTimer.reset();
		movementQueue.reset();
	}
	
	/**
	 * Un-stuns this entity completely allowing them to reestablish
	 * movement.
	 */
	public final void unStun() {
		stunDelay = 0;
		stunTimer.stop();
	}
	
	/**
	 * @return The {@link AttributeMap} instance assigned to this {@code MobileEntity}.
	 */
	public final AttributeMap getAttr() {
		return attr;
	}
	
	/**
	 * Determines if this entity is poisoned.
	 * @return {@code true} if this entity is poisoned, {@code false}
	 * otherwise.
	 */
	public final boolean isPoisoned() {
		return poisonDamage.get() > 0;
	}
	
	/**
	 * Determines if this entity is frozen.
	 * @return {@code true} if this entity is frozen, {@code false}
	 * otherwise.
	 */
	public final boolean isFrozen() {
		return !freezeTimer.elapsed(freezeDelay, TimeUnit.SECONDS);
	}
	
	/**
	 * Determines if this entity is frozen.
	 * @return {@code true} if this entity is frozen, {@code false}
	 * otherwise.
	 */
	public final boolean isStunned() {
		return !stunTimer.elapsed(stunDelay, TimeUnit.SECONDS);
	}
	
	/**
	 * Gets the slot this entity has been assigned to.
	 * @return the slot of this entity.
	 */
	public final int getSlot() {
		return slot;
	}
	
	/**
	 * Sets the value for {@link Actor#slot}.
	 * @param slot the new value to set.
	 */
	public final void setSlot(int slot) {
		this.slot = slot;
		getRegion().addChar(this);
	}
	
	/**
	 * Gets the amount of poison damage this entity has.
	 * @return the amount of poison damage.
	 */
	public final MutableNumber getPoisonDamage() {
		return poisonDamage;
	}
	
	/**
	 * Gets the primary direction this entity is facing.
	 * @return the primary direction.
	 */
	public final Direction getPrimaryDirection() {
		return primaryDirection;
	}
	
	/**
	 * Sets the value for {@link Actor#primaryDirection}.
	 * @param primaryDirection the new value to set.
	 */
	public final void setPrimaryDirection(Direction primaryDirection) {
		this.primaryDirection = primaryDirection;
	}
	
	/**
	 * Gets the secondary direction this entity is facing.
	 * @return the secondary direction.
	 */
	public final Direction getSecondaryDirection() {
		return secondaryDirection;
	}
	
	/**
	 * Sets the value for {@link Actor#secondaryDirection}.
	 * @param secondaryDirection the new value to set.
	 */
	public final void setSecondaryDirection(Direction secondaryDirection) {
		this.secondaryDirection = secondaryDirection;
	}
	
	/**
	 * Gets the last direction this entity was facing.
	 * @return the last direction.
	 */
	public final Direction getLastDirection() {
		return lastDirection;
	}
	
	/**
	 * Sets the value for {@link Actor#lastDirection}.
	 * @param lastDirection the new value to set.
	 */
	public final void setLastDirection(Direction lastDirection) {
		this.lastDirection = lastDirection;
	}
	
	/**
	 * Gets the entity's forced movement.
	 * @return entity's forced movement.
	 */
	public ForcedMovement getForcedMovement() {
		return forcedMovement;
	}
	
	/**
	 * Sets the value for {@link Actor#forcedMovement}
	 * @param forcedMovement the new value to set.
	 */
	public void setForcedMovement(ForcedMovement forcedMovement) {
		this.forcedMovement = forcedMovement;
	}
	
	/**
	 * Determines if this entity needs region update.
	 * @return {@code true} if this entity needs region update, {@code false}
	 * otherwise.
	 */
	public final boolean isNeedsRegionUpdate() {
		return needsRegionUpdate;
	}
	
	/**
	 * Determines if this entity needs placement.
	 * @return {@code true} if this entity needs placement, {@code false}
	 * otherwise.
	 */
	public final boolean isNeedsPlacement() {
		return needsPlacement;
	}
	
	/**
	 * Sets a new value for {@link #needsPlacement}.
	 * @param needsPlacement new value to set.
	 */
	public final void setNeedsPlacement(boolean needsPlacement) {
		this.needsPlacement = needsPlacement;
	}
	
	/**
	 * Sets regional and placement updates on this {@link Actor}.
	 * @param needsPlacement this flag describes if the entity needs placement.
	 * @param region         this flag descibes if the entity needs to have region map update.
	 */
	public final void setUpdates(boolean needsPlacement, boolean region) {
		this.needsPlacement = needsPlacement;
		this.needsRegionUpdate = region;
	}
	
	/**
	 * Gets the spell that this entity is currently casting.
	 * @return the spell currently being casted.
	 */
	public final CombatSpell getCurrentlyCasting() {
		return currentlyCasting;
	}
	
	/**
	 * Sets the value for {@link Actor#currentlyCasting}.
	 * @param currentlyCasting the new value to set.
	 */
	public final void setCurrentlyCasting(CombatSpell currentlyCasting) {
		this.currentlyCasting = currentlyCasting;
	}
	
	/**
	 * Gets the last region this entity is in.
	 * @return the current region.
	 */
	public Position getLastRegion() {
		return lastRegion;
	}
	
	/**
	 * Gets the current teleport stage that this player is in.
	 * @return the teleport stage.
	 */
	public int getTeleportStage() {
		return teleportStage;
	}
	
	/**
	 * Checks if the player is teleporting.
	 * @return <true> if the player is, <false> otherwise.
	 */
	public boolean isTeleporting() {
		return teleportStage > 0 || teleportStage == -1;
	}
	
	/**
	 * Sets the value for {@link Player#teleportStage}.
	 * @param teleportStage the new value to set.
	 */
	public void setTeleportStage(int teleportStage) {
		this.teleportStage = teleportStage;
	}
	
	/**
	 * Sets the value for {@link Actor#lastRegion}.
	 * @param lastRegion the new value to set.
	 */
	public void setLastRegion(Position lastRegion) {
		this.lastRegion = lastRegion;
	}
	
	/**
	 * Determines if this entity is auto-retaliating.
	 * @return {@code true} if this entity is auto-retaliating, {@code false}
	 * otherwise.
	 */
	public final boolean isAutoRetaliate() {
		return autoRetaliate;
	}
	
	/**
	 * Sets the value for {@link Actor#autoRetaliate}.
	 * @param autoRetaliate the new value to set.
	 */
	public void setAutoRetaliate(boolean autoRetaliate) {
		this.autoRetaliate = autoRetaliate;
	}
	
	/**
	 * Determines if this entity is following someone.
	 * @return {@code true} if this entity is following someone,
	 * {@code false} otherwise.
	 */
	public final boolean isFollowing() {
		return following;
	}
	
	/**
	 * Sets the value for {@link Actor#following}.
	 * @param following the new value to set.
	 */
	public final void setFollowing(boolean following) {
		this.following = following;
	}
	
	/**
	 * Gets the entity that is currently being followed.
	 * @return the entity being followed.
	 */
	public final Actor getFollowEntity() {
		return followentity;
	}
	
	/**
	 * Sets the value for {@link Actor#followentity}.
	 * @param followentity the new value to set.
	 */
	public final void setFollowEntity(Actor followentity) {
		this.followentity = followentity;
	}
	
	/**
	 * Determines if this entity is dead or not.
	 * @return {@code true} if this entity is dead, {@code false} otherwise.
	 */
	public final boolean isDead() {
		return dead;
	}

	public final AStarPathFinder getAStarPathFinder() {
		return aStarPathFinder;
	}
	/**
	 * Sets the value for {@link Actor#dead}.
	 * @param dead the new value to set.
	 */
	public final void setDead(boolean dead) {
		this.dead = dead;
	}
	
	/**
	 * Determines if this entity is visible or not.
	 * @return {@code true} if this entity is visible, {@code false}
	 * otherwise.
	 */
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Sets the value for {@link Actor#visible}.
	 * @param visible the new value to set.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * Gets the combat builder that will handle all combat operations for this
	 * entity.
	 * @return the combat builder.
	 */
	public final Combat getCombat() {
		return combat;
	}
	
	/**
	 * Gets the movement queue that will handle all movement processing for this
	 * entity.
	 * @return the movement queue.
	 */
	public final MovementQueue getMovementQueue() {
		return movementQueue;
	}
	
	/**
	 * Gets the movement queue listener that will allow for actions to be
	 * appended to the end of the movement queue.
	 * @return the movement queue listener.
	 */
	public final MovementQueueListener getMovementListener() {
		return movementListener;
	}
	
	/**
	 * @return The {@link UpdateFlagHolder} instance assigned to this {@code Actor}.
	 */
	public final UpdateFlagHolder getFlags() {
		return flags;
	}
	
	/**
	 * Gets the timer that records the difference in time between now and the
	 * last time the player was in combat.
	 * @return the timer that determines when the player was last in combat.
	 */
	public final Stopwatch getLastCombat() {
		return lastCombat;
	}
	
	/**
	 * Gets the animation update block value.
	 * @return the animation update block value.
	 */
	public final Animation getAnimation() {
		return animation;
	}
	
	/**
	 * Gets the graphic update block value.
	 * @return the graphic update block value.
	 */
	public final Graphic getGraphic() {
		return graphic;
	}
	
	/**
	 * Gets the forced text update block value.
	 * @return the forced text update block value.
	 */
	public final String getForcedText() {
		return forcedText;
	}
	
	/**
	 * Gets the face index update block value.
	 * @return the face index update block value.
	 */
	public final int getFaceIndex() {
		return faceIndex;
	}
	
	/**
	 * Gets the face position update block value.
	 * @return the face position update block value.
	 */
	public final Position getFacePosition() {
		return facePosition;
	}
	
	/**
	 * Gets the primary hit update block value.
	 * @return the primary hit update block value.
	 */
	public final Hit getPrimaryHit() {
		return primaryHit;
	}
	
	/**
	 * Gets the secondary hit update block value.
	 * @return the secondary hit update block value.
	 */
	public final Hit getSecondaryHit() {
		return secondaryHit;
	}
	
	/**
	 * Gets the type of poison that was previously applied.
	 * @return the type of poison.
	 */
	public PoisonType getPoisonType() {
		return poisonType;
	}
	
	/**
	 * Sets the value for {@link Actor#poisonType}.
	 * @param poisonType the new value to set.
	 */
	public void setPoisonType(PoisonType poisonType) {
		this.poisonType = poisonType;
	}
	
	/**
	 * Determines if this entity is a {@link Player}.
	 * @return {@code true} if this entity is a {@link Player}, {@code false}
	 * otherwise.
	 */
	public final boolean isPlayer() {
		return getType() == EntityType.PLAYER;
	}
	
	/**
	 * Executes the specified action if the underlying node is a player.
	 * @param action the action to execute.
	 */
	public final void ifPlayer(Consumer<Player> action) {
		if(!this.isPlayer()) {
			return;
		}
		action.accept(this.toPlayer());
	}
	
	/**
	 * Casts the {@link Actor} to a {@link Player}.
	 * @return an instance of this {@link Actor} as a {@link Player}.
	 */
	public final Player toPlayer() {
		Preconditions.checkArgument(isPlayer(), "Cannot cast this entity to player.");
		return (Player) this;
	}
	
	/**
	 * Determines if this entity is a {@link Mob}.
	 * @return {@code true} if this entity is a {@link Mob}, {@code false}
	 * otherwise.
	 */
	public final boolean isNpc() {
		return getType() == EntityType.NPC;
	}
	
	/**
	 * Executes the specified action if the underlying node is a player.
	 * @param action the action to execute.
	 */
	public final void ifNpc(Consumer<Mob> action) {
		if(!this.isNpc())
			return;
		action.accept(this.toNpc());
	}
	
	/**
	 * Casts the {@link Actor} to a {@link Mob}.
	 * @return an instance of this {@link Actor} as a {@link Mob}.
	 */
	public final Mob toNpc() {
		Preconditions.checkArgument(isNpc(), "Cannot cast this entity to npc.");
		return (Mob) this;
	}
	
	/**
	 * Gets the viewing distance.
	 * @return viewing distance.
	 */
	public int getViewingDistance() {
		return viewingDistance;
	}
	
	/**
	 * Viewing distance.
	 * @param viewingDistance viewing distance.
	 */
	public void setViewingDistance(int viewingDistance) {
		this.viewingDistance = viewingDistance;
	}
	
	/**
	 * Gets the aggression tick timer.
	 * @return aggression tick timer.
	 */
	public int processAgressiveTick() {
		return aggressionTick;
	}
	
	/**
	 * Sets a new aggression tick timer.
	 * @param aggressionTick new value to set.
	 */
	public void setAggressionTick(int aggressionTick) {
		this.aggressionTick = aggressionTick;
	}
	
}
