package com.rageps.net.packet.out;

import io.netty.buffer.ByteBuf;
import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.GroundItem;

public final class SendItemNode implements OutgoingPacket {
	
	private final GroundItem item;
	
	public SendItemNode(GroundItem item) {
		this.item = item;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(44);
		out.putShort(item.getItem().getId(), ByteTransform.A, ByteOrder.LITTLE);
		out.putShort(item.getItem().getAmount());
		out.put(0);
		return out;
	}
	
	@Override
	public OutgoingPacket coordinatePacket() {
		return new SendCoordinates(item.getPosition());
	}
}
