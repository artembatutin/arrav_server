package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.World;
import net.arrav.world.entity.EntityState;
import net.arrav.world.entity.actor.player.Player;

public final class SendLogout implements OutgoingPacket {
	
	@Override
	public boolean onSent(Player player) {
		if(player.getState() != EntityState.AWAITING_REMOVAL && player.getState() != EntityState.INACTIVE)
			World.get().queueLogout(player);
		return true;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(109, GamePacketType.VARIABLE_SHORT);
		out.endVarSize();
		return out;
	}
}
