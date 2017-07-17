package net.edge.world.node.actor.update;

import net.edge.net.codec.GameBuffer;
import net.edge.world.node.actor.player.Player;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the {@code FORCE_CHAT} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class PlayerForceChatUpdateBlock extends PlayerUpdateBlock {
	
	/**
	 * Creates a new {@link PlayerForceChatUpdateBlock}.
	 */
	public PlayerForceChatUpdateBlock() {
		super(4, UpdateFlag.FORCE_CHAT);
	}
	
	@Override
	public int write(Player player, Player other, GameBuffer msg) {
		msg.putCString(other.getForcedText());
		return -1;
	}
}
