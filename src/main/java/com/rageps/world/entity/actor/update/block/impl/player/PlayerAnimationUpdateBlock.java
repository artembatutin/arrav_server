package com.rageps.world.entity.actor.update.block.impl.player;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.block.PlayerUpdateBlock;
import com.rageps.world.entity.actor.update.UpdateFlag;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the {@link Animation} update block.
 * @author lare96 <http://github.org/lare96>
 */
public final class PlayerAnimationUpdateBlock extends PlayerUpdateBlock {
	
	/**
	 * Creates a new {@link PlayerAnimationUpdateBlock}.
	 */
	public PlayerAnimationUpdateBlock() {
		super(8, UpdateFlag.ANIMATION);
	}
	
	@Override
	public int write(Player player, Player other, GamePacket buf) {
		buf.putShort(other.getAnimation().getId(), ByteOrder.LITTLE);
		buf.put(other.getAnimation().getDelay(), ByteTransform.C);
		return -1;
	}
}
