package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.CoordinatesPacket;
import com.rageps.net.refactor.packet.out.model.ObjectRemovalPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ObjectRemovalPacketEncoder implements PacketEncoder<ObjectRemovalPacket> {

    @Override
    public GamePacket encode(ObjectRemovalPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(101);
        builder.put((message.getObject().getObjectType().getId() << 2) + (message.getObject().getDirection().getId() & 3), DataTransformation.NEGATE);
        builder.put(0);
        return builder.toGamePacket();
    }

    @Override
    public CoordinatesPacket coordinatePacket(ObjectRemovalPacket message) {
        return new CoordinatesPacket(message.getObject().getPosition(), message.getPlayer().getLastRegion());
    }
}
