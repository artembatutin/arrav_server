package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.MobUpdatePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MobUpdatePacketEncoder implements PacketEncoder<MobUpdatePacket> {

    @Override
    public GamePacket encode(MobUpdatePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(0);
        Thread.dumpStack();
        return builder.toGamePacket();
    }
}
