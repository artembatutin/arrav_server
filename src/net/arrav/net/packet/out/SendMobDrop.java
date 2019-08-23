package net.arrav.net.packet.out;


import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.mob.MobDefinition;
import net.arrav.world.entity.actor.mob.drop.Drop;
import net.arrav.world.entity.actor.mob.drop.DropTable;
import net.arrav.world.entity.actor.player.Player;

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
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(121, GamePacketType.VARIABLE_SHORT);
		out.putInt(id);
		if(id != 0) {
			if(id > MobDefinition.DEFINITIONS.length)
				return null;
			MobDefinition def = MobDefinition.DEFINITIONS[id];
			if(def == null)
				return null;
			out.putShort(table == null ? 0 : table.getCommon().size() + table.getRare().size());
			if(table != null && table.getCommon() != null) {
				for(Drop d : table.getCommon()) {
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
		return out;
	}
}
