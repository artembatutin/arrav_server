package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;

public final class SendItemOnInterfaceSlot implements OutgoingPacket {
	
	private final Item item;
	private final int id, slot;
	
	public SendItemOnInterfaceSlot(int id, Item item, int slot) {
		this.id = id;
		this.slot = slot;
		this.item = item;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(34, GamePacketType.VARIABLE_SHORT);
		buf.putShort(id);
		buf.put(slot);
		buf.putShort(item == null ? 0 : item.getId() + 1);
		int am = item == null ? 0 : item.getAmount();
		if(am > 254) {
			buf.put(255);
			buf.putInt(am);
		} else {
			buf.put(am);
		}
		buf.endVarSize();
		return buf;
	}
}
