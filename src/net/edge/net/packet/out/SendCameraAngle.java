package net.edge.net.packet.out;

import net.edge.locale.Position;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

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
	public void write(Player player) {
		GameBuffer str = player.getSession().getStream();
		str.message(177);
		str.put(position.getLocalX(player.getPosition()));
		str.put(position.getLocalY(player.getPosition()));
		str.putShort(height);
		str.put(movementSpeed);
		str.put(rotationSpeed);
	}
}
