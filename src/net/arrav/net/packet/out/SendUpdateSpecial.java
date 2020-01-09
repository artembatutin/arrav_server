package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendUpdateSpecial implements OutgoingPacket {
	
	private final int id, amount;
	
	public SendUpdateSpecial(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(70);
		out.putShort(amount);
		out.putShort(0, ByteOrder.LITTLE);
		out.putShort(id, ByteOrder.LITTLE);
		return out;
	}
}
