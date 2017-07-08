package net.edge.net.packet.out;

import net.edge.locale.Position;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendPrivateMessageStatus implements OutgoingPacket {
	
	private final int code;
	
	public SendPrivateMessageStatus(int code) {
		this.code = code;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(221);
		msg.put(code);
	}
}
