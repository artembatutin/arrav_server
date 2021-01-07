package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.InterfaceNpcModelPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceNpcModelPacketEncoder implements PacketEncoder<InterfaceNpcModelPacket> {

    @Override
    public GamePacket encode(InterfaceNpcModelPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(75);
        builder.putShort(message.getModel(), DataTransformation.ADD, DataOrder.LITTLE);
        builder.putShort(message.getId(), DataTransformation.ADD, DataOrder.LITTLE);
        return builder.toGamePacket();
    }
}
