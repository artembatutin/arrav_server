package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.packet.OutgoingPacket;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.DataType;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.CoordinatesPacket;
import com.rageps.net.refactor.packet.out.model.RemoveObjectPacket;
import com.rageps.world.locale.Position;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class RemoveObjectPacketEncoder implements PacketEncoder<RemoveObjectPacket> {


    @Override
    public GamePacket encode(RemoveObjectPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(101);
        builder.put(DataType.BYTE, DataTransformation.NEGATE, message.getType() << 2 | message.getOrientation());
        builder.put(DataType.BYTE, 0);
        return builder.toGamePacket();
    }

    @Override
    public GamePacket coordinatePacket(RemoveObjectPacket removeObjectPacket, Position last) {
        return new CoordinatesPacket(removeObjectPacket.getPositionOffset(), last);
    }
}
