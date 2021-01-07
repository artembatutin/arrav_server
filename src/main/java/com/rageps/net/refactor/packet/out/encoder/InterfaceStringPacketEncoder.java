package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.InterfaceStringPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceStringPacketEncoder implements PacketEncoder<InterfaceStringPacket> {

    @Override
    public GamePacket encode(InterfaceStringPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(126, PacketType.VARIABLE_SHORT);
        builder.putString(message.getText());
        builder.putShort(message.getId(), DataTransformation.ADD);
        return builder.toGamePacket();
    }
}
