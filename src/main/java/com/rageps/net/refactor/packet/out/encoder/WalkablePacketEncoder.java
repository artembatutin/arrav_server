package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.WalkablePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class WalkablePacketEncoder implements PacketEncoder<WalkablePacket> {

    @Override
    public GamePacket encode(WalkablePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(208);
        builder.putInt(message.getId());
        return builder.toGamePacket();
    }
}
