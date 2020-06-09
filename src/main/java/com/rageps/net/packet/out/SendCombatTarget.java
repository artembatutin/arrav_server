package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendCombatTarget implements OutgoingPacket {

	private final Actor opponent;

	public SendCombatTarget(Actor opponent) {
		this.opponent = opponent;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(174);
		out.putShort(opponent == null ? 0 : opponent.getSlot());
		out.put(opponent == null ? 1 : opponent.isPlayer() ? 2 : 3);
		return out;
	}

}
