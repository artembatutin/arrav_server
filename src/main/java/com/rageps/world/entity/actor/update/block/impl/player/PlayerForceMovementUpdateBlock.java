package com.rageps.world.entity.actor.update.block.impl.player;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.move.ForcedMovement;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.UpdateFlag;
import com.rageps.world.entity.actor.update.block.PlayerUpdateBlock;
import com.rageps.world.locale.Position;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the {@link ForcedMovement} update block.
 * @author Artem Batutin
 */
public final class PlayerForceMovementUpdateBlock extends PlayerUpdateBlock {
	
	/**
	 * Creates a new {@link PlayerForceMovementUpdateBlock}.
	 */
	public PlayerForceMovementUpdateBlock() {
		super(0x400, UpdateFlag.FORCE_MOVEMENT);
	}
	
	@Override
	public int write(Player player, Player other, GamePacket buf) {
		ForcedMovement movement = other.getForcedMovement();
		Position lastRegion = player.getLastRegion();
		Position position = other.getPosition();
		
		int firstVelocity = (movement.getFirstSpeed());
		int secondVelocity = (movement.getSecondSpeed());
		int direction = movement.getDirection().getId();
		int firstX = movement.getFirst().getX() - position.getX();
		int firstY = movement.getFirst().getY() - position.getY();
		int secondX = movement.getSecond().getX() - position.getX();
		int secondY = movement.getSecond().getY() - position.getY();
		
		buf.put(position.getLocalX(lastRegion) + firstX, ByteTransform.S);
		buf.put(position.getLocalY(lastRegion) + firstY, ByteTransform.S);
		buf.put(position.getLocalX(lastRegion) + secondX, ByteTransform.S);
		buf.put(position.getLocalY(lastRegion) + secondY, ByteTransform.S);
		buf.putShort(firstVelocity, ByteTransform.A, ByteOrder.LITTLE);
		buf.putShort(secondVelocity, ByteTransform.A);
		buf.put(direction, ByteTransform.S);
		return -1;
	}
}
