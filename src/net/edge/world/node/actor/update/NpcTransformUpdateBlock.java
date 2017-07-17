package net.edge.world.node.actor.update;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.player.Player;

/**
 * An {@link NpcUpdateBlock} implementation that handles the {@code TRANSFORM} update block.
 * @author lare96 <http://github.org/lare96>
 */
public final class NpcTransformUpdateBlock extends NpcUpdateBlock {
	
	/**
	 * Creates a new {@link NpcTransformUpdateBlock}.
	 */
	public NpcTransformUpdateBlock() {
		super(0x80, UpdateFlag.TRANSFORM);
	}
	
	@Override
	public int write(Player player, Npc npc, GameBuffer msg) {
		msg.putShort(npc.getTransform().orElse(-1), ByteTransform.A, ByteOrder.LITTLE);
		return -1;
	}
}