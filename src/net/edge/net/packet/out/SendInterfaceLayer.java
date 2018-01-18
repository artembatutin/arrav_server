package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendInterfaceLayer implements OutgoingPacket {
	
	private final int id;
	private final boolean hide;
	
	public SendInterfaceLayer(int id, boolean hide) {
		this.id = id;
		this.hide = hide;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(171);
		buf.put(hide ? 1 : 0);
		buf.putShort(id);
		return buf;
	}
}
