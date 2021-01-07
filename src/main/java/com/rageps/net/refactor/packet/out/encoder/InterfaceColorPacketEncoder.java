package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.InterfaceColorPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceColorPacketEncoder implements PacketEncoder<InterfaceColorPacket> {

    @Override
    public GamePacket encode(InterfaceColorPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(122);
        builder.putShort(message.getId(), DataTransformation.ADD, DataOrder.LITTLE);
        builder.putShort(message.getColor(), DataTransformation.ADD, DataOrder.LITTLE);
        return builder.toGamePacket();
    }
}
