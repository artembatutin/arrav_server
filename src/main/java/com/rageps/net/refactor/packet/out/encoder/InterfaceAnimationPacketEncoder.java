package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.InterfaceAnimationPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceAnimationPacketEncoder implements PacketEncoder<InterfaceAnimationPacket> {

    @Override
    public GamePacket encode(InterfaceAnimationPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(200);
        builder.putShort(message.getId());
        builder.putShort(message.getAnimation());
        return builder.toGamePacket();
    }
}
