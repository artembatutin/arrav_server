package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.MoveComponentPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MoveComponentPacketEncoder implements PacketEncoder<MoveComponentPacket> {

    @Override
    public GamePacket encode(MoveComponentPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(70);
        builder.putShort(message.getX());
        builder.putShort(message.getY(), DataOrder.LITTLE);
        builder.putShort(message.getId(), DataOrder.LITTLE);
        return builder.toGamePacket();
    }
}
