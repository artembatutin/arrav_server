package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.FeedMessagePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class FeedMessagePacketEncoder implements PacketEncoder<FeedMessagePacket> {

    @Override
    public GamePacket encode(FeedMessagePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(175, PacketType.VARIABLE_BYTE);
        builder.putString(message.getMessage());
        builder.putString(message.getColor());
        return builder.toGamePacket();
    }
}
