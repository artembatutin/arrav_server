package net.arrav.net.packet.out;

import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendInterfaceColor implements OutgoingPacket {
	
	private final int id, color;
	
	public SendInterfaceColor(int id, int color) {
		this.id = id;
		this.color = color;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(122);
		out.putShort(id, ByteTransform.A, ByteOrder.LITTLE);
		out.putShort(color, ByteTransform.A, ByteOrder.LITTLE);
		return out;
	}
}
