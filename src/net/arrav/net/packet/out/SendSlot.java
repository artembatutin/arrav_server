package net.arrav.net.packet.out;


import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendSlot implements OutgoingPacket {
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(249);
		out.put(1, ByteTransform.A);
		out.putShort(player.getSlot(), ByteTransform.A, ByteOrder.LITTLE);
		return out;
	}
}
