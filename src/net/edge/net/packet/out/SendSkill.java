package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendSkill implements OutgoingPacket {
	
	private final int id, level, exp;
	
	public SendSkill(int id, int level, int exp) {
		this.id = id;
		this.level = level;
		this.exp = exp;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(134);
		buf.put(id);
		buf.putInt(exp, ByteOrder.MIDDLE);
		buf.putInt(level);
		return buf;
	}
}
