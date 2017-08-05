package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.mob.MobDefinition;
import net.edge.world.entity.actor.mob.drop.DropTable;
import net.edge.world.entity.actor.mob.drop.Drop;
import net.edge.world.entity.actor.player.Player;

public final class SendMobDrop implements OutgoingPacket {
	
	private final int id;
	private final DropTable table;
	
	public SendMobDrop(int id, DropTable table) {
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
			if(id > MobDefinition.DEFINITIONS.length)
				return null;
			MobDefinition def = MobDefinition.DEFINITIONS[id];
			if(def == null)
				return null;
			msg.putShort(table == null || table.getDrops() == null ? 0 : table.getDrops().size());
			if(table != null && table.getDrops() != null) {
				for(Drop d : table.getDrops()) {
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
