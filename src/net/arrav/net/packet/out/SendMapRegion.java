package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

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
