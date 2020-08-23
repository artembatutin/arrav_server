package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.*;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.GroundItemPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class GroundItemPacketEncoder implements PacketEncoder<GroundItemPacket> {
    @Override
    public GamePacket encode(GroundItemPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(44);
        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getItem().getItem().getId());
        builder.put(DataType.SHORT, message.getItem().getItem().getAmount());
        builder.put(DataType.BYTE, 0);
        return builder.toGamePacket();
    }

}
