package net.arrav.net.packet.out;


import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendMultiIcon implements OutgoingPacket {
	
	private final boolean hide;
	
	public SendMultiIcon(boolean hide) {
		this.hide = hide;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(61);
		out.put(hide ? 0 : 1);
		return out;
	}
}
