package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.BroadcastPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class BroadcastPacketEncoder implements PacketEncoder<BroadcastPacket> {

    @Override
    public GamePacket encode(BroadcastPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(114, PacketType.VARIABLE_BYTE);
        builder.putShort(message.getId());
        builder.putShort(message.getTime() * 3000);
        builder.putString(message.getContext());
        return builder.toGamePacket();
    }
}
