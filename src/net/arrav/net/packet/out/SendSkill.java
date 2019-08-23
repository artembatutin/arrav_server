package net.arrav.net.packet.out;

import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendSkill implements OutgoingPacket {
	
	private final int id, level, exp;
	
	public SendSkill(int id, int level, int exp) {
		this.id = id;
		this.level = level;
		this.exp = exp;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(134);
		out.put(id);
		out.putInt(exp, ByteOrder.MIDDLE);
		out.putInt(level);
		return out;
	}
}
