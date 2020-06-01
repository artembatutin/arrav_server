package com.rageps.world.entity.actor.update;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code FACE_ENTITY} update block.
 * @author Artem Batutin
 */
public final class MobFaceEntityUpdateBlock extends MobUpdateBlock {
	
	/**
	 * Creates a new {@link MobFaceEntityUpdateBlock}.
	 */
	public MobFaceEntityUpdateBlock() {
		super(0x10, UpdateFlag.FACE_ENTITY);
	}
	
	@Override
	public int write(Player player, Mob mob, GamePacket buf) {
		buf.putShort(mob.getFaceIndex());
		return -1;
	}
}