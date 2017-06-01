package net.edge.world.node.entity.update;

import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.world.node.entity.player.Player;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the {@code FACE_COORDINATE} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class PlayerPositionUpdateBlock extends PlayerUpdateBlock {

	/**
	 * Creates a new {@link PlayerPositionUpdateBlock}.
	 */
	public PlayerPositionUpdateBlock() {
		super(2, UpdateFlag.FACE_COORDINATE);
	}

	@Override
	public int write(Player player, Player mob, ByteMessage msg) {
		msg.putShort(mob.getFacePosition().getX(), ByteTransform.A, ByteOrder.LITTLE);
		msg.putShort(mob.getFacePosition().getY(), ByteOrder.LITTLE);
		return -1;
	}
}
