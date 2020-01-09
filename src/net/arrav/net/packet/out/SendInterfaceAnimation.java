package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendInterfaceAnimation implements OutgoingPacket {
	
	private final int id, animation;
	
	public SendInterfaceAnimation(int id, int animation) {
		this.id = id;
		this.animation = animation;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(200);
		out.putShort(id);
		out.putShort(animation);
		return out;
	}
}
