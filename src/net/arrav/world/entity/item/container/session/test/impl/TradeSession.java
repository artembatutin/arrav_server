package net.arrav.world.entity.item.container.session.test.impl;

import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.container.session.ExchangeSessionType;
import net.arrav.world.entity.item.container.session.test._ExchangeSession;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 13-2-2018.
 */
public final class TradeSession extends _ExchangeSession {
	
	public TradeSession(Player player, Player other) {
		super(player, other, ExchangeSessionType.TRADE);
	}
	
	/**
	 * Checks if the item can be added to the container.
	 * @param player the player who's attempting to add an item.
	 * @param item the item that was attempted to being added.
	 * @param slot the slot the item is being added from.
	 * @return <true> if the item can, <false> otherwise.
	 */
	@Override
	public boolean canAddItem(Player player, Item item, int slot) {
		return false;
	}
	
	/**
	 * Checks if the item can be removed from the container.
	 * @param item the item that was attempted to being removed.
	 * @return <true> if the item can, <false> otherwise.
	 */
	@Override
	public boolean canRemoveItem(Player player, Item item) {
		return false;
	}
	
	/**
	 * Any functionality that should be dealth with when a player sends a request
	 * should be handled in here.
	 * @param player the player who requested the exchange session.
	 * @param requested the player who was requested by the {@code player}.
	 */
	@Override
	public void onRequest(Player player, Player requested) {
	
	}
	
	/**
	 * Any functionality that should be dealth when a player clicks a button
	 * should be handled in here.
	 * @param player the player whom clicked the button.
	 * @param button the id that was clicked.
	 */
	@Override
	public void onClickButton(Player player, int button) {
	
	}
	
	/**
	 * Accepts the new {@code stage}.
	 * @param player the player who accepted the stage.
	 * @param stage the possible stages.
	 */
	@Override
	public void accept(Player player, int stage) {
		if(stage == -1) {
			forEach(p -> p.message("Yay!"));
		}
	}
	
	/**
	 * Updates the main components of the interface.
	 */
	@Override
	public void updateMainComponents() {
	
	}
	
	/**
	 * Updates the offer components of the interface.
	 */
	@Override
	public void updateOfferComponents() {
	
	}
	
	/**
	 * Any functionality that should be handled when the interface closes.
	 */
	@Override
	public void onReset() {
	
	}
	
	public enum _TradeStage {
		REQUEST, CONFIRMATION;
	}
}
