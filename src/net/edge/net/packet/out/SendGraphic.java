package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.world.locale.Position;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

import static net.edge.world.entity.EntityState.INACTIVE;

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
	public ByteBuf write(Player player, GameBuffer msg) {
		new SendCoordinates(position).write(player, msg);
		msg.message(4);
		msg.put(0);
		msg.putShort(id);
		msg.put(level);
		msg.putShort(0);
		return msg.getBuffer();
	}
}
