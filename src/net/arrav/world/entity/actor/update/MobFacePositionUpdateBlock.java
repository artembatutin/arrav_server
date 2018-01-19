package net.arrav.world.entity.actor.update;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code FACE_COORDINATE} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MobFacePositionUpdateBlock extends MobUpdateBlock {
	
	/**
	 * Creates a new {@link MobFacePositionUpdateBlock}.
	 */
	public MobFacePositionUpdateBlock() {
		super(1, UpdateFlag.FACE_COORDINATE);
	}
	
	@Override
	public int write(Player player, Mob mob, ByteBuf buf) {
		buf.putShort(mob.getFacePosition().getX(), ByteOrder.LITTLE);
		buf.putShort(mob.getFacePosition().getY(), ByteOrder.LITTLE);
		return -1;
	}
}