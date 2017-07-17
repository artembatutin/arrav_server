package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.World;
import net.edge.world.node.EntityState;
import net.edge.world.node.actor.player.Player;

public final class SendLogout implements OutgoingPacket {
	
	
	@Override
	public boolean onSent(Player player) {
		if(player.getState() != EntityState.AWAITING_REMOVAL)
			World.get().queueLogout(player);
		return true;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(109, PacketType.VARIABLE_SHORT);
		msg.endVarSize();
		return msg.getBuffer();
	}
}
