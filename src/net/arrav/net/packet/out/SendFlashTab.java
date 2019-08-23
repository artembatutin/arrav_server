package net.arrav.net.packet.out;

import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendFlashTab implements OutgoingPacket {
	
	private final int code;
	
	public SendFlashTab(int code) {
		this.code = code;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(24);
		out.put(code, ByteTransform.A);
		return out;
	}
}
