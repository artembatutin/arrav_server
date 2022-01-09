package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.MobDropPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MobDropPacketEncoder implements PacketEncoder<MobDropPacket> {

    @Override
    public GamePacket encode(MobDropPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(0);
        return builder.toGamePacket();
    }
}
