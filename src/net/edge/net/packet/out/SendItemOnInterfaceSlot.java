package net.edge.net.packet.out;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.MessageType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

public final class SendItemOnInterfaceSlot implements OutgoingPacket {
	
	private final Item item;
	private final int id, slot;
	
	public SendItemOnInterfaceSlot(int id, Item item, int slot) {
		this.id = id;
		this.slot = slot;
		this.item = item;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(34, MessageType.VARIABLE_SHORT);
		msg.putShort(id);
		msg.put(slot);
		msg.putShort(item == null ? 0 : item.getId() + 1);
		int am = item == null ? 0 : item.getAmount();
		if(am > 254) {
			msg.put(255);
			msg.putInt(am);
		} else {
			msg.put(am);
		}
		msg.endVarSize();
	}
}
