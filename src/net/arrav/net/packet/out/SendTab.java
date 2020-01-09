package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.content.TabInterface;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
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
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(71);
		out.putShort(id);
		out.put(tab.getOld(), ByteTransform.A);
		out.put(tab.getNew(), ByteTransform.A);
		return out;
	}
}
