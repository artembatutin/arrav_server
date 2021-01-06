package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ChatOptionPacket;
import com.rageps.net.refactor.packet.out.model.ClanBannedPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ChatOptionPacketEncoder implements PacketEncoder<ChatOptionPacket> {

    @Override
    public GamePacket encode(ChatOptionPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(206);
        builder.put(message.getPublicChat().getCode());
        builder.put(message.getPrivateChat().getCode());
        builder.put(message.getClanChat().getCode());
        builder.put(message.getTradeChat().getCode());
        return builder.toGamePacket();
    }
}
