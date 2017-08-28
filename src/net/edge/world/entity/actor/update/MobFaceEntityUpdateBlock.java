package net.edge.world.entity.actor.update;

import net.edge.net.codec.GameBuffer;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

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
	public int write(Player player, Mob mob, GameBuffer msg) {
		msg.putShort(mob.getFaceIndex());
		return -1;
	}
}