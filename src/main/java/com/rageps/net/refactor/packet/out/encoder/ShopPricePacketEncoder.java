package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ShopPricePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ShopPricePacketEncoder implements PacketEncoder<ShopPricePacket> {

    @Override
    public GamePacket encode(ShopPricePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(54, PacketType.VARIABLE_SHORT);
        if(message.getItem().getPrice() > 254) {
            builder.put(255);
            builder.putInt(message.getItem().getPrice(), DataOrder.INVERSED_MIDDLE);
        } else {
            builder.put(message.getItem().getPrice());
        }
        builder.putShort(message.getItem().getId() + 1, DataTransformation.ADD, DataOrder.LITTLE);
        return builder.toGamePacket();
    }
}
