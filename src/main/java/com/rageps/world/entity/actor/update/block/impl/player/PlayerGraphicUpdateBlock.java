package com.rageps.world.entity.actor.update.block.impl.player;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.UpdateFlag;
import com.rageps.world.entity.actor.update.block.PlayerUpdateBlock;
import com.rageps.world.model.Graphic;

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
