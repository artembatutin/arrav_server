package net.arrav.net.packet.out;

import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendInterfaceLayer implements OutgoingPacket {
	
	private final int id;
	private final boolean hide;
	
	public SendInterfaceLayer(int id, boolean hide) {
		this.id = id;
		this.hide = hide;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(171);
		out.put(hide ? 1 : 0);
		out.putShort(id);
		return out;
	}
}
