package net.edge.net.packet.out;

import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendInterfaceLayer implements OutgoingPacket {
	
	private final int id;
	private final boolean hide;
	
	public SendInterfaceLayer(int id, boolean hide) {
		this.id = id;
		this.hide = hide;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(171);
		msg.put(hide ? 1 : 0);
		msg.putShort(id);
	}
}
