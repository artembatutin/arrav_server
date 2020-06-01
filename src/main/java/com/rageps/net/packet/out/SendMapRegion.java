package com.rageps.net.packet.out;

import io.netty.buffer.ByteBuf;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;

public final class SendMapRegion implements OutgoingPacket {
	
	private final Position position;
	
	public SendMapRegion(Position position) {
		this.position = position;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(73);
		out.putShort(position.getRegionX() + 6, ByteTransform.A);
		out.putShort(position.getRegionY() + 6);
		return out;
	}
}
