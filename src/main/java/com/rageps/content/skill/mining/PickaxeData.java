package com.rageps.content.skill.mining;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rageps.content.skill.woodcutting.Hatchet;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

import java.util.EnumSet;
import java.util.Optional;

/**
 * The enumerated type which elements represents a Pickaxe which can mine
 * a {@link RockData}.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum PickaxeData {
	BRONZE(1265, 480, 468, 625, 1, 0.05),
	IRON(1267, 482, 470, 626, 1, 0.08),
	STEEL(1269, 484, 472, 627, 6, 0.12),
	MITHRIL(1273, 486, 474, 629, 21, 0.16),
	ADAMANT(1271, 488, 476, 628, 31, 0.2),
	RUNE(1275, 490, 478, 624, 41, 0.25),
	INFERNO_ADZE(13661, 10222, 41, 0.55),
	DRAGON(15259, 12188, 61, 0.6),
	DRAGONG(20786, 12188, 61, 0.6);
	
	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<PickaxeData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(PickaxeData.class));
	
	/**
	 * Represents the identifier for the broken pickaxe handle.
	 */
	public static final Item PICKAXE_HANDLE = new Item(466);
	
	/**
	 * The identifier for this pickaxe.
	 */
	private final Item item;
	
	/**
	 * The identifier for the pickaxe head.
	 */
	private final Item head;
	
	/**
	 * The identifier for the broken pickaxe.
	 */
	private final Item broken;
	
	/**
	 * The animation for this pickaxe.
	 */
	private final Animation animation;
	
	/**
	 * The requirement for this pickaxe.
	 */
	private final int requirement;
	
	/**
	 * The delay for this pickaxe.
	 */
	private final double speed;
	
	/**
	 * Constructs a new {@link PickaxeData} enumerator.
	 * @param item {@link #item}.
	 * @param head {@link #head}.
	 * @param broken {@link #broken}.
	 * @param animation {@link #animation}.
	 * @param requirement {@link #requirement}.
	 * @param speed {@link #speed}.
	 */
	private PickaxeData(int item, int head, int broken, int animation, int requirement, double speed) {
		this.item = new Item(item);
		this.head = new Item(head);
		this.broken = new Item(broken);
		this.animation = new Animation(animation);
		this.requirement = requirement;
		this.speed = speed;
	}
	
	/**
	 * Constructs a new {@link PickaxeData} enumerator.
	 * @param item {@link #item}.
	 * @param animation {@link #animation}.
	 * @param requirement {@link #requirement}.
	 * @param speed {@link #speed}.
	 */
	private PickaxeData(int item, int animation, int requirement, double speed) {
		this.item = new Item(item);
		this.head = null;
		this.broken = null;
		this.animation = new Animation(animation);
		this.requirement = requirement;
		this.speed = speed;
	}
	
	/**
	 * @return {@link #item}.
	 */
	public Item getItem() {
		return item;
	}
	
	/**
	 * @return {@link #head}.
	 */
	public Item getHead() {
		return head;
	}
	
	/**
	 * @return {@link #broken}.
	 */
	public Item getBrokenPickaxe() {
		return broken;
	}
	
	/**
	 * @return {@link #animation}.
	 */
	public Animation getAnimation() {
		return animation;
	}
	
	/**
	 * @return {@link #requirement}.
	 */
	public int getRequirement() {
		return requirement;
	}
	
	/**
	 * @return {@link #speed}.
	 */
	public double getSpeed() {
		return speed;
	}
	
	/**
	 * Gets the definition for this hatchet.
	 * @param identifier the identifier to check for.
	 * @return an optional holding the {@link Hatchet} value found,
	 * {@link Optional#empty} otherwise.
	 */
	public static Optional<PickaxeData> getDefinition(Player player) {
		return VALUES.stream().filter(def -> player.getEquipment().contains(def.item) || player.getInventory().contains(def.item)).findAny();
	}
}
