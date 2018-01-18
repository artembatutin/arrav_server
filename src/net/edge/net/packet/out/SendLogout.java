package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.game.GamePacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.player.Player;

public final class SendLogout implements OutgoingPacket {
	
	@Override
	public boolean onSent(Player player) {
		if(player.getState() != EntityState.AWAITING_REMOVAL && player.getState() != EntityState.INACTIVE)
			World.get().queueLogout(player);
		return true;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(109, GamePacketType.VARIABLE_SHORT);
		buf.endVarSize();
		return buf;
	}
}
