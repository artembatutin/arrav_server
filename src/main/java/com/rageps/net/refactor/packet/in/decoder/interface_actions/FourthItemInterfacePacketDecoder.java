package com.rageps.net.refactor.packet.in.decoder.interface_actions;

import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.ThirdItemActionPacket;
import com.rageps.net.refactor.packet.in.model.interface_actions.FourthItemInterfacePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class FourthItemInterfacePacketDecoder implements PacketDecoder<FourthItemInterfacePacket> {

    @Override
    public FourthItemInterfacePacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int slot = reader.getShort(DataTransformation.ADD);
        int interfaceId = reader.getShort();
        int itemId =  reader.getShort(DataTransformation.ADD);

        return new FourthItemInterfacePacket(interfaceId, slot, itemId);
    }
}
