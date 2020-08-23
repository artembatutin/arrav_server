package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.LogoutPacket;
import com.rageps.world.World;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.actor.player.Player;
import io.netty.channel.ChannelFutureListener;

/**
 * A {@link PacketEncoder} for the {@link LogoutPacket}.
 *
 * @author Graham
 */
public final class LogoutMessageEncoder implements PacketEncoder<LogoutPacket> {

	@Override
	public boolean onSent() {
		if(player.getState() != EntityState.AWAITING_REMOVAL && player.getState() != EntityState.INACTIVE)
			World.get().getGameService().unregisterPlayer(player);
		return true;
	}

	@Override
	public GamePacket encode(LogoutPacket message) {
		GamePacketBuilder builder = new GamePacketBuilder(109);
		return builder.toGamePacket();
	}

}