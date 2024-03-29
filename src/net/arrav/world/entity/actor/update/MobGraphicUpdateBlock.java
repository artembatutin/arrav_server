package net.arrav.world.entity.actor.update;

import io.netty.buffer.ByteBuf;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code GRAPHIC} update block.
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
	public int write(Player player, Mob mob, ByteBuf buf) {
		buf.putShort(mob.getGraphic().getId());
		buf.putInt(mob.getGraphic().getHeight() << 16 | mob.getGraphic().getDelay() & 0xFFFF);
		return -1;
	}
}