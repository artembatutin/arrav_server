package net.edge.world.model.node.entity.update;

import net.edge.net.codec.ByteMessage;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;

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
	public int write(Player player, Npc npc, ByteMessage msg) {
		msg.putString(npc.getForcedText());
		return -1;
	}
}