package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.content.TabInterface;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendTab implements OutgoingPacket {
	
	private final int id;
	private final TabInterface tab;
	
	public SendTab(int id, TabInterface tab) {
		this.id = id;
		this.tab = tab;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(71);
		buf.putShort(id);
		buf.put(tab.getOld(), ByteTransform.A);
		buf.put(tab.getNew(), ByteTransform.A);
		return buf;
	}
}
