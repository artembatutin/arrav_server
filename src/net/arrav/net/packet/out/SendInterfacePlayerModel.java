package net.arrav.net.packet.out;


import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendInterfacePlayerModel implements OutgoingPacket {
	
	private final int id;
	
	public SendInterfacePlayerModel(int id) {
		this.id = id;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(185);
		out.putShort(id, ByteTransform.A, ByteOrder.LITTLE);
		return out;
	}
}
