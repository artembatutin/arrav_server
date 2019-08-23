package net.arrav.net.packet.out;

import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.GroundItem;

public final class SendItemNode implements OutgoingPacket {
	
	private final GroundItem item;
	
	public SendItemNode(GroundItem item) {
		this.item = item;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(44);
		out.putShort(item.getItem().getId(), ByteTransform.A, ByteOrder.LITTLE);
		out.putShort(item.getItem().getAmount());
		out.put(0);
		return out;
	}
	
	@Override
	public GamePacket coordinatePacket(Player player) {
		return new SendCoordinates(item.getPosition()).write(player);
	}
}
