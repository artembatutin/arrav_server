package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.ItemOnPlayerPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnPlayerPacketPacketDecoder implements PacketDecoder<ItemOnPlayerPacketPacket> {

    @Override
    public ItemOnPlayerPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int container = reader.getShort(DataTransformation.ADD, DataOrder.BIG);
        int index = reader.getShort();
        int itemUsed = reader.getShort();
        int itemSlot = reader.getShort(false, DataTransformation.ADD, DataOrder.LITTLE);

        return new ItemOnPlayerPacketPacket(container, index, itemUsed, itemSlot);
    }
}
