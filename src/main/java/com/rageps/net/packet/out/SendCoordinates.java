package com.rageps.net.packet.out;

import io.netty.buffer.ByteBuf;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;

public final class SendCoordinates implements OutgoingPacket {
	
	private final Position position;
	
	public SendCoordinates(Position position) {
		this.position = position;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(85);
		out.put(position.getY() - (player.getLastRegion().getRegionY() * 8), ByteTransform.C);
		out.put(position.getX() - (player.getLastRegion().getRegionX() * 8), ByteTransform.C);
		return out;
	}
}
