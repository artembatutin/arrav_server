package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.FirstItemActionPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class FirstItemPacketDecoder implements PacketDecoder<FirstItemActionPacket> {

    @Override
    public FirstItemActionPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        int container = reader.getShort(true, DataTransformation.ADD, DataOrder.LITTLE);
        int slot = reader.getShort(false, DataTransformation.ADD);
        int id = reader.getShort(false, DataOrder.LITTLE);
        return new FirstItemActionPacket(container, slot, id);
    }
}
