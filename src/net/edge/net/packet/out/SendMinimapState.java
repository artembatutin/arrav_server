package net.edge.net.packet.out;

import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendMinimapState implements OutgoingPacket {
	
	private final int code;
	
	public SendMinimapState(int code) {
		this.code = code;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer str = player.getSession().getStream();
		str.message(99);
		str.put(code);
	}
}
