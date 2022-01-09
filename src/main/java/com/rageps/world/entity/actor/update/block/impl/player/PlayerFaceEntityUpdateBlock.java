package com.rageps.world.entity.actor.update.block.impl.player;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.UpdateFlag;
import com.rageps.world.entity.actor.update.block.PlayerUpdateBlock;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the {@code FACE_ENTITY} update block.
 * @author Artem Batutin
 */
public final class PlayerFaceEntityUpdateBlock extends PlayerUpdateBlock {
	
	/**
	 * Creates a new {@link PlayerFaceEntityUpdateBlock}.
	 */
	public PlayerFaceEntityUpdateBlock() {
		super(1, UpdateFlag.FACE_ENTITY);
	}
	
	@Override
	public int write(Player player, Player other, GamePacket buf) {
		buf.putShort(other.getFaceIndex(), ByteOrder.LITTLE);
		return -1;
	}
}
