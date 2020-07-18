package com.rageps.world.entity.actor;

import com.google.common.base.Preconditions;
import com.rageps.world.*;
import com.rageps.world.attr.AttributeMap;
import com.rageps.world.entity.actor.combat.Combat;
import com.rageps.world.entity.actor.combat.CombatUtil;
import com.rageps.world.entity.actor.combat.effect.CombatEffectType;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.combat.strategy.CombatStrategy;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.move.ForcedMovement;
import com.rageps.world.entity.actor.move.MovementQueue;
import com.rageps.world.entity.actor.move.MovementQueueListener;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.actor.update.UpdateFlag;
import com.rageps.world.entity.actor.update.UpdateFlagHolder;
import com.rageps.world.entity.region.Region;
import com.rageps.world.locale.Position;
import com.rageps.task.Task;
import com.rageps.util.MutableNumber;
import com.rageps.util.Stopwatch;
import com.rageps.world.entity.Entity;
import com.rageps.world.entity.EntityType;
import com.rageps.world.locale.loc.Locations;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * The {@link Entity} implementation representing a node that is an entity. This includes {@link Player}s and {@link Mob}s.
 * @author lare96 <http://github.com/lare96>
 */
public abstract class Actor extends Entity {

	private Hit primaryHit;

	private Hit secondaryHit;
	
	/**
	 * The current teleport stage that this player is in.
	 */
	private int teleportStage;
	
	/**
	 * An {@link AttributeMap} instance assigned to this {@code Actor}.
	 */
	private final AttributeMap attributeMap = new AttributeMap();


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
	private final Stopwatch freezeTimer = new Stopwatch(), stunTimer = new Stopwatch();
	
	/**
	 * A saved position of the previous region to be moved from.
	 */
	protected Position regionChanged;
	
	/**
	 * The slot this entity has been assigned to.
	 */
	private int slot = -1;
	
	/**
	 * The flag that determines if this entity is visible or not.
	 */
	private boolean visible = true;
	
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
	 * The current location	of this entity.
	 */
	private Locations.Location location;
	
	/**
	 * The current coordinates being face by this entity.
	 */
	private Position facePosition;
	
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
	 * Creates a new {@link Actor}.
	 * @param position the position of this entity in the world.
	 * @param type the type of node that this entity is.
	 */
	public Actor(Position position, EntityType type) {
		super(position, type);
		setPosition(position);
		this.autoRetaliate = (type == EntityType.NPC);
	}
	
	public abstract void preUpdate();
	
	public abstract void update();
	
	public void postUpdate() {
		//Updating the region that the actor has entered.
		if(regionChanged != null) {
			Region prev = World.getRegions().getRegion(regionChanged.getRegion());
			if(prev != null) {
				prev.remove(this);
			}
			Region next = World.getRegions().getRegion(getPosition().getRegion());
			if(next != null) {
				next.add(this);
			}
			regionChanged = null;
		}
		primaryDirection = Direction.NONE;
		secondaryDirection = Direction.NONE;
		needsRegionUpdate = false;
		needsPlacement = false;
		animation = null;
		flags.clear();
		primaryHit = null;
		secondaryHit = null;
	}
	
	/**
	 * Sets the value for {@link Entity#position}.
	 * @param position the new value to set.
	 */
	@Override
	public void setPosition(Position position) {
		//Adding region change if the actor entered another one.
		if(getSlot() != -1 && getPosition() != null && getPosition().getRegion() != position.getRegion()) {
			regionChanged = getPosition();
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
		else if(isMob())
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

	public int getMaxHP() {
		return this.isMob() ? toMob().getMaxHealth() : toPlayer().getMaximumHealth();

	}
	
	/**
	 * Decrements this entity's health based on {@code hit}.
	 * @param hit the hit to decrement this entity's health by.
	 * @return the modified hit after the health was decremented.
	 */
	public abstract Hit decrementHealth(Hit hit);
	
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
		return toMob().getDefinition().getSize();
	}
	
	/**
	 * Gets the center position for this entity node.
	 * @return the center position for this entity node.
	 */
	public final Position getCenterPosition() {
		if(isPlayer()) {
			return this.getPosition();
		}
		return new Position((this.getPosition().getX() + toMob().getDefinition().getSize() / 2), this.getPosition().getY() + (toMob().getDefinition().getSize() / 2));
	}
	
	/**
	 * Executes {@code animation} for this entity.
	 * @param animation the animation to execute, or {@code null} to reset the current
	 * animation.
	 */
	public final void animation(Animation animation) {
		if(animation == null)
			animation = new Animation(65535, Animation.AnimationPriority.HIGH);
		if(this.animation == null || this.animation.getPriority().getValue() <= animation.getPriority().getValue()) {
			this.animation = animation.copy();
			flags.flag(UpdateFlag.ANIMATION);
		}
	}
	
	public final void animation(int animation) {
		animation(new Animation(animation));
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
	
	public final void graphic(int graphic) {
		graphic(new Graphic(graphic));
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
	 * Deals a series of hits to this entity.
	 * @param hits the hits to deal to this entity.
	 */
	public final void damage(Hit... hits) {
		for(Hit hit : hits) {
			getCombat().queueDamage(hit);
		}
	}
	
	/**
	 * Writes the {@code hit} to the {@code primary} and {@code secondary} hit
	 * update variables.
	 * @param hit the hit to write
	 */
	public final void writeDamage(Hit hit) {
		if(primaryHit == null) {
			primaryHit = decrementHealth(hit);
			getFlags().flag(UpdateFlag.PRIMARY_HIT);
		} else if(secondaryHit == null) {
			secondaryHit = decrementHealth(hit);
			getFlags().flag(UpdateFlag.SECONDARY_HIT);
		}
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
	 * Sends a delayed task for this player.
	 */
	public void taskA(int delay, Consumer<Actor> action) {
		Actor p = this;
		new Task(delay, false) {
			@Override
			protected void execute() {
				action.accept(p);
				cancel();
			}
		}.submit();
	}
	
	/**
	 * @return The {@link AttributeMap} instance assigned to this {@code MobileEntity}.
	 */
	public final AttributeMap getAttributeMap() {
		return attributeMap;
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
	 * @param region this flag descibes if the entity needs to have region map update.
	 */
	public final void setUpdates(boolean needsPlacement, boolean region) {
		this.needsPlacement = needsPlacement;
		this.needsRegionUpdate = region;
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
	
	/**
	 * Sets the value for {@link Actor#dead}.
	 * @param dead the new value to set.
	 */
	public void setDead(boolean dead) {
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
	 * Gets the combat handler of this {@link Actor}.
	 * @return combat handler.
	 */
	public abstract Combat<? extends Actor> getCombat();
	
	public abstract <T extends Actor> CombatStrategy<? super T> getStrategy();
	
	/**
	 * Gets a combat bonus.
	 * @param index bonus index.
	 * @return bonus amount.
	 */
	public abstract int getBonus(int index);
	
	/**
	 * Appends a bonus.
	 * @param index bonus index.
	 * @param bonus bonus amount appended.
	 */
	public abstract void appendBonus(int index, int bonus);
	
	/**
	 * Gets the skill level of the actor.
	 * @param skill skill id.
	 * @return the statistic skill.
	 */
	public abstract int getSkillLevel(int skill);
	
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
	 * Checks if this actor can append any more hits for this update cycle.
	 * @return {@code true} if the actor can add more hits
	 */
	public boolean canAppendHit() {
		return primaryHit == null || secondaryHit == null;
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
	public final boolean isMob() {
		return getType() == EntityType.NPC;
	}
	
	/**
	 * Executes the specified action if the underlying node is a player.
	 * @param action the action to execute.
	 */
	public final void ifNpc(Consumer<Mob> action) {
		if(!this.isMob())
			return;
		action.accept(this.toMob());
	}
	
	/**
	 * Casts the {@link Actor} to a {@link Mob}.
	 * @return an instance of this {@link Actor} as a {@link Mob}.
	 */
	public final Mob toMob() {
		Preconditions.checkArgument(isMob(), "Cannot cast this entity to npc.");
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
	 * Get's this actors name
	 * @return The name
	 */
	public Object getName() {
		if(isMob())
			return this.toMob().getName();
		return this.toPlayer().credentials.username;
	}

    public Locations.Location getLocation() {
		return location;
	}

	public void setLocation(Locations.Location location) {
		this.location = location;
	}
}
