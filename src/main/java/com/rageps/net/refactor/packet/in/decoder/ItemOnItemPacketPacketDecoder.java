package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.ItemOnItemPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnItemPacketPacketDecoder implements PacketDecoder<ItemOnItemPacketPacket> {

    @Override
    public ItemOnItemPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int secondSlot = reader.getShort();
        int firstSlot = reader.getShort(DataTransformation.ADD);
        reader.getShort();
        reader.getShort();

        return new ItemOnItemPacketPacket(firstSlot, secondSlot);
    }
}
