package com.rageps.world.entity.actor.update.block.impl.player;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.block.PlayerUpdateBlock;
import com.rageps.world.entity.actor.update.UpdateFlag;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the {@code FACE_COORDINATE} update block.
 * @author Artem Batutin
 */
public final class PlayerPositionUpdateBlock extends PlayerUpdateBlock {
	
	/**
	 * Creates a new {@link PlayerPositionUpdateBlock}.
	 */
	public PlayerPositionUpdateBlock() {
		super(2, UpdateFlag.FACE_COORDINATE);
	}
	
	@Override
	public int write(Player player, Player other, GamePacket buf) {
		buf.putShort(other.getFacePosition().getX(), ByteTransform.A, ByteOrder.LITTLE);
		buf.putShort(other.getFacePosition().getY(), ByteOrder.LITTLE);
		return -1;
	}
}
