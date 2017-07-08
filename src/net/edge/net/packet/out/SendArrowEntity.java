package net.edge.net.packet.out;

import net.edge.locale.Position;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.player.Player;

public final class SendArrowEntity implements OutgoingPacket {
	
	private final EntityNode entity;
	
	public SendArrowEntity(EntityNode entity) {
		this.entity = entity;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(248);
		msg.put(entity.isNpc() ? 1 : 10);
		msg.putShort(entity.getSlot());
		msg.put(0);
	}
}
