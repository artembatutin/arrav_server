package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.InputXOptionPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InputXOptionPacketPacketDecoder implements PacketDecoder<InputXOptionPacketPacket> {

    @Override
    public InputXOptionPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int slot = reader.getShort(DataOrder.LITTLE);
        int interfaceId = reader.getShort(DataTransformation.ADD);
        int itemId = reader.getShort(DataOrder.LITTLE);

        return new InputXOptionPacketPacket(slot, interfaceId, itemId);
    }
}
