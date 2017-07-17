package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.locale.Position;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.actor.player.Player;

public final class SendCameraAngle implements OutgoingPacket {
	
	private final int height, movementSpeed, rotationSpeed;
	private final Position position;
	
	public SendCameraAngle(Position position, int height, int movementSpeed, int rotationSpeed) {
		this.height = height;
		this.movementSpeed = movementSpeed;
		this.rotationSpeed = rotationSpeed;
		this.position = position;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(177);
		msg.put(position.getLocalX(player.getPosition()));
		msg.put(position.getLocalY(player.getPosition()));
		msg.putShort(height);
		msg.put(movementSpeed);
		msg.put(rotationSpeed);
		return msg.getBuffer();
	}
}
