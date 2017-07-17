package net.edge.world.node.entity.update;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.locale.Position;
import net.edge.world.node.entity.move.ForcedMovement;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

/**
 * An {@link NpcUpdateBlock} implementation that handles the {@code TRANSFORM} update block.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class NpcForceMovementUpdateBlock extends NpcUpdateBlock {
	
	/**
	 * Creates a new {@link NpcForceMovementUpdateBlock}.
	 */
	public NpcForceMovementUpdateBlock() {
		super(0x400, UpdateFlag.FORCE_MOVEMENT);
	}
	
	@Override
	public int write(Player player, Npc npc, GameBuffer msg) {
		ForcedMovement movement = npc.getForcedMovement();
		Position lastRegion = player.getLastRegion();
		Position position = npc.getPosition();
		
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