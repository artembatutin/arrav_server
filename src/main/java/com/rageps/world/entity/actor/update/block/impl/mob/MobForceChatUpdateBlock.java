package com.rageps.world.entity.actor.update.block.impl.mob;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.block.MobUpdateBlock;
import com.rageps.world.entity.actor.update.UpdateFlag;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code FORCE_CHAT} update block.
 * @author Artem Batutin
 */
final class MobForceChatUpdateBlock extends MobUpdateBlock {
	
	/**
	 * Creates a new {@link MobForceChatUpdateBlock}.
	 */
	MobForceChatUpdateBlock() {
		super(4, UpdateFlag.FORCE_CHAT);
	}
	
	@Override
	public int write(Player player, Mob mob, GamePacket buf) {
		buf.putCString(mob.getForcedText());
		return -1;
	}
}