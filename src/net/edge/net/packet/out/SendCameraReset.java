package net.edge.net.packet.out;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendCameraReset implements OutgoingPacket {
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(107);
		return msg.getBuffer();
	}
}
