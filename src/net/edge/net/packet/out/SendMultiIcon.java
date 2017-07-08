package net.edge.net.packet.out;

import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendMultiIcon implements OutgoingPacket {
	
	private final boolean hide;
	
	public SendMultiIcon(boolean hide) {
		this.hide = hide;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(61);
		msg.put(hide ? 0 : 1);
	}
}
