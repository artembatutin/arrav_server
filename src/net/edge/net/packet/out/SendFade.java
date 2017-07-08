package net.edge.net.packet.out;

import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

public final class SendFade implements OutgoingPacket {
	
	private final int start, duration, end;
	
	public SendFade(int start, int duration, int end) {
		this.start = start;
		this.duration = duration;
		this.end = end;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(80);
		msg.put(start);
		msg.put(duration);
		msg.put(end);
	}
}
