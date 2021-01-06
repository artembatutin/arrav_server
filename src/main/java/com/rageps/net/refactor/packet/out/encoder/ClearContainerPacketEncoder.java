package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ClearContainerPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ClearContainerPacketEncoder implements PacketEncoder<ClearContainerPacket> {

    @Override
    public GamePacket encode(ClearContainerPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(0);
        return builder.toGamePacket();
    }
}
