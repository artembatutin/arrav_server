package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.LogoutPacket;

/**
 * A {@link PacketEncoder} for the {@link LogoutPacket}.
 *
 * @author Graham
 */
public final class LogoutMessageEncoder extends PacketEncoder<LogoutPacket> {

	@Override
	public GamePacket encode(LogoutPacket message) {
		GamePacketBuilder builder = new GamePacketBuilder(109);
		return builder.toGamePacket();
	}

}