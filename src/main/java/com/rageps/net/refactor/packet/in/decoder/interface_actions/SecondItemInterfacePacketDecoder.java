package com.rageps.net.refactor.packet.in.decoder.interface_actions;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.interface_actions.FirstItemInterfacePacket;
import com.rageps.net.refactor.packet.in.model.interface_actions.SecondItemInterfacePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SecondItemInterfacePacketDecoder implements PacketDecoder<SecondItemInterfacePacket> {

    @Override
    public SecondItemInterfacePacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int interfaceId = reader.getShort(true, DataTransformation.ADD, DataOrder.LITTLE);
        int itemId =  reader.getShort(true, DataTransformation.ADD, DataOrder.LITTLE);
        int slot = reader.getShort(true, DataOrder.LITTLE);

        return new SecondItemInterfacePacket(interfaceId, slot, itemId);
    }
}
