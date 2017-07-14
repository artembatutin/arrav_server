package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.locale.Position;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendArrowPosition implements OutgoingPacket {
	
	private final Position position;
	private final int direction;
	
	/**
	 * The message that sends a hint arrow on a position.
	 * @param position  the position to send the arrow on.
	 * @param direction the direction on the position to send the arrow on. The
	 *                  possible directions to put the arrow on are as follows:
	 *                  <p>
	 *                  <p>
	 *                  Middle: 2
	 *                  <p>
	 *                  West: 3
	 *                  <p>
	 *                  East: 4
	 *                  <p>
	 *                  South: 5
	 *                  <p>
	 *                  North: 6
	 *                  <p>
	 *                  <p>
	 */
	public SendArrowPosition(Position position, int direction) {
		this.position = position;
		this.direction = direction;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(254);
		msg.put(direction);
		msg.putShort(position.getX());
		msg.putShort(position.getY());
		msg.put(position.getZ());
		return msg.getBuffer();
	}
}
