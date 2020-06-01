package com.rageps.world.entity.actor.update;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code ANIMATION} update block.
 * @author Artem Batutin
 */
final class MobAnimationUpdateBlock extends MobUpdateBlock {
	
	/**
	 * Creates a new {@link MobAnimationUpdateBlock}.
	 */
	MobAnimationUpdateBlock() {
		super(8, UpdateFlag.ANIMATION);
	}
	
	@Override
	public int write(Player player, Mob mob, GamePacket buf) {
		buf.putShort(mob.getAnimation().getId(), ByteOrder.LITTLE);
		buf.put(mob.getAnimation().getDelay());
		return -1;
	}
}