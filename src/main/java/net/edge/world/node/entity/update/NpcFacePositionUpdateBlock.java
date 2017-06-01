package net.edge.world.node.entity.update;

import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.ByteOrder;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

/**
 * An {@link NpcUpdateBlock} implementation that handles the {@code FACE_COORDINATE} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class NpcFacePositionUpdateBlock extends NpcUpdateBlock {

	/**
	 * Creates a new {@link NpcFacePositionUpdateBlock}.
	 */
	public NpcFacePositionUpdateBlock() {
		super(1, UpdateFlag.FACE_COORDINATE);
	}

	@Override
	public int write(Player player, Npc npc, ByteMessage msg) {
		msg.putShort(npc.getFacePosition().getX(), ByteOrder.LITTLE);
		msg.putShort(npc.getFacePosition().getY(), ByteOrder.LITTLE);
		return -1;
	}
}
