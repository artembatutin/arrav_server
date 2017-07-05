package net.edge.world.node.entity.update;

import net.edge.net.codec.GameBuffer;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

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
	public int write(Player player, Npc npc, GameBuffer msg) {
		msg.putCString(npc.getForcedText());
		return -1;
	}
}