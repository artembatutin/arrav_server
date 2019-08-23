package net.arrav.world.entity.actor.update;

import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

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