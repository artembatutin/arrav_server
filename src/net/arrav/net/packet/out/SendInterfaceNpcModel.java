package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendInterfaceNpcModel implements OutgoingPacket {
	
	private final int id, model;
	
	public SendInterfaceNpcModel(int id, int model) {
		this.id = id;
		this.model = model;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(75);
		out.putShort(model, ByteTransform.A, ByteOrder.LITTLE);
		out.putShort(id, ByteTransform.A, ByteOrder.LITTLE);
		return out;
	}
}
