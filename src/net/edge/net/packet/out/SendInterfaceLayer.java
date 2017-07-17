package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.actor.player.Player;

public final class SendInterfaceLayer implements OutgoingPacket {
	
	private final int id;
	private final boolean hide;
	
	public SendInterfaceLayer(int id, boolean hide) {
		this.id = id;
		this.hide = hide;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(171);
		msg.put(hide ? 1 : 0);
		msg.putShort(id);
		return msg.getBuffer();
	}
}
