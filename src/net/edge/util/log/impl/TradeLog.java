package net.edge.util.log.impl;

import net.edge.util.log.LogDetails;
import net.edge.world.entity.item.container.ItemContainer;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * The class which represents a trade log.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class TradeLog extends LogDetails {
	
	/**
	 * The items that the user sold.
	 */
	private final ItemContainer sold;
	
	/**
	 * The name of the other player in the session.
	 */
	private final String otherUsername;
	
	/**
	 * The items that the user bought.
	 */
	private final ItemContainer bought;
	
	/**
	 * Constructs a new {@link TradeLog}.
	 * @param player {@link #getUsername()}.
	 * @param sold   {@link #sold}.
	 * @param other  {@link #otherUsername}.
	 * @param bought {@link #bought}.
	 */
	public TradeLog(Player player, ItemContainer sold, Player other, ItemContainer bought) {
		super(player.getFormatUsername(), "Trading");
		this.sold = sold;
		this.otherUsername = other.getFormatUsername();
		this.bought = bought;
	}
	
	@Override
	public Optional<String> formatInformation() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("[\n");
		
		if(sold.isEmpty()) {
			builder.append("    Sold: Nothing.\n");
		} else {
			for(Item item : sold.getItems()) {
				if(item == null || (item != null && builder.toString().contains(item.getDefinition().getName()))) {
					continue;
				}
				
				builder.append("    Sold: [Amount: " + sold.computeAmountForId(item.getId()) + ", Name: " + item.getDefinition().getName() + ", Id = " + item.getId() + "]\n");
			}
		}
		
		if(bought.isEmpty()) {
			builder.append("    Bought: Nothing\n");
		} else {
			for(Item item : bought.getItems()) {
				if(item == null || (item != null && builder.toString().contains(item.getDefinition().getName()))) {
					continue;
				}
				
				builder.append("    Bought: [Amount: " + bought.computeAmountForId(item.getId()) + ", Name: " + item.getDefinition().getName() + ", Id = " + item.getId() + "].\n");
			}
		}
		
		builder.append("    Traded with: " + otherUsername + ".\n");
		return Optional.of(builder.toString());
	}
}
