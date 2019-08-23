package net.arrav.util.log.impl;

import net.arrav.util.log.LogDetails;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.container.ItemContainer;

import java.util.Optional;

/**
 * The class which represents a duel log.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class DuelLog extends LogDetails {
	
	/**
	 * The username of the rival in this duelling fight for this log.
	 */
	private final String otherUsername;
	
	/**
	 * Determines if this player won the duelling session for this log.
	 */
	private final boolean wonDuel;
	
	/**
	 * The items that were risked in this duelling session for this log.
	 */
	private final ItemContainer risked;
	
	/**
	 * The items won or lost from this duelling fight for this log.
	 */
	private final ItemContainer items;
	
	/**
	 * Constructs a new {@link DuelLog}.
	 * @param player {@link #getUsername()}.
	 * @param other {@link #otherUsername}.
	 * @param wonDuel {@link #wonDuel}.
	 * @param items {@link #items}.
	 * @param risked {@link #risked}.
	 */
	public DuelLog(Player player, Player other, boolean wonDuel, ItemContainer items, ItemContainer risked) {
		super(player.getFormatUsername(), "Duelling");
		this.otherUsername = other.getFormatUsername();
		this.wonDuel = wonDuel;
		this.items = items;
		this.risked = risked;
	}
	
	@Override
	public Optional<String> formatInformation() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("[\n");
		
		for(Item item : items.getItems()) {
			if(item == null || (item != null && builder.toString().contains(item.getDefinition().getName()))) {
				continue;
			}
			
			builder.append("    " + (wonDuel ? "Won" : "Lost") + ": [Amount: " + items.computeAmountForId(item.getId()) + ", Name: " + item.getDefinition().getName() + ", Id = " + item.getId() + "].\n");
		}
		
		if(!risked.isEmpty()) {
			for(Item item : risked.getItems()) {
				if(item == null || (item != null && builder.toString().contains(item.getDefinition().getName()))) {
					continue;
				}
				
				builder.append("    " + (wonDuel ? "Risked" : "Could of won") + ": [Amount: " + risked.computeAmountForId(item.getId()) + ", Name: " + item.getDefinition().getName() + ", Id = " + item.getId() + "].\n");
			}
		} else {
			builder.append("    " + (wonDuel ? "Risked" : "Could of won") + ": Nothing.\n");
		}
		builder.append("    Duelled with: " + otherUsername + " and " + (wonDuel ? "won" : "lost") + " the duel.\n");
		return Optional.of(builder.toString());
	}
	
}
