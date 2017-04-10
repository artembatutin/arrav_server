package net.edge.world.content.container.impl.bank;

import net.edge.world.content.container.ItemContainerAdapter;
import net.edge.world.model.node.entity.player.Player;

/**
 * An {@link ItemContainerAdapter} implementation that listens for changes to the bank.
 */
public class BankListener extends ItemContainerAdapter {
	
	/**
	 * The slot of this bank tab listener.
	 */
	private final int slot;
	
	/**
	 * Creates a new {@link BankListener}.
	 */
	public BankListener(Player player, int slot) {
		super(player);
		this.slot = slot;
	}
	
	@Override
	public int getWidgetId() {
		return 270 + slot;
	}
	
	@Override
	public String getCapacityExceededMsg() {
		return "You do not have enough bank space to deposit that.";
	}
}
