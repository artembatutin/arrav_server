package net.edge.net.packet.out;

import net.edge.content.TabInterface;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.MessageType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;

public final class SendLogout implements OutgoingPacket {
	
	private final boolean queue;
	
	public SendLogout(boolean queue) {
		this.queue = queue;
	}
	
	@Override
	public void write(Player player) {
		if(queue)
			World.get().queueLogout(player, true);
		if(player.getSession().getChannel().isActive()) {
			GameBuffer msg = player.getSession().getStream();
			msg.message(109, MessageType.VARIABLE_SHORT);
			msg.endVarSize();
		}
	}
}
