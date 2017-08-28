package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendContextMenu implements OutgoingPacket {

	private final int slot;
	private final boolean top;
	private final String option;

	public SendContextMenu(int slot, boolean top, String option) {
		this.slot = slot;
		this.top = top;
		this.option = option;
	}

	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(104, PacketType.VARIABLE_BYTE);
		msg.put(slot, ByteTransform.C);
		msg.put(top ? 1 : 0, ByteTransform.A);
		msg.putCString(option);
		msg.endVarSize();
		return msg.getBuffer();
	}
}
