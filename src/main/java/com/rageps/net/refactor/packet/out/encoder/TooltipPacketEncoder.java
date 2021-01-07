package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ArrowEntityPacket;
import com.rageps.net.refactor.packet.out.model.TooltipPacket;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class TooltipPacketEncoder implements PacketEncoder<TooltipPacket> {

    @Override
    public GamePacket encode(TooltipPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(203, PacketType.VARIABLE_BYTE);
        builder.putInt(message.getId());
        builder.putString(message.getString());
        return builder.toGamePacket();
    }
}
