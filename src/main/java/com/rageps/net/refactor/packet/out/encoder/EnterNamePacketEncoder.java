package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.EnterNamePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class EnterNamePacketEncoder implements PacketEncoder<EnterNamePacket> {

    @Override
    public GamePacket encode(EnterNamePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(0);
        return builder.toGamePacket();
    }
}
