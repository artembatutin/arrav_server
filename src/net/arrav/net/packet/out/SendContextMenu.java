package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

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
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(104, GamePacketType.VARIABLE_BYTE);
		buf.put(slot, ByteTransform.C);
		buf.put(top ? 1 : 0, ByteTransform.A);
		buf.putCString(option);
		buf.endVarSize();
		return buf;
	}
}
