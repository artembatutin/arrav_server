package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.EnterAmountPacket;
import com.rageps.net.refactor.packet.out.model.EnterNamePacket;

import java.util.Optional;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class EnterNamePacketEncoder implements PacketEncoder<EnterNamePacket> {

    @Override
    public GamePacket encode(EnterNamePacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(187, PacketType.VARIABLE_BYTE);
        builder.putString(message.getTitle());
        return builder.toGamePacket();
    }
    @Override
    public boolean onSent(EnterNamePacket message) {
        message.getPlayer().setEnterInputListener(Optional.of(message.getAction()));
        return false;
    }
}
