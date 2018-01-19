package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.content.TabInterface;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendForceTab implements OutgoingPacket {
	
	private final TabInterface tab;
	
	public SendForceTab(TabInterface tab) {
		this.tab = tab;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(106);
		buf.put(tab.getOld());
		buf.put(tab.getNew());
		return buf;
	}
}
