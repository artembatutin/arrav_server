package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ItemModelInterfacePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemModelInterfacePacketEncoder implements PacketEncoder<ItemModelInterfacePacket> {

    @Override
    public GamePacket encode(ItemModelInterfacePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(246);
        builder.putShort(message.getId(), DataOrder.LITTLE);
        builder.putShort(message.getZoom());
        builder.putShort(message.getModel());
        return builder.toGamePacket();
    }
}
