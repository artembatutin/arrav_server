package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.WalkableInterfacePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class WalkableInterfacePacketEncoder implements PacketEncoder<WalkableInterfacePacket> {

    @Override
    public GamePacket encode(WalkableInterfacePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(208);
        builder.putInt(message.getId());
        return builder.toGamePacket();
    }
}