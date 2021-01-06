package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.*;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.InterfaceStringPacket;

public class TextMessagePacketEncoder implements PacketEncoder<InterfaceStringPacket> {

    @Override
    public GamePacket encode(InterfaceStringPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(253, PacketType.VARIABLE_BYTE);
        builder.putString(message.getMessage());
        return builder.toGamePacket();
    }
}