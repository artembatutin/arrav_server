package net.edge.world.entity.item;

import net.edge.util.rand.RandomUtils;

/**
 * Defines a set of items.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ItemSet {

	/**
	 * The set of items.
	 */
	private final Item[] set;

	/**
	 * Constructs a new {@link ItemSet}.
	 *
	 * @param set {@link #set}.
	 */
	public ItemSet(Item... set) {
		this.set = set;
	}

	/**
	 * Constructs a new {@link ItemSet} with an int array.
	 *
	 * @param set {@link #set}.
	 */
	public ItemSet(int... set) {
		this(Item.convert(set));
	}

	/**
	 * Gets a random item from the item set.
	 *
	 * @return an item from the item set.
	 */
	public Item getRandom() {
		return set[RandomUtils.inclusive(set.length - 1)];
	}

	/**
	 * @return {@link #set}
	 */
	public Item[] getSet() {
		return set;
	}
}
