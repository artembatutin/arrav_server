package com.rageps.world.entity.actor.update.block.impl.mob;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.UpdateFlag;
import com.rageps.world.entity.actor.update.block.MobUpdateBlock;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code ANIMATION} update block.
 * @author Artem Batutin
 */
public final class MobAnimationUpdateBlock extends MobUpdateBlock {
	
	/**
	 * Creates a new {@link MobAnimationUpdateBlock}.
	 */
	public MobAnimationUpdateBlock() {
		super(8, UpdateFlag.ANIMATION);
	}
	
	@Override
	public int write(Player player, Mob mob, GamePacket buf) {
		buf.putShort(mob.getAnimation().getId(), ByteOrder.LITTLE);
		buf.put(mob.getAnimation().getDelay());
		return -1;
	}
}