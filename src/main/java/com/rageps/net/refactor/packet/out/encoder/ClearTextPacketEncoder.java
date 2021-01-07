package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ClearTextPacket;


/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ClearTextPacketEncoder implements PacketEncoder<ClearTextPacket> {

    @Override
    public GamePacket encode(ClearTextPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(127, PacketType.FIXED);

        for(int i = message.getStart(); i < message.getStart() + message.getCount(); i++) {
            message.getPlayer().interfaceTexts.remove(i);
        }
        builder.putShort(message.getStart(), DataTransformation.ADD);
        builder.putShort(message.getCount(), DataTransformation.ADD);
        return builder.toGamePacket();
    }
}
