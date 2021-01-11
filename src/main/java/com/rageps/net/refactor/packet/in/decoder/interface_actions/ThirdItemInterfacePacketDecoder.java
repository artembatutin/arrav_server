package com.rageps.net.refactor.packet.in.decoder.interface_actions;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.ThirdItemActionPacket;
import com.rageps.net.refactor.packet.in.model.interface_actions.SecondItemInterfacePacket;
import com.rageps.net.refactor.packet.in.model.interface_actions.ThirdItemInterfacePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ThirdItemInterfacePacketDecoder implements PacketDecoder<ThirdItemInterfacePacket> {

    @Override
    public ThirdItemInterfacePacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int interfaceId = reader.getShort(DataOrder.LITTLE);
        int slot = reader.getShort(DataTransformation.ADD);
        int itemId =  reader.getShort(DataTransformation.ADD);

        return new ThirdItemInterfacePacket(interfaceId, slot, itemId);
    }
}
