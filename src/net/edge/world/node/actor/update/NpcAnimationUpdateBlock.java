package net.edge.world.node.actor.update;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.ByteOrder;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.player.Player;

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
	public int write(Player player, Npc npc, GameBuffer msg) {
		msg.putShort(npc.getAnimation().getId(), ByteOrder.LITTLE);
		msg.put(npc.getAnimation().getDelay());
		return -1;
	}
}