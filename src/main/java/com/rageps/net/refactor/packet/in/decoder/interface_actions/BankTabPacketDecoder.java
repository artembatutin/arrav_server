package com.rageps.net.refactor.packet.in.decoder.interface_actions;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.interface_actions.BankTabPacket;
import com.rageps.net.refactor.packet.in.model.interface_actions.EquipItemPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class BankTabPacketDecoder implements PacketDecoder<BankTabPacket> {

    @Override
    public BankTabPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int tab = reader.getShort(DataTransformation.ADD, DataOrder.LITTLE);
        int fromSlot = reader.getShort(DataTransformation.ADD, DataOrder.LITTLE);
        int toTab = reader.getShort(DataTransformation.ADD, DataOrder.LITTLE);

        return new BankTabPacket(tab, fromSlot, toTab);
    }
}
