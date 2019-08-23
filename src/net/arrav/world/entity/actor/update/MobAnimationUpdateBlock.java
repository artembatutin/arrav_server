package net.arrav.world.entity.actor.update;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

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