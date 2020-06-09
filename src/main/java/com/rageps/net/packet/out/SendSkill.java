package com.rageps.net.packet.out;

import io.netty.buffer.ByteBuf;
import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;

public final class SendSkill implements OutgoingPacket {
	
	private final int id, level, exp;

	private final boolean login;
	
	public SendSkill(int id, int level, int exp, boolean login) {
		this.id = id;
		this.level = level;
		this.exp = exp;
		this.login = login;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(134);
		out.put(id);
		out.putInt(exp, ByteOrder.MIDDLE);
		out.putInt(level);
		out.put(login ? 1 : 0);
		return out;
	}
}
