package net.edge.world.model.node.entity.update;

import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.ByteOrder;
import net.edge.world.model.node.entity.model.Graphic;
import net.edge.world.model.node.entity.player.Player;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the {@link Graphic} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class PlayerGraphicUpdateBlock extends PlayerUpdateBlock {

	/**
	 * Creates a new {@link PlayerGraphicUpdateBlock}.
	 */
	public PlayerGraphicUpdateBlock() {
		super(0x100, UpdateFlag.GRAPHIC);
	}

	@Override
	public int write(Player player, Player mob, ByteMessage msg) {
		msg.putShort(mob.getGraphic().getId(), ByteOrder.LITTLE);
		msg.putInt(mob.getGraphic().getDelay() | mob.getGraphic().getHeight() << 16);
		return -1;
	}
}
