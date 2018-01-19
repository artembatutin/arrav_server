package net.arrav.world.entity.actor.update;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.world.entity.actor.player.Player;

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
	public int write(Player player, Player other, ByteBuf buf) {
		buf.putShort(other.getFaceIndex(), ByteOrder.LITTLE);
		return -1;
	}
}
