package net.edge.content.skill.summoning.familiar.ability;

import com.google.common.collect.ImmutableList;
import net.edge.content.skill.summoning.familiar.FamiliarContainer;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the beast of burden ability.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BeastOfBurden extends FamiliarContainer {

	/**
	 * The unique items this familiar can hold.
	 * <p>this also includes this familiar will <b>not</b> hold any other
	 * item</p>
	 */
	private final Optional<ImmutableList<Item[]>> uniqueItems;

	/**
	 * Constructs a new {@link BeastOfBurden}.
	 *
	 * @param uniqueItems {@link #uniqueItems}.
	 * @param size        the amount this familiar can carry.
	 */
	public BeastOfBurden(Optional<ImmutableList<Item[]>> uniqueItems, int size) {
		super(FamiliarAbilityType.BEAST_OF_BURDEN, size);

		this.uniqueItems = uniqueItems;
	}

	/**
	 * Constructs a new {@link BeastOfBurden}.
	 *
	 * @param size the amount this familiar can carry.
	 */
	public BeastOfBurden(int size) {
		this(Optional.empty(), size);
	}

	@Override
	public boolean canStore(Player player, Item item) {
		if(!uniqueItems.isPresent()) {
			return true;
		}

		ImmutableList<Item[]> collection = uniqueItems.get();

		if(collection.contains(item)) {
			return true;
		}
		return false;
	}

	@Override
	public void onStore(Player player) {
		// no functionality required.
	}

	@Override
	public boolean canWithdraw(Player player, Item item) {
		return true;
	}

	@Override
	public void onWithdraw(Player player) {
		// no extra functionality needed for withdrawing.
	}

	@Override
	public void initialise(Player player) {
		// this ability is not initialised when the familiar is summoned.
	}

}
