package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.ItemOnItemNodePacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnItemNodePacketPacketDecoder implements PacketDecoder<ItemOnItemNodePacketPacket> {

    @Override
    public ItemOnItemNodePacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        final int itemX = reader.getShort(DataOrder.LITTLE);
        final int itemY = reader.getShort(true, DataTransformation.ADD, DataOrder.LITTLE);
        final int itemId = reader.getShort(DataTransformation.ADD);

        return new ItemOnItemNodePacketPacket(itemX, itemY, itemId);
    }
}
