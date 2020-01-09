package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendPrivateMessageStatus implements OutgoingPacket {
	
	private final int code;
	
	public SendPrivateMessageStatus(int code) {
		this.code = code;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(221);
		out.put(code);
		return out;
	}
}
