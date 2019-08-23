package net.arrav.world.entity.actor.update;

import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.world.Graphic;
import net.arrav.world.entity.actor.player.Player;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the {@link Graphic} update block.
 * @author Artem Batutin
 */
public final class PlayerGraphicUpdateBlock extends PlayerUpdateBlock {
	
	/**
	 * Creates a new {@link PlayerGraphicUpdateBlock}.
	 */
	public PlayerGraphicUpdateBlock() {
		super(0x100, UpdateFlag.GRAPHIC);
	}
	
	@Override
	public int write(Player player, Player other, GamePacket buf) {
		buf.putShort(other.getGraphic().getId(), ByteOrder.LITTLE);
		buf.putInt(other.getGraphic().getDelay() | other.getGraphic().getHeight() << 16);
		return -1;
	}
}
