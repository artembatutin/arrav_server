package net.arrav.net.packet.out;


import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.player.Player;

public final class SendArrowEntity implements OutgoingPacket {
	
	private final Actor entity;
	
	public SendArrowEntity(Actor entity) {
		this.entity = entity;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(248);
		out.put(entity.isMob() ? 1 : 10);
		out.putShort(entity.getSlot());
		out.put(0);
		return out;
	}
	
}
