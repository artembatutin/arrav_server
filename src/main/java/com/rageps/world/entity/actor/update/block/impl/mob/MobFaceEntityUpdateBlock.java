package com.rageps.world.entity.actor.update.block.impl.mob;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.UpdateFlag;
import com.rageps.world.entity.actor.update.block.MobUpdateBlock;

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