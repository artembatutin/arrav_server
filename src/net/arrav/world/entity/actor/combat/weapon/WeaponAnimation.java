package net.arrav.world.entity.actor.combat.weapon;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.arrav.world.entity.actor.combat.attack.FightStyle;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;

/**
 * The container class that represents one equipment animation that is used with weapons.
 */
public final class WeaponAnimation {
	
	/**
	 * The hash collection of weapon animations.
	 */
	public static final Int2ObjectArrayMap<WeaponAnimation> ANIMATIONS = new Int2ObjectArrayMap<>();
	
	/**
	 * The standing animation for this weapon.
	 */
	private final int stand;
	
	/**
	 * The walking animation for this weapon.
	 */
	private final int walk;
	
	/**
	 * The running animation for this weapon.
	 */
	private final int run;
	
	/**
	 * The combat blocking animation for this weapon.
	 */
	private final int block;
	
	/**
	 * The combat attacking animation for this weapon.
	 */
	private final int[] attack;
	
	/**
	 * Creates a new {@link WeaponAnimation}.
	 * @param standing the standing animation for this weapon.
	 * @param walking the walking animation for this weapon.
	 * @param running the running animation for this weapon.
	 * @param blocking the blocking animation for this weapon.
	 * @param attacking the attacking animation for this weapon.
	 */
	public WeaponAnimation(int standing, int walking, int running, int blocking, int[] attacking) {
		this.stand = standing;
		this.walk = walking;
		this.run = running;
		this.block = blocking;
		this.attack = attacking;
	}
	
	/**
	 * A substitute for {@link Object#clone()} that creates another 'copy' of
	 * this instance. The created copy is <i>safe</i> meaning it does not hold
	 * <b>any</b> references to the original instance.
	 * @return a reference-free copy of this instance.
	 */
	public WeaponAnimation copy() {
		return new WeaponAnimation(stand, walk, run, block, attack);
	}
	
	/**
	 * The method executed when weapon {@code item} is equipped that assigns a
	 * weapon animation to {@code player}.
	 * @param player the player equipping the item.
	 * @param item the item the player is equipping.
	 */
	public static void execute(Player player, Item item) {
		WeaponAnimation animation = item == null ? ANIMATIONS.get(0) : ANIMATIONS.get(item.getId());
		player.setWeaponAnimation(animation == null ? ANIMATIONS.get(0).copy() : animation.copy());
	}
	
	/**
	 * Gets the standing animation for this weapon.
	 * @return the standing animation.
	 */
	public int getStanding() {
		return stand;
	}
	
	/**
	 * Gets the walking animation for this weapon.
	 * @return the walking animation.
	 */
	public int getWalking() {
		return walk;
	}
	
	/**
	 * Gets the running animation for this weapon.
	 * @return the running animation.
	 */
	public int getRunning() {
		return run;
	}
	
	/**
	 * Gets the blocking animation for this weapon.
	 * @return the blocking animation.
	 */
	public int getBlocking() {
		return block;
	}
	
	/**
	 * Gets the attacking animation for this weapon dependent on the {@link FightStyle}.
	 * @return the attacking animation.
	 */
	public int[] getAttacking() {
		return attack;
	}
	
}