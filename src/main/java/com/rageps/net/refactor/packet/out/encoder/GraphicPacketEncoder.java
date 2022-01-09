package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.CoordinatesPacket;
import com.rageps.net.refactor.packet.out.model.GraphicPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class GraphicPacketEncoder implements PacketEncoder<GraphicPacket> {

    @Override
    public GamePacket encode(GraphicPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(4);
        builder.put(0);
        builder.putShort(message.getId());
        builder.put(message.getLevel());
        builder.putShort(0);
        return builder.toGamePacket();
    }

    @Override
    public CoordinatesPacket coordinatePacket(GraphicPacket message) {
        return new CoordinatesPacket(message.getPosition(), message.getPlayer().getLastRegion());
    }
}
