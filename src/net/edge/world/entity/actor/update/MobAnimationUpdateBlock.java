package net.edge.world.entity.actor.update;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.ByteOrder;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code ANIMATION} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
final class MobAnimationUpdateBlock extends MobUpdateBlock {
	
	/**
	 * Creates a new {@link MobAnimationUpdateBlock}.
	 */
	MobAnimationUpdateBlock() {
		super(8, UpdateFlag.ANIMATION);
	}
	
	@Override
	public int write(Player player, Mob mob, GameBuffer msg) {
		msg.putShort(mob.getAnimation().getId(), ByteOrder.LITTLE);
		msg.put(mob.getAnimation().getDelay());
		return -1;
	}
}