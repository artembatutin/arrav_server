package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.CoordinatesPacket;
import com.rageps.net.refactor.packet.out.model.ObjectPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ObjectPacketEncoder implements PacketEncoder<ObjectPacket> {

    @Override
    public GamePacket encode(ObjectPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(151);
        builder.put(0, DataTransformation.NEGATE);
        builder.putInt(message.getObject().getId());
        builder.put((message.getObject().getObjectType().getId() << 2) + (message.getObject().getDirection().getId() & 3), DataTransformation.SUBTRACT);
        return builder.toGamePacket();
    }

    @Override
    public CoordinatesPacket coordinatePacket(ObjectPacket message) {
        return new CoordinatesPacket(message.getObject().getPosition(), message.getPlayer().getLastRegion());
    }
}
