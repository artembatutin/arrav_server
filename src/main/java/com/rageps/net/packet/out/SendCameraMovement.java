package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;
import io.netty.buffer.ByteBuf;

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
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(166);
		out.put(position.getLocalX(player.getPosition()));
		out.put(position.getLocalY(player.getPosition()));
		out.putShort(height);
		out.put(movementSpeed);
		out.put(rotationSpeed);
		return out;
	}
}
