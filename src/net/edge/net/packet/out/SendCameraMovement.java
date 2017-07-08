package net.edge.net.packet.out;

import net.edge.locale.Position;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

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
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(166);
		msg.put(position.getLocalX(player.getPosition()));
		msg.put(position.getLocalY(player.getPosition()));
		msg.putShort(height);
		msg.put(movementSpeed);
		msg.put(rotationSpeed);
	}
}
