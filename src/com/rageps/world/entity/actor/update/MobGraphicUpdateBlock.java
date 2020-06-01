package com.rageps.world.entity.actor.update;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code GRAPHIC} update block.
 * @author Artem Batutin
 */
public final class MobGraphicUpdateBlock extends MobUpdateBlock {
	
	/**
	 * Creates a new {@link MobGraphicUpdateBlock}.
	 */
	public MobGraphicUpdateBlock() {
		super(0x100, UpdateFlag.GRAPHIC);
	}
	
	@Override
	public int write(Player player, Mob mob, GamePacket buf) {
		buf.putShort(mob.getGraphic().getId());
		buf.putInt(mob.getGraphic().getHeight() << 16 | mob.getGraphic().getDelay() & 0xFFFF);
		return -1;
	}
}