package net.edge.world.node.actor.update;

import net.edge.net.codec.GameBuffer;
import net.edge.world.node.actor.mob.Mob;
import net.edge.world.node.actor.player.Player;

/**
 * An {@link NpcUpdateBlock} implementation that handles the {@code FORCE_CHAT} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
final class NpcForceChatUpdateBlock extends NpcUpdateBlock {
	
	/**
	 * Creates a new {@link NpcForceChatUpdateBlock}.
	 */
	NpcForceChatUpdateBlock() {
		super(4, UpdateFlag.FORCE_CHAT);
	}
	
	@Override
	public int write(Player player, Mob mob, GameBuffer msg) {
		msg.putCString(mob.getForcedText());
		return -1;
	}
}