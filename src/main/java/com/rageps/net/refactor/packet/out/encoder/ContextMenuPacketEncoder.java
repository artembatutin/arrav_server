package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ContextMenuPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ContextMenuPacketEncoder implements PacketEncoder<ContextMenuPacket> {

    @Override
    public GamePacket encode(ContextMenuPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(104, PacketType.VARIABLE_BYTE);
        builder.put(message.getSlot(), DataTransformation.NEGATE);
        builder.put(message.isTop() ? 1 : 0, DataTransformation.ADD);
        builder.putString(message.getOption());
        return builder.toGamePacket();
    }
}
