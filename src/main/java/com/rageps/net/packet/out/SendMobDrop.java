package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.mob.drop.DropTable;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

//todo - reimplement this when i do drop interface
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
	public GamePacket write(Player player, ByteBuf buf) {
		/*GamePacket out = new GamePacket(this, buf);
		out.message(121, GamePacketType.VARIABLE_SHORT);
		out.putInt(id);
		if(id != 0) {
			if(id > MobDefinition.DEFINITIONS.length)
				return null;
			MobDefinition def = MobDefinition.DEFINITIONS[id];
			if(def == null)
				return null;
			out.putShort(table == null ? 0 : table.getDrops().size() + table.getRare().size());
			if(table != null && table.getDrops() != null) {
				for(Drop d : table.getDrops()) {
					out.putShort(d.getId());
					out.putShort(d.getMinimum());
					out.putShort(d.getMaximum());
					out.put(d.getChance().ordinal());
				}
			}
			if(table != null && table.getRare() != null) {
				for(Drop d : table.getRare()) {
					out.putShort(d.getId());
					out.putShort(d.getMinimum());
					out.putShort(d.getMaximum());
					out.put(d.getChance().ordinal());
				}
			}
		}
		out.endVarSize();
		return out;*/
		return null;
	}
}
