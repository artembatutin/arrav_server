package net.edge.net.packet.out;

import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendUpdateTimer implements OutgoingPacket {
	
	private final int timer;
	
	public SendUpdateTimer(int timer) {
		this.timer = timer;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(114);
		msg.putShort(timer, ByteOrder.LITTLE);
	}
}
