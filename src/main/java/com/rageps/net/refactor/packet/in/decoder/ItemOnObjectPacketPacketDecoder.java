package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.ItemOnObjectPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnObjectPacketPacketDecoder implements PacketDecoder<ItemOnObjectPacketPacket> {

    @Override
    public ItemOnObjectPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int container = reader.getShort(false);
        int objectId = reader.getMedium();
        int objectY = reader.getShort(true, DataTransformation.ADD, DataOrder.LITTLE);
        int slot = reader.getShort(true, DataOrder.LITTLE);
        int objectX = reader.getShort(true, DataTransformation.ADD, DataOrder.LITTLE);
        int itemId = reader.getShort(false);
        
        
        return new ItemOnObjectPacketPacket(container, objectId, objectY, slot, objectX, itemId);
    }
}
