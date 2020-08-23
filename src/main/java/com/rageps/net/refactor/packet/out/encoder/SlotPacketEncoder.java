package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.*;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.SlotPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SlotPacketEncoder implements PacketEncoder<SlotPacket> {

    @Override
    public GamePacket encode(SlotPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(249);
        builder.put(DataType.BYTE, DataTransformation.ADD, 1);
        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getSlot());
        return null;
    }
}
