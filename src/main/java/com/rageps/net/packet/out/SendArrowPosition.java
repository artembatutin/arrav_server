package com.rageps.net.packet.out;

import io.netty.buffer.ByteBuf;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;

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
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(254);
		out.put(direction);
		out.putShort(position.getX());
		out.putShort(position.getY());
		out.put(position.getZ());
		return out;
	}
}
