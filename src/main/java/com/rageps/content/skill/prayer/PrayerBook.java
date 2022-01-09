package com.rageps.content.skill.prayer;

import com.rageps.content.TabInterface;
import com.rageps.world.entity.actor.player.Player;

/**
 * The enumerated type whose elements represent a prayer type.
 * @author Artem Batutin
 */
public enum PrayerBook {
	NORMAL(5608), CURSES(21356);
	
	/**
	 * The identifier for this prayer interface.
	 */
	private final int id;
	
	/**
	 * Creates a new {@link PrayerBook}.
	 * @param id the identifier for this prayer interface.
	 */
	PrayerBook(int id) {
		this.id = id;
	}
	
	@Override
	public final String toString() {
		return name().toLowerCase().replaceAll("_", " ");
	}
	
	/**
	 * Gets the identifier for this prayer interface.
	 * @return the identifier for the interface.
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * Attempts to convert the prayerbook for {@code player} to {@code book}.
	 * @param player the player to convert the spellbook for.
	 * @param book the type of prayer to convert to.
	 */
	public static void convert(Player player, PrayerBook book) {
		if(player.getPrayerBook() == book) {
			player.message("You have already converted to " + book.toString().toLowerCase() + " magics!");
			return;
		}
		player.getPrayerActive().forEach(p -> p.deactivate(player));
		TabInterface.PRAYER.sendInterface(player, book.id);
		player.setPrayerBook(book);
	}
}
