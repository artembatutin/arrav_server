package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.PickupItemPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class PickupItemPacketPacketDecoder implements PacketDecoder<PickupItemPacketPacket> {

    @Override
    public PickupItemPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        int itemY = reader.getShort(DataOrder.LITTLE);
        int itemId = reader.getShort(false);
        int itemX = reader.getShort(DataOrder.LITTLE);
        return new PickupItemPacketPacket(itemY, itemId, itemX);
    }
}
