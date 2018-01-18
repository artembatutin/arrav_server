package net.edge.world.entity.actor.update;

import io.netty.buffer.ByteBuf;
import net.edge.world.entity.actor.player.Player;

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
	public int write(Player player, Player other, ByteBuf buf) {
		buf.putCString(other.getForcedText());
		return -1;
	}
}
