package com.rageps.net.refactor.packet.in.decoder.interface_actions;

import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.interface_actions.EquipItemPacket;
import com.rageps.net.refactor.packet.in.model.interface_actions.SwapSlotPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SwapSlotPacketDecoder implements PacketDecoder<SwapSlotPacket> {

    @Override
    public SwapSlotPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int itemId = reader.getShort(DataTransformation.ADD, DataOrder.LITTLE);
        int fromSlot = reader.getShort(DataTransformation.ADD, DataOrder.LITTLE);
        int toSlot = reader.getShort(DataOrder.LITTLE);

        return new SwapSlotPacket(itemId, fromSlot, toSlot);
    }
}
