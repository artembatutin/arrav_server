package com.rageps.world.entity.actor.mob.drop;

import com.rageps.content.market.MarketItem;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.mob.drop.chance.NpcDropChance;
import com.rageps.world.entity.item.Item;


/**
 * A model representing an item within a rational item table that can be dropped.
 * @author Artem Batutin
 * @author Tamatea <tamateea@gmail.com>
 */
public class Drop {
	
	/**
	 * The identification of this {@code Drop}.
	 */
	public int id;
	
	/**
	 * The minimum amount that will be dropped.
	 */
	public int minimum;
	
	/**
	 * The maximum amount that will be dropped.
	 */
	public int maximum;
	
	/**
	 * The chance of this item being dropped.
	 */
	private final int chance;

	/**
	 * If the item is a rare drop.
	 */
	private final boolean rare;

	/**
	 * Whether or not a beam gfx should be displayed with the drop.
	 */
	private final boolean beam;

	/**
	 * A {@link NpcDropChance} for testing the drop.
	 */
	private NpcDropChance dropChance;
	
	/**
	 * Creates a new {@link Drop}.
	 * @param id the identification of this {@code Drop}.
	 * @param minimum the minimum amount that will be dropped.
	 * @param maximum the maximum amount that will be dropped.
	 * @param chance the chance of this item being dropped.
	 */
	public Drop(int id, int minimum, int maximum, int chance, boolean rare, boolean beam) {
		this.id = id;
		this.minimum = minimum;
		this.maximum = maximum;
		this.chance = chance;
		this.rare = rare;
		this.beam = beam;
		this.dropChance = new NpcDropChance(chance);
	}
	
	@Override
	public String toString() {
		return "ITEM[id= " + getId() + ", min= " + getMinimum() + ", max= " + getMaximum() + ", chance= " + getChance() + "]";
	}
	
	/**
	 * Converts this {@code Drop} into an {@link Item} Object.
	 * @return the converted drop.
	 */
	Item toItem() {
		return new Item(getId(), RandomUtils.inclusive(getMinimum(), getMaximum()));
	}
	
	/**
	 * Gets the identification of this {@code Drop}.
	 * @return the identification.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the minimum amount that will be dropped.
	 * @return the minimum amount.
	 */
	public int getMinimum() {
		return minimum;
	}
	
	/**
	 * Gets the maximum amount that will be dropped.
	 * @return the maximum amount.
	 */
	public int getMaximum() {
		return maximum;
	}
	
	/**
	 * Gets the chance of this item being dropped.
	 * @return the drop chance.
	 */
	public int getChance() {
		return chance;
	}

	/**
	 * Gets the Npc drop chance associated with the drop.
	 * @return The {@link NpcDropChance}.
	 */
	public NpcDropChance getDropChance() {
		return dropChance;
	}

	/**
	 * Gets the pricing value of this drop.
	 * @return value.
	 */
	public int value() {
		return MarketItem.get(id).getPrice() * maximum;
	}

	/**
	 * Get's the drops rare property.
	 * @return the rare property.
	 */
	public boolean isRare() {
		return rare;
	}

	/**
	 * Gets the beam property.
	 * @return the beam.
	 */
	public boolean isBeam() {
		return beam;
	}

	/**
	 * @return If the drop should always be dropped.
	 */
	boolean isAlways() {
		return chance <= 1;
	}

	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof Drop)) {
			return false;
		}
		Drop drop = (Drop) o;
		return drop.getId() == getId() && drop.getMinimum() == getMinimum() && drop.getMaximum() == getMaximum() && drop.getChance() == getChance();
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + getId();
		result = 31 * result + getMinimum();
		result = 31 * result + getMaximum();
		result = 31 * result + getChance();
		return result;
	}
	
}