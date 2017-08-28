package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.content.TabInterface;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendForceTab implements OutgoingPacket {

	private final TabInterface tab;

	public SendForceTab(TabInterface tab) {
		this.tab = tab;
	}

	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(106);
		msg.put(tab.getOld());
		msg.put(tab.getNew());
		return msg.getBuffer();
	}
}
