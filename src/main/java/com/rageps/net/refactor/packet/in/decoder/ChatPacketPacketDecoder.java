package com.rageps.net.refactor.packet.in.decoder;

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
        return new ChatPacketPacket();
    }
}
