package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.*;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.InterfaceStringPacket;
import com.rageps.net.refactor.packet.out.model.TextMessagePacket;

public class InterfaceStringPacketEncoder implements PacketEncoder<InterfaceStringPacket> {

    @Override
    public GamePacket encode(InterfaceStringPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(126, PacketType.VARIABLE_SHORT);
        builder.putString(message.getMessage());
        builder.put(DataType.SHORT, DataTransformation.ADD, message.getInterfaceId());
        return builder.toGamePacket();
    }
}