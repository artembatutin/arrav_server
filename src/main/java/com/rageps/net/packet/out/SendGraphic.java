package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;
import io.netty.buffer.ByteBuf;

public final class SendGraphic implements OutgoingPacket {
	
	private final int id, level;
	private final Position position;
	
	public static void local(Player player, int id, Position position, int level) {
		if(player.getState() == EntityState.INACTIVE)
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
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(4);
		out.put(0);
		out.putShort(id);
		out.put(level);
		out.putShort(0);
		return out;
	}
	
	@Override
	public OutgoingPacket coordinatePacket() {
		return new SendCoordinates(position);
	}
}
