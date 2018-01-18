package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendInterfaceAnimation implements OutgoingPacket {
	
	private final int id, animation;
	
	public SendInterfaceAnimation(int id, int animation) {
		this.id = id;
		this.animation = animation;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(200);
		buf.putShort(id);
		buf.putShort(animation);
		return buf;
	}
}
