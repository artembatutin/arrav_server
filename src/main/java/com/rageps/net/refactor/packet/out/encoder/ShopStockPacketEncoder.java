package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ShopStockPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ShopStockPacketEncoder implements PacketEncoder<ShopStockPacket> {

    @Override
    public GamePacket encode(ShopStockPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(55, PacketType.VARIABLE_SHORT);
        if(message.getItem().getStock() > 254) {
            builder.put(255);
            builder.putInt(message.getItem().getStock(), DataOrder.INVERSED_MIDDLE);
        } else {
            builder.put(message.getItem().getStock());
        }
        builder.putShort(message.getItem().getId() + 1, DataTransformation.ADD, DataOrder.LITTLE);
        return builder.toGamePacket();
    }
}
