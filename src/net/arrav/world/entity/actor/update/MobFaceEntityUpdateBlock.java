package net.arrav.world.entity.actor.update;

import io.netty.buffer.ByteBuf;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code FACE_ENTITY} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MobFaceEntityUpdateBlock extends MobUpdateBlock {
	
	/**
	 * Creates a new {@link MobFaceEntityUpdateBlock}.
	 */
	public MobFaceEntityUpdateBlock() {
		super(0x10, UpdateFlag.FACE_ENTITY);
	}
	
	@Override
	public int write(Player player, Mob mob, ByteBuf buf) {
		buf.putShort(mob.getFaceIndex());
		return -1;
	}
}