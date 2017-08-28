package net.edge.world.entity.actor.update;

import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

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
	public int write(Player player, Mob mob, GameBuffer msg) {
		msg.putShort(mob.getTransform().orElse(-1), ByteTransform.A, ByteOrder.LITTLE);
		return -1;
	}
}