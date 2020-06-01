package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import io.netty.buffer.ByteBuf;

public final class SendItemOnInterfaceSlot implements OutgoingPacket {
	
	private final Item item;
	private final int id, slot;
	
	public SendItemOnInterfaceSlot(int id, Item item, int slot) {
		this.id = id;
		this.slot = slot;
		this.item = item;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(34, GamePacketType.VARIABLE_SHORT);
		out.putShort(id);
		out.put(slot);
		out.putShort(item == null ? 0 : item.getId() + 1);
		int am = item == null ? 0 : item.getAmount();
		if(am > 254) {
			out.put(255);
			out.putInt(am);
		} else {
			out.put(am);
		}
		out.endVarSize();
		return out;
	}
}
