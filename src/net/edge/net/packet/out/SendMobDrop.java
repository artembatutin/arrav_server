package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.game.GamePacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.mob.MobDefinition;
import net.edge.world.entity.actor.mob.drop.Drop;
import net.edge.world.entity.actor.mob.drop.DropTable;
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
		return id >= 0;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(121, GamePacketType.VARIABLE_SHORT);
		buf.putInt(id);
		if(id != 0) {
			if(id > MobDefinition.DEFINITIONS.length)
				return null;
			MobDefinition def = MobDefinition.DEFINITIONS[id];
			if(def == null)
				return null;
			buf.putShort(table == null ? 0 : table.getCommon().size() + table.getRare().size());
			if(table != null && table.getCommon() != null) {
				for(Drop d : table.getCommon()) {
					buf.putShort(d.getId());
					buf.putShort(d.getMinimum());
					buf.putShort(d.getMaximum());
					buf.put(d.getChance().ordinal());
				}
			}
			if(table != null && table.getRare() != null) {
				for(Drop d : table.getRare()) {
					buf.putShort(d.getId());
					buf.putShort(d.getMinimum());
					buf.putShort(d.getMaximum());
					buf.put(d.getChance().ordinal());
				}
			}
		}
		buf.endVarSize();
		return buf;
	}
}
