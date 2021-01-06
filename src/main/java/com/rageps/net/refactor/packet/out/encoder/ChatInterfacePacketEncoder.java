package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ChatInterfacePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ChatInterfacePacketEncoder implements PacketEncoder<ChatInterfacePacket> {

    @Override
    public GamePacket encode(ChatInterfacePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(164);
        builder.putShort(message.getId(), DataOrder.LITTLE);
        return builder.toGamePacket();
    }
}
