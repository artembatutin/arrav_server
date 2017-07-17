package net.edge.world.entity.actor.update;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.ByteOrder;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.player.Player;

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
	public int write(Player player, Player other, GameBuffer msg) {
		msg.putShort(other.getGraphic().getId(), ByteOrder.LITTLE);
		msg.putInt(other.getGraphic().getDelay() | other.getGraphic().getHeight() << 16);
		return -1;
	}
}
