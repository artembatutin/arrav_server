package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.MessagePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MessagePacketEncoder implements PacketEncoder<MessagePacket> {

    @Override
    public GamePacket encode(MessagePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(253, PacketType.VARIABLE_BYTE);
        builder.putString(message.getMessage());
        return builder.toGamePacket();
    }
}
