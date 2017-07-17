package net.edge.content.skill.summoning.familiar.impl.forager;

import net.edge.content.skill.summoning.familiar.ability.Forager;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.function.Consumer;

/**
 * Holds functionality for the passve forager ability.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class ForagerRightClickAbility extends Forager {

	/**
	 * The action that occurs when this ability is activated.
	 */
	private final Consumer<Player> action;

	/**
	 * Constructs a new {@link ForagerPassiveAbility}.
	 * @param action       {@link #action}.
	 * @param collectables the collectables this forager can hold.
	 */
	public ForagerRightClickAbility(Consumer<Player> action, int... collectables) {
		super(collectables);

		this.action = action;
	}

	@Override
	public boolean canForage(Player player) {
		return true;
	}

	@Override
	public boolean canWithdraw(Player player, Item item) {
		// can be overriden
		return true;
	}

	@Override
	public void onWithdraw(Player player) {
		//can be overriden
	}

	public void activate(Player player) {
		if(!canForage(player)) {
			return;
		}

		action.accept(player);
	}

	@Override
	public void initialise(Player player) {
		//no functionality required here.
	}
}
