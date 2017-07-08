package net.edge.world.node.entity.npc;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.edge.content.combat.Combat;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.effect.CombatPoisonEffect;
import net.edge.content.combat.magic.CombatWeaken;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.locale.Position;
import net.edge.world.World;
import net.edge.world.node.NodeType;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Hit;
import net.edge.world.PoisonType;
import net.edge.world.node.entity.npc.impl.DefaultNpc;
import net.edge.world.node.entity.npc.impl.KalphiteQueen;
import net.edge.world.node.entity.npc.impl.corp.CorporealBeast;
import net.edge.world.node.entity.npc.impl.glacor.Glacor;
import net.edge.world.node.entity.npc.impl.gwd.CommanderZilyana;
import net.edge.world.node.entity.npc.impl.gwd.GeneralGraardor;
import net.edge.world.node.entity.npc.impl.gwd.KreeArra;
import net.edge.world.node.entity.npc.strategy.impl.TormentedDemonCombatStrategy;
import net.edge.world.node.entity.npc.strategy.impl.WildyWyrmCombatStrategy;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.update.UpdateFlag;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;

/**
 * The character implementation that represents a node that is operated by the server.
 * This type of node functions solely through the server executing functions.
 * @author Artem batutin <artembatutin@gmail.com>
 * @author lare96 <http://github.com/lare96>
 */
public abstract class Npc extends EntityNode {
	
	/**
	 * A mapping which contains all the custom npcs by their id.
	 */
	public static final Int2ObjectArrayMap<Function<Position, Npc>> CUSTOM_NPCS = new Int2ObjectArrayMap<>(
			ImmutableMap.<Integer, Function<Position, Npc>>builder()
					.put(6247, s -> new CommanderZilyana())
					.put(6260, s -> new GeneralGraardor())
					.put(6222, s -> new KreeArra())
					.put(8133, s -> new CorporealBeast())
					.put(1158, KalphiteQueen::new)
					.put(14301, Glacor::new).build());

	/**
	 * Gets a certain npc by the specified {@code id} and supplies it's position.
	 * @param id  the id to get the npc by.
	 * @param pos the position to supply this npc to.
	 * @return the npc.
	 */
	public static Npc getNpc(int id, Position pos) {
		if(id >= 8349 && id <= 8351) {
			Npc npc = new DefaultNpc(id, pos);
			return npc.setStrategy(Optional.of(new TormentedDemonCombatStrategy(npc)));
		}
		if(id == 3334) {
			Npc npc = new DefaultNpc(id, pos);
			return npc.setStrategy(Optional.of(new WildyWyrmCombatStrategy(npc)));
		}
		return CUSTOM_NPCS.containsKey(id) ? CUSTOM_NPCS.get(id).apply(pos).create() : new DefaultNpc(id, pos);
	}
	
	/**
	 * The identification for this NPC.
	 */
	private final int id;
	
	/**
	 * The maximum health of this NPC.
	 */
	private final int maxHealth;
	
	/**
	 * The original position that this NPC was created on.
	 */
	private final Position originalPosition;
	
	/**
	 * The movement coordinator for this NPC.
	 */
	private final NpcMovementCoordinator movementCoordinator = new NpcMovementCoordinator(this);
	
	/**
	 * The current health of this NPC.
	 */
	private int currentHealth;
	
	/**
	 * Determines if this NPC was originally random walking.
	 */
	private boolean originalRandomWalk;
	
	/**
	 * Determines if this NPC respawns.
	 */
	private boolean respawn;
	
	/**
	 * The spell that this NPC is weakened by.
	 */
	private CombatWeaken weakenedBy;
	
	/**
	 * The player slot this npc was spawned for.
	 */
	private int owner = -1;
	
	/**
	 * The transformation identifier.
	 */
	private OptionalInt transform = OptionalInt.empty();
	
	/**
	 * The flag determining if this {@link Npc} is active in his region.
	 */
	private boolean active = false;
	
	/**
	 * The flag determining if the {@link Npc} is a smart npc.
	 */
	private boolean smart;
	
	/**
	 * The special amount of this npc, between 0 and 100. 101 sets it off.
	 */
	private OptionalInt special = OptionalInt.empty();
	
	/**
	 * The cached field to determine this npcs dynamic combat strategy.
	 * <p></p>
	 * if this field is set, it'll use this strategy instead and each npc
	 * will have it's own strategy instead of sharing it on a global state.
	 */
	private Optional<CombatStrategy> strategy = Optional.empty();
	
	/**
	 * Creates a new {@link Npc}.
	 * @param id       the identification for this NPC.
	 * @param position the position of this character in the world.
	 */
	public Npc(int id, Position position) {
		super(position, NodeType.NPC);
		this.id = id;
		this.originalPosition = position.copy();
		this.maxHealth = getDefinition().getHitpoints();
		this.currentHealth = maxHealth;
		this.owner = -1;
		getMovementCoordinator().setRadius(3);
	}
	
	/**
	 * Creates the particular {@link Npc instance}.
	 * @return new {@link Npc} instance.
	 */
	public abstract Npc create();
	
	@Override
	public void register() {
		
	}
	
	@Override
	public void dispose() {
		setVisible(false);
		setPosition(new Position(1, 1));
		World.get().getTask().cancel(this);
		getRegion().removeChar(this);
	}
	
	@Override
	public void move(Position destination) {
		getMovementQueue().reset();
		super.setPosition(destination.copy());
		setUpdates(true, true);
	}
	
	@Override
	public void update() {
		//No sequencing.
	}
	
	@Override
	public void appendDeath() {
		setDead(true);
		World.get().submit(new NpcDeath(this));
	}
	
	@Override
	public Hit decrementHealth(Hit hit) {
		if(hit.getDamage() > currentHealth) {
			hit.setDamage(currentHealth);
			if(hit.getType() == Hit.HitType.CRITICAL) {
				hit.setType(Hit.HitType.NORMAL);
			}
		}
		currentHealth -= hit.getDamage();
		if(currentHealth <= 0) {
			setCurrentHealth(0);
		}
		return hit;
	}
	
	@Override
	public int getAttackSpeed() {
		return this.getDefinition().getAttackSpeed();
	}
	
	@Override
	public int getCurrentHealth() {
		return currentHealth;
	}
	
	@Override
	public CombatStrategy determineStrategy() {
		return strategy.orElse(Combat.determineStrategy(id));
	}
	
	@Override
	public int getBaseAttack(CombatType type) {
		int value;
		if(type == CombatType.MAGIC)
			value = getDefinition().getMagicLevel();
		else if(type == CombatType.RANGED)
			value = getDefinition().getRangedLevel();
		else
			value = getDefinition().getAttackLevel();
		if(weakenedBy == CombatWeaken.ATTACK_LOW || weakenedBy == CombatWeaken.ATTACK_HIGH)
			value -= (int) ((weakenedBy.getRate()) * (value));
		return value;
	}
	
	@Override
	public int getBaseDefence(CombatType type) {
		int value = getDefinition().getDefenceLevel();
		if(weakenedBy == CombatWeaken.DEFENCE_LOW || weakenedBy == CombatWeaken.DEFENCE_HIGH)
			value -= (int) ((weakenedBy.getRate()) * (value));
		return value;
	}
	
	@Override
	public void onSuccessfulHit(EntityNode victim, CombatType type) {
		if(getDefinition().isPoisonous())
			victim.poison(CombatPoisonEffect.getPoisonType(id).orElse(PoisonType.DEFAULT_NPC));
	}
	
	@Override
	public void healEntity(int damage) {
		if((currentHealth + damage) > maxHealth) {
			currentHealth = maxHealth;
			return;
		}
		currentHealth += damage;
	}
	
	@Override
	public boolean weaken(CombatWeaken effect) {
		if(weakenedBy == null) {
			weakenedBy = effect;
			return true;
		}
		return false;
	}
	
	/**
	 * Activates the {@code TRANSFORM} update mask for this non-player
	 * character.
	 * @param id the new npc to transform this npc into.
	 */
	public void transform(int id) {
		transform = OptionalInt.of(id);
		getFlags().flag(UpdateFlag.TRANSFORM);
	}
	
	/**
	 * Removes the {@code TRANSFORM} update mask for this non-player character.
	 */
	public void untransform() {
		transform = OptionalInt.empty();
		getFlags().flag(UpdateFlag.TRANSFORM);
	}

	/**
	 * Gets the identification of this NPC.
	 * @return the identification.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the {@link NpcType} of this npc.
	 * @return type.
	 */
	public NpcType getNpcType() {
		return NpcType.NONE;
	}
	
	/**
	 * Gets the maximum health of this NPC.
	 * @return the maximum health.
	 */
	public int getMaxHealth() {
		return maxHealth;
	}
	
	/**
	 * Sets the value for {@link Npc#currentHealth}.
	 * @param currentHealth the new value to set.
	 */
	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
		if(currentHealth == 0) {
			if(!isDead()) {
				appendDeath();
			}
		}
	}
	
	/**
	 * Gets the special amount for this NPC.
	 * @return the special amount.
	 */
	public OptionalInt getSpecial() {
		return special;
	}
	
	/**
	 * Sets the value for {@link Npc#special}.
	 * @param special the new value to set.
	 */
	public void setSpecial(int special) {
		this.special = OptionalInt.of(special);
	}
	
	/**
	 * Resets the {@link Npc#special}.
	 */
	public void resetSpecial() {
		this.special = OptionalInt.empty();
	}
	
	/**
	 * Gets the original position that this NPC was created on.
	 * @return the original position.
	 */
	public Position getOriginalPosition() {
		return originalPosition;
	}
	
	/**
	 * Gets the movement coordinator for this NPC.
	 * @return the movement coordinator.
	 */
	public NpcMovementCoordinator getMovementCoordinator() {
		return movementCoordinator;
	}
	
	/**
	 * Determines if this NPC was originally random walking.
	 * @return {@code true} if this NPC was originally walking, {@code false}
	 * otherwise.
	 */
	public boolean isOriginalRandomWalk() {
		return originalRandomWalk;
	}
	
	/**
	 * Sets the value for {@link Npc#originalRandomWalk}.
	 * @param originalRandomWalk the new value to set.
	 */
	public void setOriginalRandomWalk(boolean originalRandomWalk) {
		this.originalRandomWalk = originalRandomWalk;
	}
	
	/**
	 * Gets the spell that this NPC is weakened by.
	 * @return the weakening spell.
	 */
	public CombatWeaken getWeakenedBy() {
		return weakenedBy;
	}
	
	/**
	 * Sets the value for {@link Npc#weakenedBy}.
	 * @param weakenedBy the new value to set.
	 */
	public void setWeakenedBy(CombatWeaken weakenedBy) {
		this.weakenedBy = weakenedBy;
	}
	
	/**
	 * Determines if this NPC respawns.
	 * @return {@code true} if this NPC respawns, {@code false} otherwise.
	 */
	public boolean isRespawn() {
		return respawn;
	}
	
	/**
	 * Sets the value for {@link Npc#respawn}.
	 * @param respawn the new value to set.
	 */
	public void setRespawn(boolean respawn) {
		this.respawn = respawn;
	}
	
	/**
	 * @return the player's slot this npc was spawned for.
	 */
	public int getOwner() {
		return owner;
	}
	
	/**
	 * The flag which identifies if this npc was spawned for the player by the username.
	 * @param spawnedFor the player to check for.
	 * @return <true> if the npc was spawned for the player, <false> otherwise.
	 */
	public boolean isOwner(Player spawnedFor) {
		return this.owner != -1 && this.owner == spawnedFor.getSlot();
	}
	
	/**
	 * Sets the player's slot this npc was spawned for.
	 * @param player the player we're spawning this npc for.
	 */
	public void setOwner(Player player) {
		this.owner = player.getSlot();
		player.getMobs().add(this);
	}
	
	/**
	 * Determines if the npc is active in his region.
	 * @return {@code true} if the npc is active, {@code false} otherwise.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Sets the new value for {@link Npc#active}.
	 * @param active the new value to set.
	 */
	public void setActive(boolean active) {
		this.active = active;
		if(!active && World.getNpcMovementTask().getNpcs().contains(this)) {
			World.getNpcMovementTask().getNpcs().remove(this);
		} else if(active && movementCoordinator.isCoordinate() && !World.getNpcMovementTask().getNpcs().contains(this)) {
			World.getNpcMovementTask().getNpcs().offer(this);
		}
	}
	
	/**
	 * Determines if the npc is a smart npc.
	 * @return {@code true} if the npc is smart, {@code false} otherwise.
	 */
	public boolean isSmart() {
		return smart;
	}
	
	/**
	 * Sets the new value for {@link Npc#smart}.
	 * @param smart the new value to set.
	 */
	public void setSmart(boolean smart) {
		this.smart = smart;
	}
	
	/**
	 * Gets the definition for this NPC.
	 * @return the definition.
	 */
	public NpcDefinition getDefinition() {
		return NpcDefinition.DEFINITIONS[transform.orElse(id)];
	}
	
	/**
	 * Gets the transformation identifier.
	 * @return the transformation id.
	 */
	public OptionalInt getTransform() {
		return transform;
	}
	
	/**
	 * Gets the combat strategy.
	 * @return combat strategy.
	 */
	public Optional<CombatStrategy> getStrategy() {
		return strategy;
	}
	
	/**
	 * Sets a new value for {@link #strategy}.
	 * @param strategy the new value to set.
	 */
	public Npc setStrategy(Optional<CombatStrategy> strategy) {
		this.strategy = strategy;
		return this;
	}
	
	/**
	 * Determines if this npc is a familiar.
	 * @return <true> if the npc is a familiar, <false> otherwise.
	 */
	public boolean isFamiliar() {
		return false;
	}
	
	/**
	 * Determines if this npc is a pet.
	 * @return <true> if the npc is a pet, <false> otherwise.
	 */
	public boolean isPet() {
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Npc) {
			Npc other = (Npc) obj;
			return getId() == other.getId() && getSlot() == other.getSlot();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getId(), getSlot());
	}
	
	@Override
	public String toString() {
		return "NPC[slot= " + getSlot() + ", name=" + getDefinition().getName() + "]";
	}
	
}
