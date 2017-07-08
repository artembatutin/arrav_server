package net.edge.net.packet.out;

import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.MessageType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendInventoryInterface implements OutgoingPacket {
	
	private final int open, overlay;
	
	public SendInventoryInterface(int open, int overlay) {
		this.open = open;
		this.overlay = overlay;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(248);
		msg.putShort(open, ByteTransform.A);
		msg.putShort(overlay);
	}
}
