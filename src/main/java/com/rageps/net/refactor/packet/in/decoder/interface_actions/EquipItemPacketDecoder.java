package com.rageps.net.refactor.packet.in.decoder.interface_actions;

import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.interface_actions.EquipItemPacket;
import com.rageps.net.refactor.packet.in.model.interface_actions.FirstItemInterfacePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class EquipItemPacketDecoder implements PacketDecoder<EquipItemPacket> {

    @Override
    public EquipItemPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int itemId = reader.getShort(false);
        int slot = reader.getShort(false, DataTransformation.ADD);
        int interfaceId = reader.getShort(false, DataTransformation.ADD);

        return new EquipItemPacket(interfaceId, slot, itemId);
    }
}
