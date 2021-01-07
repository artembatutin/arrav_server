package com.rageps.content.wilderness;

import com.rageps.content.PlayerPanel;
import com.rageps.content.achievements.Achievement;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

/**
 * Handles some parts of wilderness activity.
 * @author Artem Batutin
 */
public final class WildernessActivity {
	
	/**
	 * A list of all active {@link Player} in wilderness.
	 */
	private static final ObjectList<Player> PLAYERS = new ObjectArrayList<>();
	
	/**
	 * Gets the list of all active players in wilderness.
	 * @return active players in wilderness.
	 */
	public static ObjectList<Player> getPlayers() {
		return PLAYERS;
	}
	
	/**
	 * A player entering wilderness.
	 * @param player entering player.
	 */
	public static void enter(Player player) {
		PLAYERS.add(player);
		Achievement.WILD_ONE.inc(player);
		PlayerPanel.PLAYERS_IN_WILD.refreshAll("@or2@ - Players in wild: @yel@" + PLAYERS.size());
	}
	
	/**
	 * A player leaving wilderness
	 * @param player player leaving.
	 */
	public static void leave(Player player) {
		PLAYERS.remove(player);
		PlayerPanel.PLAYERS_IN_WILD.refreshAll("@or2@ - Players in wild: @yel@" + PLAYERS.size());
	}
	
	public static int getFooledCount(Player player) {
		if(player.getRights().isDonator())
			return 0;
		return RandomUtils.inclusive(4) == 1 ? RandomUtils.inclusive(10) : 0;
	}
	
	public static int getX(Position pos) {
		return (int) ((pos.getX() - 2950) * 0.6704545454545 / 2);
	}
	
	public static int getY(Position pos) {
		return (int) ((pos.getY() - 3525) * 0.6704545454545 / 2);
	}
	
	public static int fooledX() {
		return (int) (RandomUtils.inclusive(440) * 0.6704545454545 / 2);
	}
	
	public static int fooledY() {
		return (int) (RandomUtils.inclusive(440) * 0.6704545454545 / 2);
	}
	
}