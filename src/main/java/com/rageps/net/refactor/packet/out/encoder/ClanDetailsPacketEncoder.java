package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ClanDetailsPacket;
import com.rageps.net.refactor.packet.out.model.ClanMessagePacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ClanDetailsPacketEncoder implements PacketEncoder<ClanDetailsPacket> {

    @Override
    public GamePacket encode(ClanDetailsPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(217, PacketType.VARIABLE_BYTE);
        builder.putString(message.getAuthor());
        builder.putString(message.getMessage());
        builder.putString(message.getClanName());
        builder.putShort(message.getRank().rank);
        return builder.toGamePacket();
    }
}
