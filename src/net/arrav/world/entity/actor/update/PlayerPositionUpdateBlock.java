package net.arrav.world.entity.actor.update;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.world.entity.actor.player.Player;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the {@code FACE_COORDINATE} update block.
 * @author Artem Batutin
 */
public final class PlayerPositionUpdateBlock extends PlayerUpdateBlock {
	
	/**
	 * Creates a new {@link PlayerPositionUpdateBlock}.
	 */
	public PlayerPositionUpdateBlock() {
		super(2, UpdateFlag.FACE_COORDINATE);
	}
	
	@Override
	public int write(Player player, Player other, ByteBuf buf) {
		buf.putShort(other.getFacePosition().getX(), ByteTransform.A, ByteOrder.LITTLE);
		buf.putShort(other.getFacePosition().getY(), ByteOrder.LITTLE);
		return -1;
	}
}
