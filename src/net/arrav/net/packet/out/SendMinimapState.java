package net.arrav.net.packet.out;

import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendMinimapState implements OutgoingPacket {
	
	private final int code;
	
	public SendMinimapState(int code) {
		this.code = code;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(99);
		out.put(code);
		return out;
	}
}
