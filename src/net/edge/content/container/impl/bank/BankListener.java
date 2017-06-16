package net.edge.content.container.impl.bank;

import net.edge.content.container.ItemContainerAdapter;
import net.edge.world.node.entity.player.Player;

/**
 * An {@link ItemContainerAdapter} implementation that listens for changes to the bank.
 */
public class BankListener extends ItemContainerAdapter {
	
	/**
	 * Creates a new {@link BankListener}.
	 */
	public BankListener(Player player) {
		super(player);
	}
	
	@Override
	public String getCapacityExceededMsg() {
		return "You do not have enough bank space to deposit that.";
	}
}
