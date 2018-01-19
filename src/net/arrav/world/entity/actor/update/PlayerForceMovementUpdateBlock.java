package net.arrav.world.entity.actor.update;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.world.entity.actor.move.ForcedMovement;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

/**
 * An {@link PlayerUpdateBlock} implementation that handles the {@link ForcedMovement} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class PlayerForceMovementUpdateBlock extends PlayerUpdateBlock {
	
	/**
	 * Creates a new {@link PlayerForceMovementUpdateBlock}.
	 */
	public PlayerForceMovementUpdateBlock() {
		super(0x400, UpdateFlag.FORCE_MOVEMENT);
	}
	
	@Override
	public int write(Player player, Player other, ByteBuf buf) {
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
