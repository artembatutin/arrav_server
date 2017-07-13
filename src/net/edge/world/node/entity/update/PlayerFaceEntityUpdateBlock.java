package net.edge.world.node.entity.update;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.ByteOrder;
import net.edge.world.node.entity.player.Player;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the {@code FACE_ENTITY} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class PlayerFaceEntityUpdateBlock extends PlayerUpdateBlock {

	/**
	 * Creates a new {@link PlayerFaceEntityUpdateBlock}.
	 */
	public PlayerFaceEntityUpdateBlock() {
		super(1, UpdateFlag.FACE_ENTITY);
	}

	@Override
	public int write(Player player, Player other, GameBuffer msg) {
		msg.putShort(other.getFaceIndex(), ByteOrder.LITTLE);
		return -1;
	}
}
