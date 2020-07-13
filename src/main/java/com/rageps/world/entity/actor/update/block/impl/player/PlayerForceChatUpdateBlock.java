package com.rageps.world.entity.actor.update.block.impl.player;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.block.PlayerUpdateBlock;
import com.rageps.world.entity.actor.update.UpdateFlag;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the {@code FORCE_CHAT} update block.
 * @author Artem Batutin
 */
public final class PlayerForceChatUpdateBlock extends PlayerUpdateBlock {
	
	/**
	 * Creates a new {@link PlayerForceChatUpdateBlock}.
	 */
	public PlayerForceChatUpdateBlock() {
		super(4, UpdateFlag.FORCE_CHAT);
	}
	
	@Override
	public int write(Player player, Player other, GamePacket buf) {
		buf.putCString(other.getForcedText());
		return -1;
	}
}
