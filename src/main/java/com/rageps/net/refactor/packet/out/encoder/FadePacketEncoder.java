package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.FadePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class FadePacketEncoder implements PacketEncoder<FadePacket> {

    @Override
    public GamePacket encode(FadePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(80);
        builder.put(message.getStart());
        builder.put(message.getDuration());
        builder.put(message.getEnd());
        return builder.toGamePacket();
    }
}
