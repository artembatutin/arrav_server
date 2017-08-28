package net.edge.world.entity.actor.update;

import net.edge.net.codec.GameBuffer;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code GRAPHIC} update block.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MobGraphicUpdateBlock extends MobUpdateBlock {

	/**
	 * Creates a new {@link MobGraphicUpdateBlock}.
	 */
	public MobGraphicUpdateBlock() {
		super(0x100, UpdateFlag.GRAPHIC);
	}

	@Override
	public int write(Player player, Mob mob, GameBuffer msg) {
		msg.putShort(mob.getGraphic().getId());
		msg.putInt(mob.getGraphic().getHeight() << 16 | mob.getGraphic().getDelay() & 0xFFFF);
		return -1;
	}
}