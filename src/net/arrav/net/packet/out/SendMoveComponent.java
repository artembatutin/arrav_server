package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendMoveComponent implements OutgoingPacket {
	
	private final int id, x, y;
	
	public SendMoveComponent(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(70);
		out.putShort(x);
		out.putShort(y, ByteOrder.LITTLE);
		out.putShort(id, ByteOrder.LITTLE);
		return out;
	}
}
