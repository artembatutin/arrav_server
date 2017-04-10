package net.edge.world.model.node.entity.update;

import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.ByteOrder;

/**
 * An {@link NpcUpdateBlock} implementation that handles the {@code ANIMATION} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
final class NpcAnimationUpdateBlock extends NpcUpdateBlock {

	/**
	 * Creates a new {@link NpcAnimationUpdateBlock}.
	 */
	NpcAnimationUpdateBlock() {
		super(8, UpdateFlag.ANIMATION);
	}

	@Override
	public int write(Player player, Npc npc, ByteMessage msg) {
		msg.putShort(npc.getAnimation().getId(), ByteOrder.LITTLE);
		msg.put(npc.getAnimation().getDelay());
		return -1;
	}
}
