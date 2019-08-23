package net.arrav.world.entity.actor.update;

import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.player.Player;

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
