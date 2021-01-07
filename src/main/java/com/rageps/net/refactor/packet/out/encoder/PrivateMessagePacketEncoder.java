package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.PrivateMessagePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class PrivateMessagePacketEncoder implements PacketEncoder<PrivateMessagePacket> {

    @Override
    public GamePacket encode(PrivateMessagePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(196, PacketType.VARIABLE_BYTE);
        builder.putLong(message.getName());
        builder.putInt(message.getPrivateMessageId());
        builder.put(message.getRights());
        builder.putBytes(message.getMessage(), message.getSize());
        return builder.toGamePacket();
    }
}
