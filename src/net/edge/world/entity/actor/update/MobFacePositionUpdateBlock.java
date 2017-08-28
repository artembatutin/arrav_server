package net.edge.world.entity.actor.update;

import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.GameBuffer;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code FACE_COORDINATE} update block.
 *
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
	public int write(Player player, Mob mob, GameBuffer msg) {
		msg.putShort(mob.getFacePosition().getX(), ByteOrder.LITTLE);
		msg.putShort(mob.getFacePosition().getY(), ByteOrder.LITTLE);
		return -1;
	}
}