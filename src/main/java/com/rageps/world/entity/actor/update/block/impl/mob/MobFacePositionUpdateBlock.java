package com.rageps.world.entity.actor.update.block.impl.mob;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.block.MobUpdateBlock;
import com.rageps.world.entity.actor.update.UpdateFlag;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code FACE_COORDINATE} update block.
 * @author Artem Batutin
 */
public final class MobFacePositionUpdateBlock extends MobUpdateBlock {
	
	/**
	 * Creates a new {@link MobFacePositionUpdateBlock}.
	 */
	public MobFacePositionUpdateBlock() {
		super(1, UpdateFlag.FACE_COORDINATE);
	}
	
	@Override
	public int write(Player player, Mob mob, GamePacket buf) {
		buf.putShort(mob.getFacePosition().getX(), ByteOrder.LITTLE);
		buf.putShort(mob.getFacePosition().getY(), ByteOrder.LITTLE);
		return -1;
	}
}