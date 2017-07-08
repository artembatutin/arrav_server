package net.edge.net.packet.out;

import net.edge.locale.Position;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.MessageType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

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
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(104, MessageType.VARIABLE);
		msg.put(slot, ByteTransform.C);
		msg.put(top ? 1 : 0, ByteTransform.A);
		msg.putCString(option);
		msg.endVarSize();
	}
}
