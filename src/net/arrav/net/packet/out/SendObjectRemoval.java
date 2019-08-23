package net.arrav.net.packet.out;

import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.object.GameObject;

public final class SendObjectRemoval implements OutgoingPacket {
	
	private final GameObject object;
	
	public SendObjectRemoval(GameObject object) {
		this.object = object;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(101);
		out.put((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ByteTransform.C);
		out.put(0);
		return out;
	}
	
	@Override
	public GamePacket coordinatePacket(Player player) {
		return new SendCoordinates(object.getPosition()).write(player);
	}
}
