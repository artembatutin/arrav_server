package net.arrav.net.packet.out;


import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

public final class SendCoordinates implements OutgoingPacket {
	
	private final Position position;
	
	public SendCoordinates(Position position) {
		this.position = position;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(85);
		out.put(position.getY() - (player.getLastRegion().getRegionY() * 8), ByteTransform.C);
		out.put(position.getX() - (player.getLastRegion().getRegionX() * 8), ByteTransform.C);
		return out;
	}
}
