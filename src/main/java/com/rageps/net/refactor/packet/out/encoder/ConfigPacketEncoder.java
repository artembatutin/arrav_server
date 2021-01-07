package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ConfigPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ConfigPacketEncoder implements PacketEncoder<ConfigPacket> {

    @Override
    public GamePacket encode(ConfigPacket message) {
        if(message.getState() < Byte.MIN_VALUE || message.getState()  > Byte.MAX_VALUE) {
            GamePacketBuilder builder = new GamePacketBuilder(87);
            builder.putShort(message.getId(), DataOrder.LITTLE);
            builder.putInt(message.getState(), DataOrder.MIDDLE);
            return builder.toGamePacket();
        }
        GamePacketBuilder builder = new GamePacketBuilder(36);
        builder.putShort(message.getId(), DataOrder.LITTLE);
        builder.put(message.getState());
        return builder.toGamePacket();
    }
}
