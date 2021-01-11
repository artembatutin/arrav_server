package com.rageps.net.refactor.packet.in.decoder;

import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketReader;
import com.rageps.net.refactor.packet.in.PacketDecoder;
import com.rageps.net.refactor.packet.in.model.ChatPacketPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ChatPacketPacketDecoder implements PacketDecoder<ChatPacketPacket> {

    @Override
    public ChatPacketPacket decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        int effects = reader.get(false, DataTransformation.SUBTRACT);
        int color = reader.get(false, DataTransformation.SUBTRACT);
        int chatLength = packet.getLength() - 2;
        byte[] text = reader.getBytesReverse(chatLength, DataTransformation.ADD);
        return new ChatPacketPacket(effects, color, text);
    }
}
