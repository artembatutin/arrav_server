package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.MoveComponentPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MoveComponentPacketEncoder implements PacketEncoder<MoveComponentPacket> {

    @Override
    public GamePacket encode(MoveComponentPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(0);
        return builder.toGamePacket();
    }
}
