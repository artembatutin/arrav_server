package net.edge.world.model.node.entity.update;

import net.edge.net.codec.ByteMessage;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;

/**
 * An {@link NpcUpdateBlock} implementation that handles the {@code GRAPHIC} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class NpcGraphicUpdateBlock extends NpcUpdateBlock {

	/**
	 * Creates a new {@link NpcGraphicUpdateBlock}.
	 */
	public NpcGraphicUpdateBlock() {
		super(0x100, UpdateFlag.GRAPHIC);
	}

	@Override
	public int write(Player player, Npc mob, ByteMessage msg) {
		msg.putShort(mob.getGraphic().getId());
		msg.putInt(mob.getGraphic().getHeight() << 16 | mob.getGraphic().getDelay() & 0xFFFF);
		return -1;
	}
}