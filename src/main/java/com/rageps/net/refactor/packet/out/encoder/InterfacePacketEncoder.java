package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.InterfacePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfacePacketEncoder implements PacketEncoder<InterfacePacket> {

    @Override
    public GamePacket encode(InterfacePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(0);
        return builder.toGamePacket();
    }
}
