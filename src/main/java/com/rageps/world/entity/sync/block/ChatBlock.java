package com.rageps.world.entity.sync.block;


import com.rageps.net.sql.forum.account.MemberGroup;
import com.rageps.world.entity.actor.player.Player;

/**
 * The chat {@link SynchronizationBlock}. Only players can utilise this block.
 *
 * @author Graham
 */
public final class ChatBlock extends SynchronizationBlock {

	private final Player player;

	/**
	 * Creates the chat block.
	 */
	ChatBlock(Player player) {
		this.player = player;
	}

	/**
	 * Gets the compressed message.
	 *
	 * @return The compressed message.
	 */
	public byte[] getCompressedMessage() {
		return player.getChatText();
	}

	/**
	 * Gets the {@link MemberGroup} of the player who said the message.
	 *
	 * @return The privilege level.
	 */
	public MemberGroup getPrivilegeLevel() {
		return player.getMemberGroup();
	}

	/**
	 * Gets the text color.
	 *
	 * @return The text color.
	 */
	public int getTextColor() {
		return player.getChatColor();
	}

	/**
	 * Gets the text effects.
	 *
	 * @return The text effects.
	 */
	public int getTextEffects() {
		return player.getChatEffects();
	}

}