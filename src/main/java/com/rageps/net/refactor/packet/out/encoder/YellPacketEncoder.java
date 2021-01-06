package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.YellPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class YellPacketEncoder implements PacketEncoder<YellPacket> {

    @Override
    public GamePacket encode(YellPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(210, PacketType.VARIABLE_BYTE);
        builder.putString(message.getAuthor());
        builder.putString(message.getAuthor());
        builder.putShort(message.getRank().getProtocolValue());
        return builder.toGamePacket();
    }
}
