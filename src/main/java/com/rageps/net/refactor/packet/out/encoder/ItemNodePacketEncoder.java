package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.net.packet.out.SendCoordinates;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.CoordinatesPacket;
import com.rageps.net.refactor.packet.out.model.ItemNodePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemNodePacketEncoder implements PacketEncoder<ItemNodePacket> {

    @Override
    public GamePacket encode(ItemNodePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(44);
        builder.putShort(message.getItem().getItem().getId(), DataTransformation.ADD, DataOrder.LITTLE);
        builder.putShort(message.getItem().getItem().getAmount());
        builder.put(0);
        return builder.toGamePacket();
    }

    @Override
    public CoordinatesPacket coordinatePacket(ItemNodePacket message) {
        return new CoordinatesPacket(message.getItem().getPosition(), message.getPlayer().getLastRegion());
    }
}
