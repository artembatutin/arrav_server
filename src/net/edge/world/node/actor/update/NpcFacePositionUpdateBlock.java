package net.edge.world.node.actor.update;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.ByteOrder;
import net.edge.world.node.actor.mob.Mob;
import net.edge.world.node.actor.player.Player;

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
	public int write(Player player, Mob mob, GameBuffer msg) {
		msg.putShort(mob.getFacePosition().getX(), ByteOrder.LITTLE);
		msg.putShort(mob.getFacePosition().getY(), ByteOrder.LITTLE);
		return -1;
	}
}