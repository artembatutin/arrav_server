package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.actor.npc.NpcDefinition;
import net.edge.world.node.actor.npc.drop.ItemCache;
import net.edge.world.node.actor.npc.drop.NpcDrop;
import net.edge.world.node.actor.npc.drop.NpcDropTable;
import net.edge.world.node.actor.player.Player;

public final class SendNpcDrop implements OutgoingPacket {
	
	private final int id;
	private final NpcDropTable table;
	
	public SendNpcDrop(int id, NpcDropTable table) {
		this.id = id;
		this.table = table;
	}
	
	@Override
	public boolean onSent(Player player) {
		return id >= 1;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(121, PacketType.VARIABLE_SHORT);
		msg.putInt(id);
		if(id != 0) {
			if(id > NpcDefinition.DEFINITIONS.length)
				return null;
			NpcDefinition def = NpcDefinition.DEFINITIONS[id];
			if(def == null)
				return null;
			msg.putShort(table.getCommon() == null ? 0 : table.getCommon().size());
			if(table.getCommon() != null) {
				for(ItemCache c : table.getCommon()) {
					msg.putShort(c.ordinal());
				}
			}
			msg.putShort(table.getUnique() == null ? 0 : table.getUnique().size());
			if(table.getUnique() != null) {
				for(NpcDrop d : table.getUnique()) {
					msg.putShort(d.getId());
					msg.putShort(d.getMinimum());
					msg.putShort(d.getMaximum());
					msg.put(d.getChance().ordinal());
				}
			}
		}
		msg.endVarSize();
		return msg.getBuffer();
	}
}
