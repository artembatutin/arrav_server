package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.content.market.MarketItem;
import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ShopPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ShopPacketEncoder implements PacketEncoder<ShopPacket> {

    @Override
    public GamePacket encode(ShopPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(53, PacketType.VARIABLE_SHORT);
        builder.putShort(message.getId());
        if(message.getItems() == null) {
            builder.putShort(0);
            builder.putShort(0);
            builder.put(0);
            builder.putShort(0, DataTransformation.ADD, DataOrder.LITTLE);
        } else {
            builder.putShort(message.getItems().size());
            builder.putShort(message.getItems().size());
            for(int i : message.getItems()) {
                MarketItem item = MarketItem.get(i);
                if(item != null) {
                    builder.put(item.isUnlimitedStock() ? 1 : 0);
                    if(!item.isUnlimitedStock()) {
                        if(item.getStock() > 254) {
                            builder.put(255);
                            builder.putInt(item.getStock(), DataOrder.INVERSED_MIDDLE);
                        } else {
                            builder.put(item.getStock());
                        }
                    }
                    boolean noted = false;//todo why is it never noted? I think i remember messing with this.
                    builder.putShort(item.getId() + (noted ? 0 : 1), DataTransformation.ADD, DataOrder.LITTLE);
                    if(item.getPrice() > 254) {
                        builder.put(255);
                        builder.putInt(item.getPrice(), DataOrder.INVERSED_MIDDLE);
                    } else {
                        builder.put(item.getPrice());
                    }
                } else {
                    builder.put(0);
                    builder.putShort(0, DataTransformation.ADD, DataOrder.LITTLE);
                    builder.put(0);
                    builder.put(0);
                }
            }
        }
        return builder.toGamePacket();
    }
}
