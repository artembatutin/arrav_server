package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.CoordinatesPacket;
import com.rageps.net.refactor.packet.out.model.ItemNodeRemovalPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemNodeRemovalPacketEncoder implements PacketEncoder<ItemNodeRemovalPacket> {

    @Override
    public GamePacket encode(ItemNodeRemovalPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(156);
        builder.put(0, DataTransformation.SUBTRACT);
        builder.putShort(message.getItem().getItem().getId());
        return builder.toGamePacket();
    }

    @Override
    public CoordinatesPacket coordinatePacket(ItemNodeRemovalPacket message) {
        return new CoordinatesPacket(message.getItem().getPosition(), message.getPlayer().getLastRegion());
    }
}
