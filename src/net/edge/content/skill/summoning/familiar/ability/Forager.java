package net.edge.content.skill.summoning.familiar.ability;

import net.edge.content.skill.summoning.familiar.FamiliarContainer;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

/**
 * Holds functionality for the forager ability.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class Forager extends FamiliarContainer {

	/**
	 * The collectable items this container can hold.
	 */
	private final Item[] collectables;

	/**
	 * The last item this forager collected.
	 */
	private Item produced;

	/**
	 * Constructs a new {@link Forager}.
	 *
	 * @param collectables the items this container can hold.
	 */
	public Forager(Item... collectables) {
		super(FamiliarAbilityType.FORAGER, 30);

		this.collectables = collectables;
	}

	/**
	 * Constructs a new {@link Forager}.
	 *
	 * @param collectables the items this container can hold.
	 */
	public Forager(int... collectables) {
		this(Item.convert(collectables));
	}

	/**
	 * Checks if the {@code player}'s familiar can forage.
	 *
	 * @param player the player to check this for.
	 * @return <true> if the familiar can forage, <false> otherwise.
	 */
	public abstract boolean canForage(Player player);

	/**
	 * Any extra functionality that should be handled when the
	 * item is added to the container.
	 *
	 * @param player the player we're executing this functionality for.
	 * @param item   the item that was added to the container.
	 */
	public void onStore(Player player, final Item item) {
		//should be overriden.
	}

	@Override
	public final boolean canStore(Player player, Item item) {
		player.message("You cannot store items in this familiar.");
		return false;
	}

	@Override
	public final void onStore(Player player) {
		//shouldn't be overriden.
	}

	/**
	 * @return the collectables
	 */
	public Item[] getCollectables() {
		return collectables;
	}

	/**
	 * @return the produced
	 */
	public Item getProduced() {
		return produced;
	}

	/**
	 * @param produced the produced to set
	 */
	public void setProduced(Item produced) {
		this.produced = produced;
	}
}
