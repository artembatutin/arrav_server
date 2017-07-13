package net.edge.world.node.entity.update;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.world.Animation;
import net.edge.world.node.entity.player.Player;

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
	public int write(Player player, Player other, GameBuffer msg) {
		msg.putShort(other.getAnimation().getId(), ByteOrder.LITTLE);
		msg.put(other.getAnimation().getDelay(), ByteTransform.C);
		return -1;
	}
}
