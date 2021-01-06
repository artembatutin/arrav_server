package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.MultiIconPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MultiIconPacketEncoder implements PacketEncoder<MultiIconPacket> {

    @Override
    public GamePacket encode(MultiIconPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(0);
        return builder.toGamePacket();
    }
}
