package net.edge.net.packet.out;

import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendSkill implements OutgoingPacket {
	
	private final int id, level, exp;
	
	public SendSkill(int id, int level, int exp) {
		this.id = id;
		this.level = level;
		this.exp = exp;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(134);
		msg.put(id);
		msg.putInt(exp, ByteOrder.MIDDLE);
		msg.putInt(level);
	}
}
