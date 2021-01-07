package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ClearContainerPacket;
import com.rageps.net.refactor.packet.out.model.ItemsOnInterfacePacket;
import com.rageps.world.entity.item.Item;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemsOnInterfacePacketEncoder implements PacketEncoder<ItemsOnInterfacePacket> {

    @Override
    public boolean onSent(ItemsOnInterfacePacket message) {
        if(message.getId() == -1)
            return false;
        if(message.getItems().length == 0) {
            message.getPlayer().send(new ClearContainerPacket(message.getId()));
            return false;
        }
        return true;
    }

    @Override
    public GamePacket encode(ItemsOnInterfacePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(53, PacketType.VARIABLE_SHORT);
        builder.putShort(message.getId());
        if(message.getItems() == null) {
            builder.putShort(0);
            builder.putShort(0);
            builder.put(0);
            builder.putShort(0, DataTransformation.ADD, DataOrder.LITTLE);
        } else {
            int count = message.getItems().length;
            builder.putShort(message.getCapacity());
            builder.putShort(count);
            for(Item item : message.getItems()) {
                if(count == 0)
                    break;
                if(item != null) {
                    count--;
                    if(item.getAmount() > 254) {
                        builder.put(255);
                        builder.putInt(item.getAmount(), DataOrder.INVERSED_MIDDLE);
                    } else {
                        builder.put(item.getAmount());
                    }
                    builder.putShort(item.getId() + 1, DataTransformation.ADD, DataOrder.LITTLE);
                } else {
                    builder.put(0);
                    builder.putShort(0, DataTransformation.ADD, DataOrder.LITTLE);
                }
            }
        }
        return builder.toGamePacket();
    }
}
