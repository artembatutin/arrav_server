package net.edge.content;

import net.edge.world.node.entity.player.Player;

/**
 * An enumeration of tab interfaces.
 * @author Artem Batutin<artembatutin@gmail.com>
 */
public enum TabInterface {
	ATTACK(0, 0),
	SKILL(1, 1),
	QUEST(2, 2),
	ACHIEVEMENT(3, 14),
	INVENTORY(4, 3),
	EQUIPMENT(5, 4),
	PRAYER(6, 5),
	MAGIC(7, 6),
	SUMMONING(8, 15),
	FRIEND(9, 8),
	IGNORE(10, 9),
	LOGOUT(16, 10),
	CLAN_CHAT(11, 7),
	SETTING(12, 11),
	EMOTE(13, 12),
	MUSIC(14, 13);
	
	/**
	 * The id of the new based game-frames.
	 */
	private final int newId;
	
	/**
	 * The id of the old based game-frames.
	 */
	private final int oldId;
	
	/**
	 * Constructs a new {@link TabInterface}.
	 * @param newId The identifier of the newer type game-frames.
	 * @param oldId The identifier of the older type game-frames.
	 */
	TabInterface(int newId, int oldId) {
		this.newId = newId;
		this.oldId = oldId;
	}
	
	/**
	 * Sends the interface to the selected tab.
	 * @param player The player used in the context.
	 * @param id     The id of the interface to send.
	 */
	public void sendInterface(Player player, int id) {
		if(newId != -1)
			player.getMessages().sendSidebarInterface(newId, id, 1);
		if(oldId != -1)
			player.getMessages().sendSidebarInterface(oldId, id, 0);
	}
	
	public int getNew() {
		return newId;
	}
}