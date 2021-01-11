package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.ItemOnMobPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnMobPacketPacketDecoder implements PacketDecoder<ItemOnMobPacketPacket> {

    @Override
    public ItemOnMobPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int itemId = reader.getShort(false, DataTransformation.ADD);
        int mob = reader.getShort(false, DataTransformation.ADD);
        int slot = reader.getShort(true, DataOrder.LITTLE);
        int container = reader.getShort(false, DataTransformation.ADD);


        return new ItemOnMobPacketPacket(itemId, mob, slot, container);
    }
}
