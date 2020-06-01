package com.rageps.net.packet.out;

import io.netty.buffer.ByteBuf;
import com.rageps.content.TabInterface;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;

public final class SendForceTab implements OutgoingPacket {
	
	private final TabInterface tab;
	
	public SendForceTab(TabInterface tab) {
		this.tab = tab;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(106);
		out.put(tab.getOld());
		out.put(tab.getNew());
		return out;
	}
}
