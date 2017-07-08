package net.edge.net.packet.out;

import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendFlashTab implements OutgoingPacket {
	
	private final int code;
	
	public SendFlashTab(int code) {
		this.code = code;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(24);
		msg.put(code, ByteTransform.A);
	}
}
