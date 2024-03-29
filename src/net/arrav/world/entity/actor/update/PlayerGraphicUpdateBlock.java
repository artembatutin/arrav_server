package net.arrav.world.entity.actor.update;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.world.Graphic;
import net.arrav.world.entity.actor.player.Player;

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
	public int write(Player player, Player other, ByteBuf buf) {
		buf.putShort(other.getGraphic().getId(), ByteOrder.LITTLE);
		buf.putInt(other.getGraphic().getDelay() | other.getGraphic().getHeight() << 16);
		return -1;
	}
}
