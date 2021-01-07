package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ArrowEntityPacket;
import com.rageps.net.refactor.packet.out.model.ScrollBarPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ScrollBarPacketEncoder implements PacketEncoder<ScrollBarPacket> {

    @Override
    public GamePacket encode(ScrollBarPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(79);
        builder.putInt(message.getId());
        builder.putShort(message.getSize());
        return builder.toGamePacket();
    }
}
