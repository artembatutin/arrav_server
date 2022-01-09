package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ArrowPositionPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ArrowPositionPacketEncoder implements PacketEncoder<ArrowPositionPacket> {

    @Override
    public GamePacket encode(ArrowPositionPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(254);
        builder.put(message.getDirection());
        builder.putShort(message.getPosition().getX());
        builder.putShort(message.getPosition().getY());
        builder.put(message.getPosition().getZ());
        return builder.toGamePacket();
    }
}
