package net.arrav.net.packet.out;

import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendClearContainer implements OutgoingPacket {
	
	private final int id;
	
	public SendClearContainer(int id) {
		this.id = id;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(40);
		out.putShort(id);
		return out;
	}
}
