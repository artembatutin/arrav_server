package com.rageps.net.packet.out;

import io.netty.buffer.ByteBuf;
import com.rageps.content.TabInterface;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;

public final class SendTab implements OutgoingPacket {
	
	private final int id;
	private final TabInterface tab;
	
	public SendTab(int id, TabInterface tab) {
		this.id = id;
		this.tab = tab;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(71);
		out.putShort(id);
		out.put(tab.getOld(), ByteTransform.A);
		out.put(tab.getNew(), ByteTransform.A);
		return out;
	}
}
