package net.arrav.world.entity.actor.update;

import net.arrav.net.codec.game.GamePacket;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

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