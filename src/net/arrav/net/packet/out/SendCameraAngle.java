package net.arrav.net.packet.out;

import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

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
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(177);
		out.put(position.getLocalX(player.getPosition()));
		out.put(position.getLocalY(player.getPosition()));
		out.putShort(height);
		out.put(movementSpeed);
		out.put(rotationSpeed);
		return out;
	}
}
