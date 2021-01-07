package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.CoordinatesPacket;
import com.rageps.net.refactor.packet.out.model.ObjectAnimationPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ObjectAnimationPacketEncoder implements PacketEncoder<ObjectAnimationPacket> {

    @Override
    public GamePacket encode(ObjectAnimationPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(160);
        builder.put(((0 & 7) << 4) + (0 & 7), DataTransformation.SUBTRACT);
        builder.put((message.getType() << 2) + (message.getDirection() & 3), DataTransformation.SUBTRACT);
        builder.putShort(message.getAnimation(), DataTransformation.ADD);
        return builder.toGamePacket();
    }

    @Override
    public CoordinatesPacket coordinatePacket(ObjectAnimationPacket message) {
        return new CoordinatesPacket(message.getPosition(), message.getPlayer().getLastRegion());
    }
}
