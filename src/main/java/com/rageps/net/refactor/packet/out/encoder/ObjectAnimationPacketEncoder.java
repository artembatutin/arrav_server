package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ObjectAnimationPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ObjectAnimationPacketEncoder implements PacketEncoder<ObjectAnimationPacket> {

    @Override
    public GamePacket encode(ObjectAnimationPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(0);
        return builder.toGamePacket();
    }
}
