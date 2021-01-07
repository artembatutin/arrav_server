package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ItemOnInterfaceSlotPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnInterfaceSlotPacketEncoder implements PacketEncoder<ItemOnInterfaceSlotPacket> {

    @Override
    public GamePacket encode(ItemOnInterfaceSlotPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(34, PacketType.VARIABLE_SHORT);
        builder.putShort(message.getId());
        builder.put(message.getSlot());
        builder.putShort(message.getItem() == null ? 0 : message.getItem().getId() + 1);
        int am = message.getItem() == null ? 0 : message.getItem().getAmount();
        if(am > 254) {
            builder.put(255);
            builder.putInt(am);
        } else {
            builder.put(am);
        }
        return builder.toGamePacket();
    }
}
