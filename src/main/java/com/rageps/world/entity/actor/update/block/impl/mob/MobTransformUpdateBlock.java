package com.rageps.world.entity.actor.update.block.impl.mob;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.UpdateFlag;
import com.rageps.world.entity.actor.update.block.MobUpdateBlock;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code TRANSFORM} update block.
 * @author lare96 <http://github.org/lare96>
 */
public final class MobTransformUpdateBlock extends MobUpdateBlock {
	
	/**
	 * Creates a new {@link MobTransformUpdateBlock}.
	 */
	public MobTransformUpdateBlock() {
		super(0x80, UpdateFlag.TRANSFORM);
	}
	
	@Override
	public int write(Player player, Mob mob, GamePacket buf) {
		buf.putShort(mob.getTransform().orElse(-1), ByteTransform.A, ByteOrder.LITTLE);
		return -1;
	}
}