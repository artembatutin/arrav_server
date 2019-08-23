package net.arrav.net.packet.out;


import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

public final class SendArrowPosition implements OutgoingPacket {
	
	private final Position position;
	private final int direction;
	
	/**
	 * The message that sends a hint arrow on a position.
	 * @param position the position to send the arrow on.
	 * @param direction the direction on the position to send the arrow on. The
	 * possible directions to put the arrow on are as follows:
	 * <p>
	 * <p>
	 * Middle: 2
	 * <p>
	 * West: 3
	 * <p>
	 * East: 4
	 * <p>
	 * South: 5
	 * <p>
	 * North: 6
	 * <p>
	 * <p>
	 */
	public SendArrowPosition(Position position, int direction) {
		this.position = position;
		this.direction = direction;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(254);
		out.put(direction);
		out.putShort(position.getX());
		out.putShort(position.getY());
		out.put(position.getZ());
		return out;
	}
}
