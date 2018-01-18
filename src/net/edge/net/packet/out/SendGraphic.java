package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;

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
	public ByteBuf write(Player player, ByteBuf buf) {
		new SendCoordinates(position).write(player, buf);
		buf.message(4);
		buf.put(0);
		buf.putShort(id);
		buf.put(level);
		buf.putShort(0);
		return buf;
	}
}
