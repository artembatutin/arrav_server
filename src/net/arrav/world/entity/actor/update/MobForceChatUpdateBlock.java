package net.arrav.world.entity.actor.update;

import io.netty.buffer.ByteBuf;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code FORCE_CHAT} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
final class MobForceChatUpdateBlock extends MobUpdateBlock {
	
	/**
	 * Creates a new {@link MobForceChatUpdateBlock}.
	 */
	MobForceChatUpdateBlock() {
		super(4, UpdateFlag.FORCE_CHAT);
	}
	
	@Override
	public int write(Player player, Mob mob, ByteBuf buf) {
		buf.putCString(mob.getForcedText());
		return -1;
	}
}