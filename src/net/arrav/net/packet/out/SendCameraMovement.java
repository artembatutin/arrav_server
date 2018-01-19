package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

public final class SendCameraMovement implements OutgoingPacket {
	
	private final int height, movementSpeed, rotationSpeed;
	private final Position position;
	
	public SendCameraMovement(Position position, int height, int movementSpeed, int rotationSpeed) {
		this.height = height;
		this.movementSpeed = movementSpeed;
		this.rotationSpeed = rotationSpeed;
		this.position = position;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(166);
		buf.put(position.getLocalX(player.getPosition()));
		buf.put(position.getLocalY(player.getPosition()));
		buf.putShort(height);
		buf.put(movementSpeed);
		buf.put(rotationSpeed);
		return buf;
	}
}
