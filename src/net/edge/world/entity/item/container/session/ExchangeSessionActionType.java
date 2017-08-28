package net.edge.world.entity.item.container.session;

/**
 * The enumerated type whose elements represent the exchange action types.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum ExchangeSessionActionType {
	/**
	 * The action for when both players get their items restored.
	 */
	RESTORE_ITEMS,

	/**
	 * The action for clearing the container session.
	 */
	DISPOSE_ITEMS,

	/**
	 * The action for halting for what happens to the items inside the containers.
	 */
	HALT;
}
