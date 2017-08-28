package net.edge.world.entity.actor.update;

import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.move.ForcedMovement;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code TRANSFORM} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MobForceMovementUpdateBlock extends MobUpdateBlock {
	
	/**
	 * Creates a new {@link MobForceMovementUpdateBlock}.
	 */
	public MobForceMovementUpdateBlock() {
		super(0x400, UpdateFlag.FORCE_MOVEMENT);
	}
	
	@Override
	public int write(Player player, Mob mob, GameBuffer msg) {
		ForcedMovement movement = mob.getForcedMovement();
		Position lastRegion = player.getLastRegion();
		Position position = mob.getPosition();
		
		int firstVelocity = (movement.getFirstSpeed());
		int secondVelocity = (movement.getSecondSpeed());
		int direction = movement.getDirection().getId();
		int firstX = movement.getFirst().getX() - position.getX();
		int firstY = movement.getFirst().getY() - position.getY();
		int secondX = movement.getSecond().getX() - position.getX();
		int secondY = movement.getSecond().getY() - position.getY();
		
		msg.put(position.getLocalX(lastRegion) + firstX, ByteTransform.S);
		msg.put(position.getLocalY(lastRegion) + firstY, ByteTransform.S);
		msg.put(position.getLocalX(lastRegion) + secondX, ByteTransform.S);
		msg.put(position.getLocalY(lastRegion) + secondY, ByteTransform.S);
		msg.putShort(firstVelocity, ByteTransform.A, ByteOrder.LITTLE);
		msg.putShort(secondVelocity, ByteTransform.A);
		msg.put(direction, ByteTransform.S);
		return -1;
	}
}