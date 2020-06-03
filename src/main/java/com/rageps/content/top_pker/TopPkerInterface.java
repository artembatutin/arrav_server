package com.rageps.content.top_pker;


import com.rageps.util.DateTimeUtil;
import com.rageps.util.Duration;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;

import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Locale;

/**
 * Created by Jason M on 2017-03-16 at 1:38 AM
 */
public class TopPkerInterface {

	/**
	 * The single instance of this class.
	 */
	private static final TopPkerInterface SINGLETON = new TopPkerInterface();

	private static final int ENTRY_COMPONENTS = 4;

	private TopPkerInterface() {

	}

	/**
	 * Visually displays the scoreboard for a player and will attempt to start an event
	 * that will continuously refresh the interface unless the current player interface has changed.
	 *
	 * @param player
	 * 			  the player having the scoreboard shown to them.
	 */
	public void showScoreboard(Player player) {
		//player.getPacketSender().sendInterface(50000);

		if (player.getAttributeMap().getObject(AttributeKeys.INTERFACE_EVENT) == null) {
			player.getAttributeMap().set(AttributeKeys.INTERFACE_EVENT, new InterfaceUpdateTask(player));
			World.get().getTask().submit(player.getAttributeMap().getObject(AttributeKeys.INTERFACE_EVENT));
		}
	}

	/**
	 * Visually updates the entire scoreboard for a player.
	 *
	 * @param player
	 * 			  the player having the scoreboard updated.
	 */
	public void updateScoreboard(Player player) {
		TopPker dailyTopPker = TopPker.getSingleton();

		TopPkerSession session = dailyTopPker.getSession().orElse(null);
/*
		PacketSender sender = player.getPacketSender();

		if (session == null) {
			sender.sendString("<img=20></img> Top Pker " + ChatColor.RED +"Offline</col>", 50006);
			sender.sendString("---", 50012);
			sender.sendString("", 50013);
			sender.sendString("---", 50022);
			sender.sendString("---", 50017);
		} else {
			sender.sendString(String.format("<img=20></img>Top Pker #%s", session.getId()), 50006);
			sender.sendString(String.format("Current Event #%s", session.getId()), 50012);
			sender.sendString("", 50013);

			Duration duration = new Duration(ZonedDateTime.now(DateTimeUtil.ZONE), session.getEndDate());

			sender.sendString(String.format("Ends in: %s days, %s hours and %s mins.\\nRewards:", duration.getDays(), duration.getHours(), duration.getMinutes()), 50017);
			sender.sendItemContainer(PredefinedReward.ALL.stream().filter(r ->
					r.appendable(player, session.getEndDate())).map(PredefinedReward::getItem).toArray(Item[]::new), 50020);
		}
		TopPkerSession lastSession = dailyTopPker.getLastSession().orElse(null);

		if (lastSession == null) {
			sender.sendString("---", 50009);
			sender.sendString("---", 50010);
		} else {
			ZonedDateTime date = lastSession.getEndDate();
			String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
			String day = Integer.toString(date.getDayOfMonth());
			String year = Integer.toString(date.getYear());
			String formattedDate = month + " " + day + ", " + year;

			Winner winner = lastSession.getWinner().orElse(TopPkerSession.DEFAULT_WINNER);
			sender.sendString(String.format("Last Event #%s", lastSession.getId()), 50009);
			sender.sendString(String.format("Ended on: %s\\nWinner: %s\\nKills: %d\\nDeaths: %d",
					formattedDate, StringUtil.prettifyUsername(winner.getUsernameAsLong()),
					winner.getKills(), winner.getDeaths()), 50010);
		}
		updateScoreboardEntries(player);*/
	}

	/**
	 * Visually updates the scoreboard entries for a singular player.
	 *
	 * @param player
	 * 			  the player having the scoreboard entries updated.
	 */
	private void updateScoreboardEntries(Player player) {
		/*TopPker dailyTopPker = TopPker.getSingleton();

		TopPkerSession session = dailyTopPker.getSession().orElse(null);

		if (session == null) {
			for (int index = 0; index <= 25; index++) {
				player.getPacketSender().sendString("---", 50026 + (index * ENTRY_COMPONENTS) + 1);
				player.getPacketSender().sendString("-", 50026 + (index * ENTRY_COMPONENTS) + 2);
				player.getPacketSender().sendString("-", 50026 + (index * ENTRY_COMPONENTS) + 3);
			}
			return;
		}
		ListIterator<Entry> entriesIterator = new ArrayList<>(session.bestEntries(25)).listIterator();

		for (int index = 0; index <= 25; index++) {
			int interfaceChildId = 50026 + (index * ENTRY_COMPONENTS);

			Entry entry = entriesIterator.hasNext() ? entriesIterator.next() : null;

			if (entry == null || entry.getKills() == 0) {
				player.getPacketSender().sendString("---", interfaceChildId + 1);
				player.getPacketSender().sendString("-", interfaceChildId + 2);
				player.getPacketSender().sendString("-", interfaceChildId + 3);
			} else {
				player.getPacketSender().sendString(String.format("%s.) %s", index + 1, StringUtil.prettifyUsername(entry.getUsernameAsLong())), interfaceChildId + 1);
				player.getPacketSender().sendString(Integer.toString(entry.getKills()), interfaceChildId + 2);
				player.getPacketSender().sendString(Integer.toString(entry.getDeaths()), interfaceChildId + 3);
			}
		}*/
	}

	public static TopPkerInterface getSingleton() {
		return SINGLETON;
	}

}
