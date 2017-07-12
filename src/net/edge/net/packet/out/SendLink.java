package net.edge.net.packet.out;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendLink implements OutgoingPacket {
	
	private final String link;
	
	public SendLink(String link) {
		this.link = link;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(100, PacketType.VARIABLE_BYTE);
		msg.putCString(link);
		msg.endVarSize();
	}
}
