package net.arrav.net.packet.out;

import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

import static net.arrav.world.entity.EntityState.INACTIVE;

public final class SendGraphic implements OutgoingPacket {
	
	private final int id, level;
	private final Position position;
	
	public static void local(Player player, int id, Position position, int level) {
		if(player.getState() == INACTIVE)
			return;
		player.out(new SendGraphic(id, position, level));
		for(Player p : player.getLocalPlayers()) {
			if(p == null)
				continue;
			p.out(new SendGraphic(id, position, level));
		}
	}
	
	public SendGraphic(int id, Position position, int level) {
		this.id = id;
		this.position = position;
		this.level = level;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(4);
		out.put(0);
		out.putShort(id);
		out.put(level);
		out.putShort(0);
		return out;
	}
	
	@Override
	public GamePacket coordinatePacket(Player player) {
		return new SendCoordinates(position).write(player);
	}
}
