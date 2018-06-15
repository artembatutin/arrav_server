package net.arrav.content;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;

/**
 * The container class that represents one equipment animation that is used with
 * weapons.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @author lare96 <http://github.com/lare96>
 */
public final class ShieldAnimation {
	
	/**
	 * The hash collection of weapon animations.
	 */
	public static final Int2ObjectOpenHashMap<ShieldAnimation> ANIMATIONS = new Int2ObjectOpenHashMap<>();
	
	/**
	 * The combat blocking animation for this weapon.
	 */
	private final int block;
	
	/**
	 * Constructs a new {@link ShieldAnimation}.
	 * @param block {@link #block}.
	 */
	public ShieldAnimation(int block) {
		this.block = block;
	}
	
	/**
	 * A substitute for {@link Object#clone()} that creates another 'copy' of
	 * this instance. The created copy is <i>safe</i> meaning it does not hold
	 * <b>any</b> references to the original instance.
	 * @return a reference-free copy of this instance.
	 */
	public ShieldAnimation copy() {
		return new ShieldAnimation(block);
	}
	
	/**
	 * The method executed when shield {@code item} is equipped that assigns a
	 * shield animation to {@code player}.
	 * @param player the player equipping the item.
	 * @param item the item the player is equipping.
	 */
	public static void execute(Player player, Item item) {
		if(item == null)
			return;
		ShieldAnimation animation = ANIMATIONS.get(item.getId());
		player.setShieldAnimation(animation == null ? null : animation.copy());
	}
	
	/**
	 * @return the block
	 */
	public int getBlock() {
		return block;
	}
}
