package net.edge.world.entity.actor.mob.drop;

import net.edge.util.rand.Chance;
import net.edge.util.rand.RandomUtils;
import net.edge.world.entity.item.Item;

import java.util.concurrent.ThreadLocalRandom;

import static net.edge.util.rand.Chance.*;

/**
 * A model representing an item within a rational item table that can be dropped.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class Drop {
	
	/**
	 * The identification of this {@code Drop}.
	 */
	private int id;
	
	/**
	 * The minimum amount that will be dropped.
	 */
	private int minimum;
	
	/**
	 * The maximum amount that will be dropped.
	 */
	private int maximum;
	
	/**
	 * The chance of this item being dropped.
	 */
	private Chance chance;
	
	/**
	 * Creates a new {@link Drop}.
	 * @param id      the identification of this {@code Drop}.
	 * @param minimum the minimum amount that will be dropped.
	 * @param maximum the maximum amount that will be dropped.
	 * @param chance  the chance of this item being dropped.
	 */
	public Drop(int id, int minimum, int maximum, Chance chance) {
		this.id = id;
		this.minimum = minimum;
		this.maximum = maximum;
		this.chance = chance;
	}
	
	@Override
	public String toString() {
		return "ITEM[id= " + getId() + ", min= " + getMinimum() + ", max= " + getMaximum() + ", chance= " + getChance() + "]";
	}
	
	/**
	 * Converts this {@code Drop} into an {@link Item} Object.
	 * @return the converted drop.
	 */
	public Item toItem() {
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
	public Chance getChance() {
		return chance;
	}
	
	/**
	 * Returns the condition if this item is rare.
	 * @return true if chance is a rare chance.
	 */
	public boolean isRare() {
		return chance == RARE || chance == VERY_RARE || chance == EXTREMELY_RARE;
	}
	
	/**
	 * Tries to roll this item.
	 * @param rand random gen.
	 * @return condition if successful.
	 */
	public boolean roll(ThreadLocalRandom rand) {
		return chance.getRational().doubleValue() >= rand.nextDouble();
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
		result = 31 * result + getChance().hashCode();
		return result;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}
	
	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}
	
	public void setChance(Chance chance) {
		this.chance = chance;
	}
}